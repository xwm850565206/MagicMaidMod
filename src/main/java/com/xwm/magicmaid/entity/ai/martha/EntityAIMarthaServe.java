package com.xwm.magicmaid.entity.ai.martha;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.init.PotionInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class EntityAIMarthaServe extends EntityAIBase
{
    private EntityMagicMaid maid;
    private int tick = 0;

    public EntityAIMarthaServe(EntityMagicMaid maid)
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

        PotionEffect effect = entityLivingBase.getActivePotionEffect(PotionInit.PROTECT_BLESS_EFFECT);
        return effect == null;
    }

    public void startExecuting()
    {
        EntityLivingBase entityLivingBase = this.maid.world.getPlayerEntityByUUID(this.maid.getOwnerID());
        if (entityLivingBase == null)
            return;

        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1 + maid.getRank()));
        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, maid.getRank()));
        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, maid.getRank()));
        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 2 + maid.getRank()));

        if (maid.hasArmor()){
            entityLivingBase.addPotionEffect(new PotionEffect(PotionInit.PROTECT_BLESS_EFFECT, 400 + maid.getRank() * 400, maid.getRank()));
        }
    }
}
