package com.xwm.magicmaid.entity.ai.rett;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRett;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import com.xwm.magicmaid.network.CustomerParticlePacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.Sys;

import java.util.List;
import java.util.Random;

public class EntityAIDemonKillerAttack extends EntityAIBase
{
    private static final int COLDTIME = 10;
    private static final int PERFORMTIME = 45;
    private EntityMagicMaidRett maid;
    private World world;
    private EntityLivingBase target;
    private int tick = 0;
    private int performTick = 0;
    private Random random = new Random();

    public EntityAIDemonKillerAttack(EntityMagicMaidRett maid)
    {
        this.maid = maid;
    }

    public  boolean shouldExecute(){
        this.maid.debug();
        System.out.println("tick : " + tick);
        if (!maid.hasOwner() && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS) //如果没有主人又不是boss就不放技能
            return false;
        if (EnumEquipment.valueOf(maid.getWeaponType()) != EnumEquipment.DEMONKILLINGSWORD)
            return false;
        if (EnumMode.valueOf(maid.getMode()) != EnumMode.FIGHT && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;
        if (maid.getAttackTarget() == null)
            return false;
        tick++;
        if (maid.getAttackTarget().getDistance(maid) > 6.0D)
            return false;
        return tick >= COLDTIME;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting(){
        return this.performTick < PERFORMTIME && this.target != null;
    }

    public void startExecuting()
    {
        System.out.println("start");
        this.tick = 0;
        this.world = maid.getEntityWorld();
        this.target = maid.getAttackTarget();
    }

    public void updateTask()
    {
        this.maid.setPerformtick(performTick);
        try{
            if (performTick == 5){
                List<EntityLiving> entityLivingList = world.getEntitiesWithinAABB(EntityLiving.class, target.getEntityBoundingBox().grow(2, 1, 2));
                for (EntityLiving entityLiving : entityLivingList){
                    if (!maid.isEnemy(entityLiving))
                        continue;

                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 1 + maid.getRank()));
                    entityLiving.attackEntityFrom(DamageSource.causeMobDamage(maid), maid.getAttackDamage(EnumAttackType.DEMONKILLER));
                }
            }
            else if (performTick == 10) {
                List<EntityLiving> entityLivingList = world.getEntitiesWithinAABB(EntityLiving.class, target.getEntityBoundingBox().grow(2, 1, 2));
                for (EntityLiving entityLiving : entityLivingList){
                    if (!maid.isEnemy(entityLiving))
                        continue;

                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                    entityLiving.attackEntityFrom(DamageSource.causeMobDamage(maid), maid.getAttackDamage(EnumAttackType.DEMONKILLER));

                }
            }
            else if (performTick >= 20 && performTick < 30) {
                playHoldPatricle(this.maid.getEntityBoundingBox().grow(0, 1, 0), this.maid.getHorizontalFacing());
            }
            else if (performTick == 30) {
                playBombardmentParticle(this.maid.getEntityBoundingBox());
                List<EntityLiving> entityLivingList = world.getEntitiesWithinAABB(EntityLiving.class, maid.getEntityBoundingBox().grow(4, 2, 4));
                for (EntityLiving entityLiving : entityLivingList){
                    if (!maid.isEnemy(entityLiving))
                        continue;
                    entityLiving.attackEntityFrom(DamageSource.causeMobDamage(maid), maid.getAttackDamage(EnumAttackType.DEMONKILLER) * 2);
                    entityLiving.setVelocity(random.nextDouble(), random.nextDouble()*3, random.nextDouble());
                }
            }

        } catch (Exception e){
            ;
        }

        performTick++;
    }

    public void resetTask(){
        this.maid.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_STANDARD));
        this.performTick = 0;
        this.maid.setPerformtick(performTick);
    }

    private void playHoldPatricle(AxisAlignedBB bb, EnumFacing facing)
    {
        double k = 0.5;
        double angle = Math.toRadians(facing.getHorizontalAngle());
        double d0 = (bb.minX + bb.maxX) / 2.0 - Math.sin(angle) * k;
        double d1 = bb.maxY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0 + Math.cos(angle) * k;
        for (int i = 0; i < 2; i++) {
            CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                    d0 + random.nextDouble(),
                    d1 + random.nextDouble(),
                    d2 + random.nextDouble(), EnumCustomParticles.STAR);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);
        }
    }

    private void playBombardmentParticle(AxisAlignedBB bb)
    {
        double radius = 4;
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.minY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        double perAngle = 360 / 10.0;
        double perHeight = 3.0 / 6.0;
        double perRadius = radius / 6.0;
        for (int j = 0; j < 10; j++)
            for (int k = 0; k < 6; k++) {
                for (int i = 0; i < k; i++) {
                    CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                            d0 + perRadius * k * Math.sin(Math.toRadians(j * perAngle)),
                            d1 + perHeight * i,
                            d2 + perRadius * k * Math.cos(Math.toRadians(j * perAngle)), EnumCustomParticles.STAR);
                    NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
                    NetworkLoader.instance.sendToAllAround(particlePacket, target);
                }
            }
    }
}
