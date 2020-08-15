package com.xwm.magicmaid.potion;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionWiseBless extends Potion
{
    public PotionWiseBless(String name, boolean isBadEffectIn, int liquidColorIn, int iconIndexX, int iconIndexY) {
        super(isBadEffectIn, liquidColorIn);
        setPotionName(name);
        setIconIndex(iconIndexX, iconIndexY);
        setRegistryName(new ResourceLocation(Reference.MODID + ":" + name));
    }

    public boolean hasStatusIcon()
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MODID + ":/textures/gui/effect.png"));
        return true;
    }

    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn instanceof EntityPlayer)
        {
            if (entityLivingBaseIn.isPotionActive(this)) {
                ((EntityPlayer) entityLivingBaseIn).capabilities.allowFlying = true;
            }
            else {
                ((EntityPlayer) entityLivingBaseIn).capabilities.allowFlying = false;
            }
        }
    }


    public boolean isInstant()
    {
        return true;
    }
}
