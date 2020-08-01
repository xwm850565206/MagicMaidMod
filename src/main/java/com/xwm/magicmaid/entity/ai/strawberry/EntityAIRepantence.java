package com.xwm.magicmaid.entity.ai.strawberry;

import com.xwm.magicmaid.entity.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.maid.EnumAttackTypes;
import com.xwm.magicmaid.entity.maid.EnumModes;
import com.xwm.magicmaid.event.NetworkLoader;
import com.xwm.magicmaid.util.ParticlePacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

//todo
public class EntityAIRepantence extends EntityAIBase
{
    private static final int COLDTIME = 100;
    private static final int PERFORMTIME = 20;
    private EntityMagicMaid maid;
    private EntityLivingBase owner;
    private int tick = 0;
    private int performTick = 0;

    public EntityAIRepantence(EntityMagicMaid maid){
        this.maid = maid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

        System.out.println("ower: " + maid.hasOwner() + " weaponType: "
                + maid.getWeaponType() + " mode: " + EnumModes.valueOf(maid.getMode()));

        if (!maid.hasOwner())
            return false;
//        if (EnumWeapons.valueOf(maid.getWeaponType()) != EnumWeapons.REPATENCE)
//            return false;
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

        World world = this.maid.getEntityWorld();
        AxisAlignedBB bb = this.maid.getEntityBoundingBox().grow(10, 1, 10);
        List<EntityLivingBase> entityLivingBases = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
        System.out.println("entity list size: " + entityLivingBases.size());
        for (EntityLivingBase entityLivingBase : entityLivingBases)
        {
            try{
                if (entityLivingBase.equals(this.maid) || (owner != null && entityLivingBase.equals(owner)))
                    continue;
                entityLivingBase.attackEntityFrom(new EntityDamageSource("repantence_attack", maid),
                        maid.getAttackDamage(EnumAttackTypes.REPANTENCE));
                playParticle(entityLivingBase.getEntityBoundingBox());
                if (entityLivingBase.getHealth() <= 0){
                    playDeathParticle(entityLivingBase.getEntityBoundingBox());
                }
            } catch (Exception e){
                ; //有可能出现问题
            }
        }
    }

    public void resetTask(){
        this.maid.setState(0);
        this.performTick = 0;
    }

    private void playParticle(AxisAlignedBB bb){

        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.minY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        double radius = Math.max((bb.maxX - bb.minX), (bb.maxZ - bb.minZ)) / 2.0 + 0.25;
        double perAngle = 360 / 10.0;
        double perHeight = (bb.maxY - bb.minY) / 6.0;
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 10; j++)
            {
                ParticlePacket particlePacket = new ParticlePacket(
                        d0 + radius * Math.sin(Math.toRadians(j * perAngle)),
                        d1 + perHeight * i,
                        d2 + radius * Math.cos(Math.toRadians(j * perAngle)), EnumParticleTypes.SMOKE_LARGE);
                NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
                NetworkLoader.instance.sendToAllAround(particlePacket, target);
            }
    }

    private void playDeathParticle(AxisAlignedBB bb){
        //todo 灵魂抽离效果
    }
}
