package com.xwm.magicmaid.entity.ai;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityAIMaidAttackMelee extends EntityAIAttackMelee
{
    public EntityAIMaidAttackMelee(EntityMagicMaid maid, double speedIn, boolean useLongMemory) {
        super(maid, speedIn, useLongMemory);
    }

    public boolean shouldExecute(){
        EntityMagicMaid maid = (EntityMagicMaid) this.attacker;
        EnumMode mode  = EnumMode.valueOf(maid.getMode());
        if ((mode != EnumMode.FIGHT && mode != EnumMode.BOSS) || maid.isPerformAttack())
            return false;
        else
            return super.shouldExecute();
    }
}
