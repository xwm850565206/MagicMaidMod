package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.EntityAIMaidAttackRanged;
import com.xwm.magicmaid.entity.ai.martha.EntityAIConviction;
import com.xwm.magicmaid.entity.ai.martha.EntityAIMarthaServe;
import com.xwm.magicmaid.entity.ai.martha.EntityAIRepantence;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.object.item.equipment.*;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import com.xwm.magicmaid.util.handlers.LootTableHandler;
import com.xwm.magicmaid.util.handlers.PunishOperationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

import static com.xwm.magicmaid.registry.MagicEquipmentRegistry.*;

public class EntityMagicMaidMartha extends EntityMagicMaid implements IRangedAttackMob
{
    //state: 0-草莓标准站立 1-草莓服侍站立 2-草莓待命站立 3-草莓攻击

    public EntityMagicMaidMartha(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit(){
        super.entityInit();
    }

    public void initEntityAI(){
        super.initEntityAI();

        this.tasks.addTask(2, new EntityAIMaidAttackRanged(this, 1.3D, 40, 10));
        this.targetTasks.addTask(3, new EntityAIMarthaServe(this));
        this.targetTasks.addTask(3, new EntityAIRepantence(this));
        this.targetTasks.addTask(3, new EntityAIConviction(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
    }

    @Override
    public int getAttackDamage(EquipmentAttribute type){

        if (NONE.equals(type)) {
            return 5 + 5 * this.getRank();
        } else if (REPANTENCE.equals(type)) {
            return this.getRank() > 0 ? 10 : 15;
        } else if (CONVICTION.equals(type)) {
            return this.getRank() > 0 ? 0 : 1;
        }
        return super.getAttackDamage(type);
    }

    @Override
    public int getAttackColdTime(EquipmentAttribute type){
        if (NONE.equals(type)) {
            return 20;
        } else if (REPANTENCE.equals(type)) {
            return 100 - 30 * this.getRank();
        } else if (CONVICTION.equals(type)) {
            return 100 - 10 * this.getRank();
        }
        return super.getAttackColdTime(type);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!world.isRemote){
            EnumMode mode = EnumMode.valueOf(this.getMode());
            if (mode == EnumMode.SERVE && this.getState() != 1)
                this.setState(1);
            else if (mode == EnumMode.SITTING && this.getState() != 2)
                this.setState(2);
            else if ((mode == EnumMode.FIGHT || mode == EnumMode.BOSS) && !this.isPerformAttack() && this.getState() != 0)
                this.setState(0);
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

    public void loseEquipment(ItemEquipment equipment) {

        super.loseEquipment(equipment);
        if (equipment != null) {

            if (world.isRemote)
                return;

            if (equipment instanceof ItemWeapon) {
                try {
                    EntityMaidWeapon weapon1 = EntityMaidWeapon.getWeaponFromUUID(world, getWeaponID());
                    weapon1.setDead();
                    setWeaponID(null);
                    this.weapon = null;
                } catch (Exception e) {
                    ; //可能武器被其他模组杀死了
                }
            } else {
                this.setMaxHealthbarnum(10);
            }
        }
    }

    @Override
    public AxisAlignedBB getUsingArea(ItemStack stack, EntityLivingBase player, AxisAlignedBB bb) {
        AxisAlignedBB area = bb;
        if (stack.getItem() instanceof ItemConviction)
        {
            area = this.getEntityBoundingBox().grow(4 + 3 * getRank(), 0, 4 + 3 * getRank()).expand(0, 2, 0);
        }
        else if (stack.getItem() instanceof ItemRepantence)
        {
            area = this.getEntityBoundingBox().grow(6 + 2 * getRank(), 1, 6 + 2 * getRank());
        }

        return area;
    }

    public void switchMode(){
        this.setMode((this.getMode() + 1) % 3);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.getRank() >= 1 && MagicEquipmentRegistry.getAttribute(getArmorType()) != NONE) //回敬伤害 要注意有可能产生死循环，所以这里要判断
        {
            try{
                if (!(source.getTrueSource() instanceof EntityMagicMaid)
                        && this.isEnemy((EntityLivingBase) source.getTrueSource()))
                    source.getTrueSource().attackEntityFrom(source, amount);

                if (!(source.getImmediateSource() instanceof EntityMagicMaid)
                        && this.isEnemy((EntityLivingBase) source.getImmediateSource()))
                    source.getImmediateSource().attackEntityFrom(source, amount);
            }catch (Exception e){
                ;//有可能会有空指针
            }
        }

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
        if (swingingArms)
            this.setState(3);
        else
            this.setState(1);
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        if (getTrueHealth() > 0) return null;
        return LootTableHandler.HOLY_FRUIT_MARTHA;
    }

}
