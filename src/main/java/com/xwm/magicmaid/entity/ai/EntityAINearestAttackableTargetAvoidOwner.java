package com.xwm.magicmaid.entity.ai;

import com.google.common.base.Predicate;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import javax.annotation.Nullable;

public class EntityAINearestAttackableTargetAvoidOwner extends EntityAINearestAttackableTarget
{
    private EntityMagicMaid maid;

    public EntityAINearestAttackableTargetAvoidOwner(EntityMagicMaid maid, Class classTarget, boolean checkSight, @Nullable final Predicate targetSelector) {
        super(maid, classTarget, 10, checkSight, false, targetSelector);
        this.maid = maid;
    }

    public boolean shouldExecute()
    {
        EnumMode mode  = EnumMode.valueOf(maid.getMode());
        if ((mode != EnumMode.FIGHT && mode != EnumMode.BOSS))
            return false;

        boolean flag = super.shouldExecute();
        if (flag && maid.isEnemy(this.targetEntity)){
            return true;
        }
        this.targetEntity = null;
        return false;
    }
}
