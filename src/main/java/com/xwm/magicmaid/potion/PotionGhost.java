package com.xwm.magicmaid.potion;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//todo
public class PotionGhost extends Potion
{
    public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID + ":textures/gui/potion_effect.png");

    public PotionGhost(String name, boolean isBadEffectIn, int liquidColorIn, int iconIndexX, int iconIndexY) {
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
            double f1 = 18.0 / 78.0;
            double f2 = 18.0 / 104.0;
            GlStateManager.translate(x + 5, y + 6, 0);
            GlStateManager.scale(f1, f2, 0);
            mc.ingameGUI.drawTexturedModalRect(0, 0, 179, 153, 78, 104);
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
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        try {
            GlStateManager.pushMatrix();
            mc.getTextureManager().bindTexture(ICON);
            double f1 = 18.0 / 78.0;
            double f2 = 18.0 / 108.0;
            GlStateManager.translate(x + 3, y + 3, 0);
            GlStateManager.scale(f1, f2, 0);
            mc.ingameGUI.drawTexturedModalRect(0, 0, 179, 153, 78, 104);
            GlStateManager.popMatrix();
        } catch (NullPointerException e){
            ;
        }
    }

}
