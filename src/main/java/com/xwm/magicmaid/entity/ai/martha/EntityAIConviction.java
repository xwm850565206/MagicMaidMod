package com.xwm.magicmaid.entity.ai.martha;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.network.CustomerParticlePacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//todo 只完成了最基本的逻辑 提供测试用
public class EntityAIConviction extends EntityAIBase
{
    private static final int PERFORMTIME = 20;
    private EntityMagicMaid maid;
    private EntityLivingBase owner;
    private int tick = 0;
    private int performTick = 0;
    private double radius = 10;
    private Random random = new Random();

    public EntityAIConviction(EntityMagicMaid maid){
        this.maid = maid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

//        System.out.println("ower: " + maid.hasOwner() + " weaponType: "
//                + maid.getWeaponType() + " mode: " + EnumMode.valueOf(maid.getMode()) + " state: " + maid.getState());

        if (!maid.hasOwner() && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;
        if (EnumEquipment.valueOf(maid.getWeaponType()) != EnumEquipment.CONVICTION)
            return false;
        if (EnumMode.valueOf(maid.getMode()) != EnumMode.FIGHT && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;

        System.out.println("tick: " + tick);
        return tick++ >= this.maid.getAttackColdTime(EnumAttackType.CONVICTION);
    }

    public boolean shouldContinueExecuting(){
        return this.performTick < PERFORMTIME;
    }

    public void startExecuting()
    {
        System.out.println("start");
        this.owner = this.maid.getEntityWorld().getPlayerEntityByUUID(this.maid.getOwnerID());
        this.maid.setState(3);
        this.tick = 0;
    }


    public void updateTask()
    {
        if (performTick++ < PERFORMTIME-1)
            return;

        playParticle(this.maid.getEntityBoundingBox());
        List<EntityLiving> entityLivings = this.maid.world.getEntitiesWithinAABB(EntityLiving.class,
                this.maid.getEntityBoundingBox().grow(radius, 0, radius).expand(0, 4, 0));

        for (EntityLiving entityLiving : entityLivings)
        {
            try {
                if (!maid.isEnemy(entityLiving))
                    continue;

                if (maid.getRank() >= 1)
                    entityLiving.setHealth(1);

                removeTasks(entityLiving);
                playTipParticle(entityLiving.getEntityBoundingBox());

            } catch (Exception e){
                ;
            }
        }


    }

    public void resetTask(){
        this.maid.setState(0);
        this.performTick = 0;
    }

    private void playTipParticle(AxisAlignedBB bb)
    {
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.maxY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        for (int i = 0; i < 4; i++) {
            CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                    d0 + random.nextDouble(),
                    d1 + random.nextDouble(),
                    d2 + random.nextDouble(), EnumCustomParticles.CONVICTION);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);
        }
    }

    private void playParticle(AxisAlignedBB bb) {
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.minY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        double perAngle = 360 / 10.0;
        double perHeight = (bb.maxY - bb.minY) / 6.0;
        double perRadius = radius / 10.0;
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 10; j++)
            {
                for (int k = 0; k < 20; k++) {
                    CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                            d0 + perRadius * k * Math.sin(Math.toRadians(j * perAngle)),
                            d1 + perHeight * i,
                            d2 + perRadius * k * Math.cos(Math.toRadians(j * perAngle)), EnumCustomParticles.CROSS);
                    NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
                    NetworkLoader.instance.sendToAllAround(particlePacket, target);
                }
            }
    }

    private void removeTasks(EntityLiving entityLiving)
    {
        List<EntityAITasks.EntityAITaskEntry> taskEntryList = new ArrayList<EntityAITasks.EntityAITaskEntry>(entityLiving.targetTasks.taskEntries);
        for (EntityAITasks.EntityAITaskEntry ai : taskEntryList)
        {
            entityLiving.targetTasks.removeTask(ai.action);
        }

        taskEntryList = new ArrayList<EntityAITasks.EntityAITaskEntry>(entityLiving.tasks.taskEntries);
        for (EntityAITasks.EntityAITaskEntry ai : taskEntryList)
        {
            entityLiving.tasks.removeTask(ai.action);
        }
    }
}
