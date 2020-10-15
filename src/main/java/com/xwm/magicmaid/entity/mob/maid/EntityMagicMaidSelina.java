package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.EntityAIMaidAttackRanged;
import com.xwm.magicmaid.entity.ai.selina.EntityAIPandora;
import com.xwm.magicmaid.entity.ai.selina.EntityAISelinaServe;
import com.xwm.magicmaid.entity.ai.selina.EntityAIWhisper;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import com.xwm.magicmaid.util.handlers.PunishOperationHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

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

        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, true));


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
    public int getAttackDamage(EnumAttackType type){

        switch(type){
            case NORMAL: return 10;
            case PANDORA: return 1 + getRank();
            case WHISPER: return 10 + 10 * getRank();
            default: return super.getAttackDamage(type);
        }
    }

    @Override
    public int getAttackColdTime(EnumAttackType type){
        switch(type){
            case NORMAL: return 20;
            case PANDORA: return 60 - 5 * this.getRank();
            case WHISPER: return 100 - 10 * this.getRank();
            default: return super.getAttackColdTime(type);
        }
    }
    @Override
    public void onUpdate() {

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

        super.onUpdate();
    }

    public void getEquipment(ItemEquipment equipment){
        if (this.world.isRemote)
            return;

        EnumEquipment equipment1 = equipment.enumEquipment;
        switch (equipment1){
            case PANDORA:
                EntityMaidWeaponPandorasBox pandorasBox = new EntityMaidWeaponPandorasBox(world);
                pandorasBox.setMaid(this);
                pandorasBox.setPosition(posX, posY + height + 1, posZ);
                world.spawnEntity(pandorasBox);
                this.setWeaponID(pandorasBox.getUniqueID());
                this.setWeaponType(EnumEquipment.toInt(equipment1));
                this.setHasWeapon(true);
                this.weapon = pandorasBox;
                break;
            case WHISPER:
                EntityMaidWeaponWhisper whisper = new EntityMaidWeaponWhisper(world);
                whisper.setMaid(this);
                whisper.setPosition(posX, posY + height + 1, posZ);
                world.spawnEntity(whisper);
                this.setWeaponID(whisper.getUniqueID());
                this.setWeaponType(EnumEquipment.toInt(equipment1));
                this.setHasWeapon(true);
                this.weapon = whisper;
                break;
            case WISE:
                this.setHasArmor(true);
                this.setMaxHealthbarnum(200);
                this.setHealthbarnum(200);
                break;
        }


    }

    public void loseEquipment(ItemEquipment equipment){
        if (equipment instanceof  ItemWeapon){
            try {
                EntityMaidWeapon.getWeaponFromUUID(world, this.getWeaponID()).setDead();
            }catch (Exception e){
                ;
            }
            this.setWeaponType(EnumEquipment.toInt(EnumEquipment.NONE));
            this.setWeaponID(null);
            this.setHasWeapon(false);
            this.weapon = null;
        }
        else {
            this.setHasArmor(false);
            this.setMaxHealthbarnum(10);
        }
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
        if (!hasArmor())
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
        target.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackDamage(EnumAttackType.NORMAL));
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        if (swingingArms){
            this.setState(EnumSelineState.toInt(EnumSelineState.STIMULATE));
        }
        else
            this.setState(EnumSelineState.toInt(EnumSelineState.SERVE));
    }
}
