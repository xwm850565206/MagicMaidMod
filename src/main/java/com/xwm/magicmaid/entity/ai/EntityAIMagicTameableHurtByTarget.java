package com.xwm.magicmaid.entity.ai;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityTameableCreature;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

public class EntityAIMagicTameableHurtByTarget extends EntityAIHurtByTarget {

    private IEntityTameableCreature entityTameableCreature;
    public EntityAIMagicTameableHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>... excludedReinforcementTypes) {
        super(creatureIn, entityCallsForHelpIn, excludedReinforcementTypes);
        this.entityTameableCreature = (IEntityTameableCreature) creatureIn;
    }

    @Override
    public boolean shouldExecute() {
        if (entityTameableCreature.hasOwner() && this.taskOwner.getRevengeTarget() != null && entityTameableCreature.getOwnerID().equals(this.taskOwner.getRevengeTarget().getUniqueID()))
            return false;
        else
            return super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (entityTameableCreature.hasOwner() && this.target != null && entityTameableCreature.getOwnerID().equals(this.target.getUniqueID()))
            return false;
        else
            return super.shouldContinueExecuting();
    }
}
