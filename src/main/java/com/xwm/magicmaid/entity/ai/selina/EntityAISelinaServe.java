package com.xwm.magicmaid.entity.ai.selina;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.init.PotionInit;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.PotionEffect;

import static com.xwm.magicmaid.registry.MagicEquipmentRegistry.NONE;


public class EntityAISelinaServe extends EntityAIBase
{
    private EntityMagicMaid maid;

    public EntityAISelinaServe(EntityMagicMaid maid){
        this.maid = maid;
    }

    @Override
    public boolean shouldExecute() {

        if (!this.maid.hasOwner())
            return false;
        if (EnumMode.valueOf(this.maid.getMode()) != EnumMode.SERVE)
            return false;

        EntityLivingBase entityLivingBase = this.maid.world.getPlayerEntityByUUID(this.maid.getOwnerID());
        if (entityLivingBase == null)
            return false;

        if (maid.getDistance(entityLivingBase) > 20)
            return false;


        PotionEffect effect = entityLivingBase.getActivePotionEffect(PotionInit.WISE_BLESS_EFFECT);
        return effect == null;
    }

    public void startExecuting()
    {
        EntityLivingBase entityLivingBase = this.maid.world.getPlayerEntityByUUID(this.maid.getOwnerID());
        if (entityLivingBase == null)
            return;
        if (MagicEquipmentRegistry.getAttribute(maid.getArmorType()) != NONE){
            entityLivingBase.addPotionEffect(new PotionEffect(PotionInit.WISE_BLESS_EFFECT, 400 + maid.getRank() * 400, maid.getRank()));
        }
    }
}
