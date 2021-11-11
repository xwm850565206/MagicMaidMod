package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.EntityAIMaidAttackRanged;
import com.xwm.magicmaid.entity.ai.selina.EntityAIPandora;
import com.xwm.magicmaid.entity.ai.selina.EntityAISelinaServe;
import com.xwm.magicmaid.entity.ai.selina.EntityAIWhisper;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import com.xwm.magicmaid.object.item.equipment.EquipmentAttribute;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import com.xwm.magicmaid.object.item.equipment.ItemWhisper;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import com.xwm.magicmaid.util.handlers.LootTableHandler;
import com.xwm.magicmaid.util.handlers.PunishOperationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

import static com.xwm.magicmaid.registry.MagicEquipmentRegistry.*;

public class EntityMagicMaidSelina extends EntityMagicMaid implements IRangedAttackMob
{
    public EntityMagicMaidSelina(World worldIn) {
        super(worldIn);
        weaponStandbyPos = new BlockPos(1, 1, 0);
    }

    @Override
    protected void entityInit(){
        super.entityInit();
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();

        this.tasks.addTask(2, new EntityAIMaidAttackRanged(this, 1.3D, 40, 10));
        this.tasks.addTask(3, new EntityAIPandora(this));
        this.tasks.addTask(3, new EntityAIWhisper(this));
        this.tasks.addTask(3, new EntityAISelinaServe(this));


    }
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40);
    }


    @Override
    public int getAttackDamage(EquipmentAttribute type){

        if (NONE.equals(type)) {
            return 10;
        } else if (PANDORA.equals(type)) {
            return 1 + getRank();
        } else if (WHISPER.equals(type)) {
            return 10 + 10 * getRank();
        }
        return super.getAttackDamage(type);
    }

    @Override
    public int getAttackColdTime(EquipmentAttribute type){
        if (NORMAL.equals(type)) {
            return 20;
        } else if (PANDORA.equals(type)) {
            return 60 - 5 * this.getRank();
        } else if (WHISPER.equals(type)) {
            return 100 - 10 * this.getRank();
        }
        return super.getAttackColdTime(type);
    }
    @Override
    public void onLivingUpdate() {

        super.onLivingUpdate();

        if (!world.isRemote)
        {
            EnumSelineState state = EnumSelineState.valueOf(this.getState());
            EnumMode mode = EnumMode.valueOf(this.getMode());
            if (mode == EnumMode.SITTING && state != EnumSelineState.SITTING)
                this.setState(EnumSelineState.toInt(EnumSelineState.SITTING));
            else if (mode == EnumMode.SERVE && state != EnumSelineState.SERVE)
                this.setState(EnumSelineState.toInt(EnumSelineState.SERVE));
            else if (mode == EnumMode.BOSS && (state == EnumSelineState.SITTING || state == EnumSelineState.SERVE))
                this.setState(EnumSelineState.toInt(EnumSelineState.STANDARD));
            else if ((mode == EnumMode.FIGHT || mode == EnumMode.BOSS) && !this.isPerformAttack() && EnumSelineState.valueOf(this.getState()) != EnumSelineState.STANDARD)
                this.setState(EnumSelineState.toInt(EnumSelineState.STANDARD));
        }
    }

    public void getEquipment(ItemEquipment equipment){

        super.getEquipment(equipment);

        if (equipment != null) {
            EquipmentAttribute attribute = equipment.getEquipmentAttribute();
            if (attribute.getType() == EquipmentAttribute.EquipmentType.NONE)
                return;
            else if (attribute.getType() == EquipmentAttribute.EquipmentType.WEAPON) {
                Class<? extends Entity> clazz = attribute.getEntityClass();
                try {
                    EntityMaidWeapon weapon = (EntityMaidWeapon) clazz.getConstructor(World.class).newInstance(world);
                    weapon.setMaid(this);
                    weapon.setPosition(posX, posY + height + 1, posZ);
                    this.setWeaponID(weapon.getUniqueID());
                    if (!this.world.isRemote)
                        this.world.spawnEntity(weapon);
                    this.weapon = weapon;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } else if (attribute.getType() == EquipmentAttribute.EquipmentType.ARMOR) {
                this.setMaxHealthbarnum(40);
                if (this.isFirstGetArmor()) {
                    this.setHealthbarnum(40);
                    this.setFirstGetArmor(false);
                }
            }
        }
    }

    public void loseEquipment(ItemEquipment equipment){

        super.loseEquipment(equipment);
        if (equipment != null) {
            if (world.isRemote)
                return;

            if (equipment instanceof ItemWeapon) {
                try {
                    EntityMaidWeapon.getWeaponFromUUID(world, this.getWeaponID()).setDead();
                } catch (Exception e) {
                    ;
                }
                this.setWeaponID(null);
                this.weapon = null;
            } else {
                this.setMaxHealthbarnum(10);
            }
        }
    }

    @Override
    public AxisAlignedBB getUsingArea(ItemStack stack, EntityLivingBase player, AxisAlignedBB bb) {
        AxisAlignedBB area = bb;
        if (stack.getItem() instanceof ItemWhisper)
        {
            return area.grow( 4 + this.getRank() * 4, 2,  4 + this.getRank() * 4);
        }
        return area;
    }


    public void switchMode(){
        this.setMode((this.getMode() + 1) % 3);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if(shouldAvoidDamage((int) amount, source)) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) source.getTrueSource();
            if (entityLivingBase instanceof EntityPlayerMP && isEnemy(entityLivingBase)) {
                try {
                    entityLivingBase.sendMessage(new TextComponentString("检测到高额攻击伤害，尝试清除玩家物品"));
                } catch (Exception e) {
                    ;
                }
                PunishOperationHandler.punishPlayer((EntityPlayerMP) entityLivingBase, 1, null);
                amount = 1;
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean shouldAvoidDamage(int damage, DamageSource source)
    {
        //等级2时候不会受到过高伤害的攻击
        if (MagicEquipmentRegistry.getAttribute(getArmorType()) == NONE)
            return false;
        if (getRank() < 2)
            return false;

        return super.shouldAvoidDamage(damage, source);
    }

    /**
     * Attack the specified entity using a ranged attack.
     *
     * @param target
     * @param distanceFactor
     */
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        setSwingingArms(true);
        target.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackDamage(NONE));
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        if (swingingArms){
            this.setState(EnumSelineState.toInt(EnumSelineState.STIMULATE));
        }
        else
            this.setState(EnumSelineState.toInt(EnumSelineState.SERVE));
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        if (getTrueHealth() > 0) return null;
        return LootTableHandler.HOLY_FRUIT_SELINA;
    }
}
