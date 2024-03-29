package com.xwm.magicmaid.gui.maidwindow;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.network.entity.CPacketMaidMode;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiMaidWindow extends GuiContainer
{
    private static final ResourceLocation WEAPONSLOT = new ResourceLocation(Reference.MODID + ":textures/gui/weapon_icon.png");
    private static final ResourceLocation ARMORSLOT = new ResourceLocation(Reference.MODID + ":/gui/armor_icon.png");

    private static final int BUTTON_MODE_SWITH = 0;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":textures/gui/maidwindow.png");
    private InventoryPlayer player;
    private EntityMagicMaid maid;

    /** The old x position of the mouse pointer */
    private float oldMouseX;
    /** The old y position of the mouse pointer */
    private float oldMouseY;

    private ContainerMaidWindow maidWindow;


    public GuiMaidWindow(ContainerMaidWindow maidWindow, InventoryPlayer player, EntityMagicMaid maid) {
        super(maidWindow);
        this.maidWindow = maidWindow;
        this.player = player;
        this.maid = maid;
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        this.oldMouseX = (float)mouseX;
        this.oldMouseY = (float)mouseY;
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

        if (this.maid != null)
        {
            drawHealthBarNumLayer(i, j);
            drawHealthNumLayer(i, j);
            drawExpLayer(i, j);
            drawRankLayer(i, j);
            drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.maid);
        }
    }

    /**
     * Draws an entity on the screen looking toward the cursor.
     */
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
//        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(1, 1, 1);
//        GlStateManager.translate(12, 0, 0);
        String tmp = I18n.format("container.maid.healthBar");
        fontRenderer.drawString(tmp,77 + 75, 9, 0x000000);
        fontRenderer.drawString(String.valueOf(maid.getHealthBarNum()), 87, 9,0x000000);

        tmp = I18n.format("container.maid.health");
        fontRenderer.drawString(tmp,77 + 75,  19, 0x000000);
        fontRenderer.drawString(String.valueOf(maid.getHealth()), 87, 19,0x000000);

        tmp = I18n.format("container.maid.exp");
        fontRenderer.drawString(tmp, 77 + 75,  29, 0x000000);
        fontRenderer.drawString(String.valueOf(maid.getExp()), 87, 29,0x000000);

        tmp = I18n.format("container.maid.rank");
        fontRenderer.drawString(tmp, 77 + 75, 39, 0x000000);
        fontRenderer.drawString(String.valueOf( maid.getRank()), 87, 39,0x000000);

        GlStateManager.popMatrix();



        tmp = I18n.format("container.maid." + EnumMode.valueOf(maid.getMode()).toString().toLowerCase());
        fontRenderer.drawString(tmp, 77,  70, 0x000000);
    }


    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiButton(BUTTON_MODE_SWITH, this.guiLeft + 130, this.guiTop + 64, 40, 15,I18n.format("container.button.mode_switch"))
        {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
            {
                super.drawButton(mc, mouseX, mouseY, partialTicks);
            }
        });
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id){
            case BUTTON_MODE_SWITH: if (EnumMode.valueOf(this.maid.getMode()) != EnumMode.BOSS) swithMode();break;
            default: super.actionPerformed(button);
        }
    }

    private void swithMode()
    {
        CPacketMaidMode packet = new CPacketMaidMode(maid.getEntityId(), maid.getEntityWorld().provider.getDimension());
        NetworkLoader.instance.sendToServer(packet);
    }

    private void drawHealthBarNumLayer(int x, int y){
        drawTexturedModalRect(x + 78, y + 10, 178, 47, 6, 6);
    }

    private void drawHealthNumLayer(int x, int y){
        drawTexturedModalRect(x + 78, y + 20, 178, 38, 7, 7);
    }

    private void drawExpLayer(int x, int y){
        drawTexturedModalRect(x + 80 , y + 31, 180, 63,3, 5);
    }

    private void drawRankLayer(int x, int y){
        drawTexturedModalRect(x + 79, y + 41, 179, 56, 5, 5);
    }


}
