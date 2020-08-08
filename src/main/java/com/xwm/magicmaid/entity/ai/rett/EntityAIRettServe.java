package com.xwm.magicmaid.entity.ai.rett;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class EntityAIRettServe extends EntityAIBase
{
    private static final int COLDTIME = 100;

    private EntityMagicMaid maid;
    private int tick = 0;

    public EntityAIRettServe(EntityMagicMaid maid)
    {
        this.maid = maid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

        if (!this.maid.hasOwner())
            return false;
        if (EnumMode.valueOf(this.maid.getMode()) != EnumMode.SERVE)
            return false;

        EntityLivingBase entityLivingBase = this.maid.world.getPlayerEntityByUUID(this.maid.getOwnerID());
        if (entityLivingBase == null)
            return false;

        return tick++ >= COLDTIME;
    }

    //todo 不朽者效果
    public void startExecuting()
    {
        tick = 0;
        EntityLivingBase entityLivingBase = this.maid.world.getPlayerEntityByUUID(this.maid.getOwnerID());
        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 400, 1 + maid.getRank()));
    }
}
