package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.EntityAIMaidAttackMelee;
import com.xwm.magicmaid.entity.ai.rett.EntityAIDemonKillerAttack;
import com.xwm.magicmaid.entity.ai.rett.EntityAIRettServe;
import com.xwm.magicmaid.entity.ai.rett.EntityAITeleportAttack;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import com.xwm.magicmaid.object.item.equipment.EquipmentAttribute;
import com.xwm.magicmaid.object.item.equipment.ItemDemonKillerSword;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import com.xwm.magicmaid.util.handlers.LootTableHandler;
import com.xwm.magicmaid.util.handlers.PunishOperationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import static com.xwm.magicmaid.registry.MagicEquipmentRegistry.*;

public class EntityMagicMaidRett extends EntityMagicMaid
{
    private static final DataParameter<Integer> PERFORMTICK = EntityDataManager.<Integer>createKey(EntityMagicMaidRett.class, DataSerializers.VARINT);

    public EntityMagicMaidRett(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit(){
        super.entityInit();
        this.dataManager.register(PERFORMTICK, 0);
    }

    @Override
    public void initEntityAI(){
        super.initEntityAI();

        this.tasks.addTask(2, new EntityAIMaidAttackMelee(this, 1.3D, false));
        this.tasks.addTask(4, new EntityAIRettServe(this));
        this.tasks.addTask(4, new EntityAITeleportAttack(this));
        this.targetTasks.addTask(3, new EntityAIDemonKillerAttack(this));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.setMaxHealthbarnum(20);
        this.setHealthbarnum(20);
    }


    @Override
    public int getAttackDamage(EquipmentAttribute type){

        return 5 + 5 * getRank();
    }

    public void onUpdate()
    {
        if (!world.isRemote){
            EnumRettState state = EnumRettState.valueOf(this.getState());
            EnumMode mode = EnumMode.valueOf(this.getMode());
            EquipmentAttribute equipment = MagicEquipmentRegistry.getAttribute(this.getWeaponType());
            if (equipment == DEMONKILLINGSWORD
                    && (mode == EnumMode.FIGHT || mode == EnumMode.BOSS) && !isAttackState()){
                this.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_STANDARD));
            }
            else if (equipment == NONE)
            {
                if (mode == EnumMode.SITTING && state != EnumRettState.SITTING)
                    this.setState(EnumRettState.toInt(EnumRettState.SITTING));
                else if (mode == EnumMode.SERVE && state != EnumRettState.SERVE)
                    this.setState(EnumRettState.toInt(EnumRettState.SERVE));
            }
        }
        super.onUpdate();
    }

    public void getEquipment(ItemEquipment equipment){

        EquipmentAttribute equipment1 = equipment.getEquipmentAttribute();
        if (DEMONKILLINGSWORD.equals(equipment1)) {
            this.setWeaponType(equipment1.getName());
            this.setHasWeapon(true);
        } else if (IMMORTAL.equals(equipment1)) {
            this.setHasArmor(true);
            this.setMaxHealthbarnum(200);
            this.setArmorType(equipment1.getName());
            if (this.isFirstGetArmor()) {
                this.setHealthbarnum(200);
                this.setFirstGetArmor(false);
            }
        }

    }

    public void loseEquipment(ItemEquipment equipment){

        if (world.isRemote)
            return;

        EquipmentAttribute equipment1 = equipment.getEquipmentAttribute();
        if (DEMONKILLINGSWORD.equals(equipment1)) {
            this.setWeaponType(NONE.getName());
            this.setHasWeapon(false);
        } else if (IMMORTAL.equals(equipment1)) {
            this.setHasArmor(false);
            this.setArmorType(NONE.getName());
            this.setMaxHealthbarnum(20);
        }
    }

    @Override
    public AxisAlignedBB getUsingArea(ItemStack stack, EntityLivingBase player, AxisAlignedBB bb) {
        AxisAlignedBB area = bb;
        if (stack.getItem() instanceof ItemDemonKillerSword)
            area = bb.grow(2, 1 ,2);
        return area;
    }

    public void switchMode(){
        this.setMode((this.getMode() + 1) % 3);
    }

    private boolean isAttackState(){
        EnumRettState state = EnumRettState.valueOf(this.getState());
        return state == EnumRettState.DEMON_KILLER_ATTACK1
                || state == EnumRettState.DEMON_KILLER_ATTACK2
                || state == EnumRettState.DEMON_KILLER_ATTACK3
                || state == EnumRettState.DEMON_KILLER_ATTACK4;
    }

    public int getPerformTick(){
        return this.dataManager.get(PERFORMTICK);
    }

    public void setPerformtick(int performtick){
        this.dataManager.set(PERFORMTICK, performtick);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if (source.damageType.equals("drown") || source.damageType.equals("fall"))
            return false;

        if (getRank() >= 1 && hasArmor()) {
            if (EnumMode.valueOf(getMode()) != EnumMode.BOSS)
                return false;
            else super.attackEntityFrom(source, amount / 10);
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
        if (!hasArmor())
            return false;
        if (getRank() < 2)
            return false;

        return super.shouldAvoidDamage(damage, source);
    }

    public boolean attackEntityAsMob(Entity entityIn){
        this.setLastAttackedEntity(entityIn);
        return false;
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        if (getHealth() > 0) return null;
        return LootTableHandler.HOLY_FRUIT_RETT;
    }
}
