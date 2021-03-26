package com.xwm.magicmaid.entity.mob.basic;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAttackableCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAvoidThingCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityMultiHealthCreature;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.manager.IMagicCreatureManager;
import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.manager.MagicCreatureUtils;
import com.xwm.magicmaid.manager.MagicDamageSource;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 *  note 7.0: 多段血条会导致分裂问题，具体原因不明，暂时没法解决，弃用
 *  note 7.5: 尝试解决多段血条问题，重新使用多段血条
 */
public abstract class AbstractEntityMagicCreature extends EntityCreature implements IEntityMultiHealthCreature, IEntityAttackableCreature, IEntityAvoidThingCreature
{
    protected IMagicCreatureManager magicFightManager = IMagicCreatureManagerImpl.getInstance();

    /**
     * multi-health creature
     */
    protected static int max_health_bar_num = 10;
    private static final DataParameter<Integer> MAX_HEALTH_BAR_NUM = EntityDataManager.<Integer>createKey(AbstractEntityMagicCreature.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> HEALTH_BAR_NUM = EntityDataManager.<Integer>createKey(AbstractEntityMagicCreature.class, DataSerializers.VARINT);


    /**
     * avoid thing creature
     */
    protected static int max_set_health = 50;
    protected static int max_damage_health = 50;
    private static final DataParameter<Integer> MAX_SET_HEALTH = EntityDataManager.<Integer>createKey(AbstractEntityMagicCreature.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MAX_DAMAGE_HEALTH = EntityDataManager.<Integer>createKey(AbstractEntityMagicCreature.class, DataSerializers.VARINT);

    /**
     * attackable creature
     */
    private Boolean isPerformAttack = false;

    public AbstractEntityMagicCreature(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(MAX_HEALTH_BAR_NUM, 10);
        this.dataManager.register(HEALTH_BAR_NUM, 10);
        this.dataManager.register(MAX_SET_HEALTH, 50);
        this.dataManager.register(MAX_DAMAGE_HEALTH, 50);
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));

//        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    @Override
    protected void applyEntityAttributes()
    {
        // minecraft
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25000000298023224D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20);

        MagicCreatureUtils.setCreatureMaxHealth(this, 20);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("health_bar_num", this.getHealthBarNum());
        compound.setInteger("avoid_set_num", this.getAvoidSetHealth());
        compound.setInteger("avoid_damage_num", this.getAvoidDamage());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setHealthbarnum(compound.getInteger("health_bar_num"));
        this.setAvoidSetHealth(compound.getInteger("avoid_set_num"));
        this.setAvoidDamage(compound.getInteger("avoid_damage_num"));
    }

    @Override
    public abstract int getAttackDamage(EnumAttackType type);

    @Override
    public abstract int getAttackColdTime(EnumAttackType type);

    @Override
    public boolean isEnemy(EntityLivingBase entityLivingBase) {
        if (entityLivingBase == null)
            return false;
        if (this == entityLivingBase)
            return false;
        if (entityLivingBase instanceof EntityMaidWeapon)
            return false;

        return true;
    }

    @Override
    public boolean shouldAvoidSetHealth(int healthnum) {
        int i = this.dataManager.get(MAX_SET_HEALTH);
        return i != -1 && i < healthnum;
    }

    @Override
    public boolean shouldAvoidDamage(int damage, DamageSource source) {
        int i = this.dataManager.get(MAX_DAMAGE_HEALTH);
        return i != -1 && i < damage;
    }

    @Override
    public void setAvoidSetHealth(int avoid) {
        this.dataManager.set(MAX_SET_HEALTH, avoid);
    }

    @Override
    public void setAvoidDamage(int avoid) {
        this.dataManager.set(MAX_DAMAGE_HEALTH, avoid);
    }

    @Override
    public int getAvoidSetHealth() {
        return this.dataManager.get(MAX_SET_HEALTH);
    }

    @Override
    public int getAvoidDamage() {
        return this.dataManager.get(MAX_DAMAGE_HEALTH);
    }

    /**
     * 直接设置血量
     * @param health
     */
    @Override
    public void setHealth(float health)
    {
        float curHealth = getHealth();
        float minus = curHealth - health;

        if (minus < 0)
            super.setHealth(health);
        else if (!shouldAvoidSetHealth((int) minus))
            super.setHealth(health);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (world.isRemote)
            return false;

        if (source == MagicDamageSource.DEATH_IMMEDIATELY) {
            magicFightManager.setDead(this);
            return true;
        }
        else if (source == MagicDamageSource.IGNORE_REDUCTION)
        {
            return super.attackEntityFrom(source, amount);
        }
        else {
            if (source.getImmediateSource() != null && source.getImmediateSource() instanceof EntityLivingBase)
                amount = magicFightManager.caculateDamageAmount((EntityLivingBase) source.getImmediateSource(), this, null, amount);
            else if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityLivingBase)
                amount = magicFightManager.caculateDamageAmount((EntityLivingBase) source.getTrueSource(), this, null, amount);

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getHealthBarNum() > this.getMaxHealthBarnum())
            this.setHealthbarnum(this.getMaxHealthBarnum());
    }

    /**
     * 恢复生命
     * @param healAmount
     */
    @Override
    public void heal(float healAmount){
        if (healAmount < 0)
            return;
        float t = healAmount + getHealth();
        int barHeal = (int)(t / getMaxHealth());
        float heal = t - barHeal * getMaxHealth();
        setHealthbarnum(Math.min(getHealthBarNum()+barHeal, getMaxHealthBarnum()));
        super.heal(heal);
    }

    /**
     * 死亡的回收步骤
     */
    @Override
    public void onDeathUpdate() {
        if (getTrueHealth() > 0) { // 血量大于0时
            if (this.getHealthBarNum() > 0) { //如果血条没掉完 是不会死的
                this.setHealthbarnum(this.getHealthBarNum() - 1);
                this.setHealth(this.getMaxHealth());
            }
            this.deathTime = 0;
        }
        else{
            super.onDeathUpdate();
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (getTrueHealth() > 0) { // 血量大于0时
            if (this.getHealthBarNum() > 0) { //如果血条没掉完 是不会死的
                this.setHealthbarnum(this.getHealthBarNum() - 1);
                this.setHealth(this.getMaxHealth());
            }
            this.deathTime = 0;
        }
        else{
            super.onDeath(cause);
        }
    }


    @Override
    public void setNoAI(boolean disabled)
    {
        return; //不允许暂停ai
    }


    public void setItNoAI(boolean disabled){
        super.setNoAI(disabled);
    }

    @Override
    public void setMaxHealthbarnum(int maxhealthbarnum) {
        this.dataManager.set(MAX_HEALTH_BAR_NUM, maxhealthbarnum);
    }

    @Override
    public int getMaxHealthBarnum() {
        return dataManager.get(MAX_HEALTH_BAR_NUM);
    }

    @Override
    public void setHealthbarnum(int healthbarnum) {
        this.dataManager.set(HEALTH_BAR_NUM, healthbarnum);
    }

    @Override
    public int getHealthBarNum() {
        return this.dataManager.get(HEALTH_BAR_NUM);
    }

    @Override
    public float getTrueHealth() {
        return getMaxHealth() * getHealthBarNum() + Math.max(getHealth(), 0);
    }

    @Override
    public float getTrueMaxHealth() {
        return getMaxHealthBarnum() * getMaxHealth();
    }

    @Override
    public void killSelf() {
        setAvoidDamage(-1);
        setAvoidSetHealth(-1);
        setHealthbarnum(0);
        setHealth(0);
    }

    @Override
    public void setDead(){
        if (world.isRemote || getHealth() <= 0) { //血条没掉完不允许被杀死 所以指令应该没用
            super.setDead();
        }
    }


    /**
     * 普通攻击
     * @param entityIn
     * @return
     */
    public boolean attackEntityAsMob(Entity entityIn)
    {
        super.attackEntityAsMob(entityIn);
        try{
            entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackDamage(EnumAttackType.NORMAL));
        } catch (Exception e){
            return false;
        }
        return true;
    }


    public void setIsPerformAttack(boolean isPerformAttack){
        this.isPerformAttack = isPerformAttack;
    }

    public boolean isPerformAttack(){
        return isPerformAttack;
    }
}
