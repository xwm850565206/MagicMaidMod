package com.xwm.magicmaid.entity.ai;

import com.xwm.magicmaid.entity.mob.basic.AbstructEntityMagicCreature;
import com.xwm.magicmaid.entity.mob.basic.EntityTameableCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityMultiModeCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityTameableCreature;
import com.xwm.magicmaid.enumstorage.EnumMode;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityAIMagicCreatureOwnerHurtByTarget extends EntityAITarget
{
    AbstructEntityMagicCreature tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAIMagicCreatureOwnerHurtByTarget(AbstructEntityMagicCreature theDefendingTameableIn)
    {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);

        if (!(theDefendingTameableIn instanceof IEntityTameableCreature))
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
                this.attacker = entityLivingBase.getRevengeTarget();
                int i = entityLivingBase.getRevengeTimer();
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
            this.timestamp = entityLivingBase.getRevengeTimer();
        }

        super.startExecuting();
    }
}
