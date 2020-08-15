package com.xwm.magicmaid.entity.ai;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class EntityAINearestAttackableTargetAvoidOwner extends EntityAINearestAttackableTarget
{
    private EntityMagicMaid maid;

    public EntityAINearestAttackableTargetAvoidOwner(EntityMagicMaid maid, Class classTarget, boolean checkSight) {
        super(maid, classTarget, checkSight);
        this.maid = maid;
    }

    public boolean shouldExecute()
    {
        EnumMode mode  = EnumMode.valueOf(maid.getMode());
        if ((mode != EnumMode.FIGHT && mode != EnumMode.BOSS) )
            return false;

        boolean flag = super.shouldExecute();
        if (flag){
            return maid.isEnemy(this.targetEntity);
        }
        return false;
    }
}
