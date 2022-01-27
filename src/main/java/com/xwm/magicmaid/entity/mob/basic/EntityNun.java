package com.xwm.magicmaid.entity.mob.basic;

import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.particle.SPacketThreeParamParticle;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.util.handlers.LootTableHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityNun extends EntityMob implements IRangedAttackMob
{
    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.<Boolean>createKey(EntityNun.class, DataSerializers.BOOLEAN);


    public EntityNun(World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 1.5F);
        this.experienceValue = 100;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(ARMS_RAISED, Boolean.FALSE);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAINunAttackRanged(this, 1.0, 40, 4));
        this.applyEntityAI();
    }

    protected void applyEntityAI()
    {
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityNun.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityZombie.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEquipmentBasedOnDifficulty(difficulty);
        return livingdata;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemInit.ITEM_REPANTENCE));
    }

    @SideOnly(Side.CLIENT)
    public boolean isArmsRaised()
    {
        return this.getDataManager().get(ARMS_RAISED);
    }


    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {

        if (!target.world.isRemote) {
            float r = target.width * 1.5f / 2.0f;
            AxisAlignedBB bb = target.getEntityBoundingBox();
            double d0 = (bb.minX + bb.maxX) / 2.0;
            double d1 = (bb.minY + bb.maxY) / 2.0;
            double d2 = (bb.minZ + bb.maxZ) / 2.0;

            for (int i = 0; i < 12; i++) {
                SPacketThreeParamParticle packet = new SPacketThreeParamParticle(d0 + r*Math.sin(30 * i), d1, d2 + r*Math.cos(30*i), EnumCustomParticles.SOUL);
                NetworkLoader.instance.sendToAllAround(packet, new NetworkRegistry.TargetPoint(target.dimension, d0, d1, d2, 30));
            }

            target.attackEntityFrom(DamageSource.causeMobDamage(this).setMagicDamage(), (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        this.getDataManager().set(ARMS_RAISED, swingingArms);
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        return LootTableHandler.NUN;
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier){
        // avoid drop repantence
    }
}
