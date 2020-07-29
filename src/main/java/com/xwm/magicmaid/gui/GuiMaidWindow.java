package com.xwm.magicmaid.gui;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.maid.EntityMagicMaid;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMaidWindow extends GuiContainer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":textures/gui/maidwindow.png");
    private InventoryPlayer player;
    private EntityMagicMaid maid;

    public GuiMaidWindow(Container inventory, InventoryPlayer player) {
        super(inventory);
        this.player = player;
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
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        Main.logger.info("call foregroundLayer");
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
