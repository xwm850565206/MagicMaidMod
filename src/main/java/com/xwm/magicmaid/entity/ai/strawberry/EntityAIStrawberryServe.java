package com.xwm.magicmaid.entity.ai.strawberry;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EnumModes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class EntityAIStrawberryServe extends EntityAIBase
{
    private static final int COLDTIME = 100;

    private EntityMagicMaid maid;
    private int tick = 0;

    public EntityAIStrawberryServe(EntityMagicMaid maid)
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
        if (EnumModes.valueOf(this.maid.getMode()) != EnumModes.SERVE)
            return false;

        EntityLivingBase entityLivingBase = this.maid.world.getPlayerEntityByUUID(this.maid.getOwnerID());
        if (entityLivingBase == null)
            return false;

        return tick++ >= COLDTIME;
    }

    //todo 守护者效果
    public void startExecuting()
    {
        tick = 0;
        EntityLivingBase entityLivingBase = this.maid.world.getPlayerEntityByUUID(this.maid.getOwnerID());
        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
    }
}
