package com.xwm.magicmaid.entity.ai;

import com.xwm.magicmaid.entity.mob.basic.AbstractEntityMagicCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityMultiModeCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityTameableCreature;
import com.xwm.magicmaid.enumstorage.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityAIMagicCreatureOwerHurtTarget extends EntityAITarget
{
    AbstractEntityMagicCreature tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAIMagicCreatureOwerHurtTarget(AbstractEntityMagicCreature theEntityTameableIn)
    {
        super(theEntityTameableIn, false);
        this.tameable = theEntityTameableIn;
        this.setMutexBits(1);

        if (!(theEntityTameableIn instanceof IEntityTameableCreature))
            throw new IllegalArgumentException("need IEntityTameableCreature");
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!((IEntityTameableCreature)this.tameable).hasOwner())
        {
            return false;
        }
        if (this.tameable instanceof IEntityMultiModeCreature && EnumMode.valueOf(((IEntityMultiModeCreature) this.tameable).getMode()) !=  EnumMode.FIGHT)
            return false;
        else
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase) FMLCommonHandler.instance()
                    .getMinecraftServerInstance().getEntityFromUuid(((IEntityTameableCreature)this.tameable).getOwnerID());

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
                .getMinecraftServerInstance().getEntityFromUuid(((IEntityTameableCreature)this.tameable).getOwnerID());

        if (entityLivingBase != null)
        {
            this.timestamp = entityLivingBase.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}
