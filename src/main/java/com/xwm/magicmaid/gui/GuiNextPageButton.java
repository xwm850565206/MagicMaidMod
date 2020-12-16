package com.xwm.magicmaid.gui;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNextPageButton extends GuiButton
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_attribute.png");
    private final boolean isForward;

    public GuiNextPageButton(int buttonId, int x, int y, boolean isForwardIn)
    {
        super(buttonId, x, y, 7, 11, "");
        this.isForward = isForwardIn;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {

        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(BACKGROUND);
        int i = 0;
        int j = 242;

        if (this.isForward)
        {
            i += width;
        }

        this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);
    }
}
