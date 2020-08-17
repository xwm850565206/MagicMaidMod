package com.xwm.magicmaid.entity.ai.rett;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRett;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAITeleportAttack extends EntityAIBase
{
    private EntityMagicMaidRett maid;

    public EntityAITeleportAttack(EntityMagicMaidRett maid){
        this.maid = maid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (maid.getAttackTarget() == null)
            return false;
        if (maid.getAttackTarget().getDistanceSq(maid) < 20.0D)
            return false;
        if (maid.getRank() < 2) //满阶解锁瞬移攻击
            return false;
        return true;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return false;
    }

    public void startExecuting()
    {
        BlockPos pos = maid.getAttackTarget().getPosition();
        maid.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), maid.rotationYaw, maid.rotationPitch);
        maid.world.updateEntityWithOptionalForce(maid, true);
    }

}
