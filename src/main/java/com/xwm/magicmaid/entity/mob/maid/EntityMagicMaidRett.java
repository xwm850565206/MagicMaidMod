package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.EntityAIMaidAttackMelee;
import com.xwm.magicmaid.entity.ai.EntityAINearestAttackableTargetAvoidOwner;
import com.xwm.magicmaid.entity.ai.rett.EntityAIDemonKillerAttack;
import com.xwm.magicmaid.entity.ai.rett.EntityAIRettServe;
import com.xwm.magicmaid.entity.ai.rett.EntityAITeleportAttack;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import com.xwm.magicmaid.util.handlers.PunishOperationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
    public int getAttackDamage(EnumAttackType type){

        return 50 + 50 * getRank();
    }

    public void onUpdate()
    {
        if (!world.isRemote){
            EnumRettState state = EnumRettState.valueOf(this.getState());
            EnumMode mode = EnumMode.valueOf(this.getMode());
            EnumEquipment equipment = EnumEquipment.valueOf(this.getWeaponType());
            if (equipment == EnumEquipment.DEMONKILLINGSWORD
                    && (mode == EnumMode.FIGHT || mode == EnumMode.BOSS) && !isAttackState()){
                this.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_STANDARD)); // todo 有两把武器不能这么判断
            }
            else if (equipment == EnumEquipment.NONE)
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
        if (this.world.isRemote)
            return;
        EnumEquipment equipment1 = equipment.enumEquipment;
        switch (equipment1){
            case DEMONKILLINGSWORD:
                this.setWeaponType(EnumEquipment.toInt(equipment.enumEquipment));
                this.setHasWeapon(true);
                break;
            case IMMORTAL:
                this.setHasArmor(true);
                this.setMaxHealthbarnum(1000);
                this.setHealthbarnum(1000);
                break;

        }

    }

    public void loseEquipment(ItemEquipment equipment){
        if (this.world.isRemote)
            return;
        EnumEquipment equipment1 = equipment.enumEquipment;
        switch (equipment1) {
            case DEMONKILLINGSWORD:
                this.setWeaponType(EnumEquipment.toInt(EnumEquipment.NONE));
                this.setHasWeapon(false);
                break;
            case IMMORTAL:
                this.setHasArmor(false);
                this.setMaxHealthbarnum(20);
                break;
        }
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

        if (source.damageType.equals("killed_rett")) {
            try{
                return this.killItself((EntityPlayer) source.getTrueSource());
            } catch (Exception e){
                return false;
            }
        }

        if (source.damageType.equals("drown") || source.damageType.equals("fall"))
            return false;

        if (getRank() >= 1 && hasArmor()) {
            if (EnumMode.valueOf(getMode()) != EnumMode.BOSS)
                return false;
            else super.attackEntityFrom(source, amount / 10);
        }
        if (this.getRank() >= 2 && hasArmor()) { //等级2时候不会受到过高伤害的攻击 这里还不严谨 很容易绕过
            if (amount > 50) {
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
        }
        return super.attackEntityFrom(source, amount);
    }

    public boolean attackEntityAsMob(Entity entityIn){
        this.setLastAttackedEntity(entityIn);
        return false;
    }

}
