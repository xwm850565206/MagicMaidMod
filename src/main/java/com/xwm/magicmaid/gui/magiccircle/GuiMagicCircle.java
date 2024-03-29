package com.xwm.magicmaid.gui.magiccircle;

import com.xwm.magicmaid.object.tileentity.TileEntityMagicCircle;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMagicCircle extends GuiContainer
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":textures/gui/gui_circle_window.png");

    private TileEntityMagicCircle magicCircle;

    public GuiMagicCircle(Container inventorySlotsIn, TileEntityMagicCircle magicCircle) {
        super(inventorySlotsIn);
        this.magicCircle = magicCircle;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    /**
     * Draws the background layer of this container (behind the items).
     *
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;

        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        float progress = this.magicCircle.getProgress();
        int width = (int) (24 * progress);

        this.drawTexturedModalRect(i + 96, j + 31, 176, 14, width, 16);
    }
}
