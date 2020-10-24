package com.xwm.magicmaid.potion;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBianFlower extends Potion
{
    public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID + ":textures/gui/potion_effect.png");

    public PotionBianFlower(String name, boolean isBadEffectIn, int liquidColorIn, int iconIndexX, int iconIndexY) {
        super(isBadEffectIn, liquidColorIn);
        setPotionName(name);
        setIconIndex(iconIndexX, iconIndexY);
        setRegistryName(new ResourceLocation(Reference.MODID + ":" + name));
    }

    public boolean hasStatusIcon()
    {
        return true;
    }

    @Override
    public boolean isInstant()
    {
        return false;
    }

    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
        try {
            GlStateManager.pushMatrix();
            mc.getTextureManager().bindTexture(ICON);
            double f1 = 18.0 / 112.0;
            double f2 = 18.0 / 198.0;
            GlStateManager.translate(x + 5, y + 6, 0);
            GlStateManager.scale(f1, f2, 0);
            mc.ingameGUI.drawTexturedModalRect(0, 0, 65, 18, 112, 198);
            GlStateManager.popMatrix();
        } catch (NullPointerException e){
            ;
        }
    }

    public boolean shouldRenderHUD(PotionEffect effect)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
        try {
            GlStateManager.pushMatrix();
            mc.getTextureManager().bindTexture(ICON);
            double f1 = 18.0 / 112.0;
            double f2 = 18.0 / 198.0;
            GlStateManager.translate(x + 4, y + 4, 0);
            GlStateManager.scale(f1, f2, 0);
            mc.ingameGUI.drawTexturedModalRect(0, 0, 65, 18, 112, 198);
            GlStateManager.popMatrix();
        } catch (NullPointerException e){
            ;
        }
    }

}
