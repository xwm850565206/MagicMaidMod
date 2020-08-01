package com.xwm.magicmaid.entity.ai;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EnumModes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityAIMaidOwerHurtTarget extends EntityAITarget
{
    EntityMagicMaid tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAIMaidOwerHurtTarget(EntityMagicMaid theEntityTameableIn)
    {
        super(theEntityTameableIn, false);
        this.tameable = theEntityTameableIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.tameable.hasOwner())
        {
            return false;
        }
        if (EnumModes.valueOf(this.tameable.getMode()) !=  EnumModes.FIGHT)
            return false;

        else
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase) FMLCommonHandler.instance()
                    .getMinecraftServerInstance().getEntityFromUuid(this.tameable.getOwnerID());

            if (entityLivingBase == null)
            {
                return false;
            }
            else
            {
                this.attacker = entityLivingBase.getLastAttackedEntity();
                int i = entityLivingBase.getLastAttackedEntityTime();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, false);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entityLivingBase = (EntityLivingBase) FMLCommonHandler.instance()
                .getMinecraftServerInstance().getEntityFromUuid(this.tameable.getOwnerID());

        if (entityLivingBase != null)
        {
            this.timestamp = entityLivingBase.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}
