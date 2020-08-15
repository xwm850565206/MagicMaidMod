package com.xwm.magicmaid.entity.ai;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRanged;

public class EntityAIMaidAttackRanged extends EntityAIAttackRanged
{
    private IRangedAttackMob taskOwner;
    public EntityAIMaidAttackRanged(IRangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, maxAttackTime, maxAttackDistanceIn);
        taskOwner = attacker;
    }

    public boolean shouldExecute()
    {
        EntityMagicMaid maid = (EntityMagicMaid) taskOwner;
        EnumMode mode  = EnumMode.valueOf(maid.getMode());
        if ((mode != EnumMode.FIGHT && mode != EnumMode.BOSS) || maid.isPerformAttack())
            return false;
        else
            return super.shouldExecute();
    }

    public void updateTask()
    {
        super.updateTask();
    }
}
