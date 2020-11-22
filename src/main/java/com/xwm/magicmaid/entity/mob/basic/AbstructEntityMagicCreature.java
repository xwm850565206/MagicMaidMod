package com.xwm.magicmaid.entity.mob.basic;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAttackableCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAvoidThingCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityMultiHealthCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class AbstructEntityMagicCreature extends EntityCreature implements IEntityMultiHealthCreature, IEntityAttackableCreature, IEntityAvoidThingCreature
{
    /**
     * multi-health creature
     */
    protected static int max_health_bar_num = 10;
    private static final DataParameter<Integer> MAX_HEALTH_BAR_NUM = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> HEALTH_BAR_NUM = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);

    /**
     * avoid thing creature
     */
    protected static int max_set_health = 50;
    protected static int max_damage_health = 50;
    private static final DataParameter<Integer> MAX_SET_HEALTH = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MAX_DAMAGE_HEALTH = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);

    /**
     * attackable creature
     */
    private Boolean isPerformAttack = false;

    public AbstructEntityMagicCreature(World worldIn) {
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

        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25000000298023224D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("healthBarNum", this.getHealthBarNum());
        compound.setInteger("max_health_bar_num", this.getMaxHealthBarnum());
        compound.setInteger("avoid_set_num", this.getAvoidSetHealth());
        compound.setInteger("avoid_damage_num", this.getAvoidDamage());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setHealthbarnum(compound.getInteger("healthBarNum"));
        this.setMaxHealthbarnum(compound.getInteger("max_health_bar_num"));
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
     * 设置血条最大值
     *
     * @param maxhealthbarnum
     */
    @Override
    public void setMaxHealthbarnum(int maxhealthbarnum){
         this.dataManager.set(MAX_HEALTH_BAR_NUM, maxhealthbarnum);
    }


    /**
     * 得到血条最大值
     *
     * @return
     */
    @Override
    public int getMaxHealthBarnum() {
        return dataManager.get(MAX_HEALTH_BAR_NUM);
    }

    /**
     * 设置当前血条数
     *
     * @param healthbarnum
     */
    @Override
    public void setHealthbarnum(int healthbarnum) {
        this.dataManager.set(HEALTH_BAR_NUM, healthbarnum);
    }

    /**
     * 得到当前血条数
     *
     * @return
     */
    @Override
    public int getHealthBarNum() {
        return this.dataManager.get(HEALTH_BAR_NUM);
    }

    /**
     * 得到当前真正血量
     * @return
     */
    @Override
    public float getTrueHealth(){
        return getMaxHealth() * getHealthBarNum() + Math.max(getHealth(), 0);
    }

    /**
     * 得到当前最大血量
     * @return
     */
    @Override
    public float getTrueMaxHealth() {
        return getMaxHealthBarnum() * getMaxHealth();
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
        if (this.getHealthBarNum() > 0){ //如果血条没掉完 是不会死的
            this.setHealthbarnum(this.getHealthBarNum()-1);
            this.setHealth(this.getMaxHealth());
            this.deathTime = 0;
        }
        else{
            super.onDeathUpdate();
        }
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        if (this.getHealthBarNum() > 0){ //如果血条没掉完 是不会死的
            this.setHealthbarnum(this.getHealthBarNum()-1);
            this.setHealth(this.getMaxHealth());
        }
        else{
            super.onDeath(cause);
        }
    }

    @Override
    public void setDead(){
        if (getTrueHealth() <= 0) { //血条没掉完不允许被杀死 所以指令应该没用
            super.setDead();
        }
    }

    @Override
    public boolean isEntityAlive(){
        return this.getTrueHealth() > 0;
    }


    /**
     * 直接死亡
     */
    @Override
    public void killSelf() {
        setAvoidDamage(-1);
        setAvoidSetHealth(-1);
        setHealthbarnum(0);
        setHealth(0);
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
