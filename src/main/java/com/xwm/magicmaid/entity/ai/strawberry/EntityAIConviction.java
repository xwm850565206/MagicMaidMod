package com.xwm.magicmaid.entity.ai.strawberry;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EnumModes;
import com.xwm.magicmaid.entity.mob.weapon.EnumEquipments;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.ParticlePacket;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.Iterator;
import java.util.List;


//todo 只完成了最基本的逻辑 提供测试用
public class EntityAIConviction extends EntityAIBase
{
    private static final int COLDTIME = 100;
    private static final int PERFORMTIME = 20;
    private EntityMagicMaid maid;
    private EntityLivingBase owner;
    private int tick = 0;
    private int performTick = 0;
    private double radius = 5;

    public EntityAIConviction(EntityMagicMaid maid){
        this.maid = maid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

        System.out.println("ower: " + maid.hasOwner() + " weaponType: "
                + maid.getWeaponType() + " mode: " + EnumModes.valueOf(maid.getMode()));

        if (!maid.hasOwner() && EnumModes.valueOf(maid.getMode()) != EnumModes.BOSS)
            return false;
        if (EnumEquipments.valueOf(maid.getWeaponType()) != EnumEquipments.CONVICTION)
            return false;
        if (EnumModes.valueOf(maid.getMode()) != EnumModes.FIGHT)
            return false;
        System.out.println("tick: " + tick);
        return tick++ >= COLDTIME;
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
                if (entityLiving.equals(maid) || (owner != null && entityLiving.equals(owner)))
                    continue;

                entityLiving.setHealth(1);
                removeTasks(entityLiving);

            } catch (Exception e){
                ;
            }
        }


    }

    public void resetTask(){
        this.maid.setState(0);
        this.performTick = 0;
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
                for (int k = 0; k < 10; k++) {
                    ParticlePacket particlePacket = new ParticlePacket(
                            d0 + perRadius * k * Math.sin(Math.toRadians(j * perAngle)),
                            d1 + perHeight * i,
                            d2 + perRadius * k * Math.cos(Math.toRadians(j * perAngle)), EnumParticleTypes.REDSTONE);
                    NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
                    NetworkLoader.instance.sendToAllAround(particlePacket, target);
                }
            }
    }

    private void removeTasks(EntityLiving entityLiving)
    {
        Iterator iterator = entityLiving.tasks.taskEntries.iterator();
        while (iterator.hasNext())
            iterator.remove();
        iterator = entityLiving.targetTasks.taskEntries.iterator();
        while (iterator.hasNext())
            iterator.remove();
    }
}
