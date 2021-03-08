package com.xwm.magicmaid.gui.player;

import com.xwm.magicmaid.network.CPacketChangeDifficulty;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.skill.CPacketSkillPoint;
import com.xwm.magicmaid.object.item.interfaces.ICanGetSkillPoint;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.store.WorldDifficultyData;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class GuiPlayerMenuMain extends GuiContainer
{
    private static final String[] DIFFICULT = {"简单", "普通", "困难"};
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_main.png");
    private final int imageWidth = 176;
    private final int imageHeight = 166;
    /** The old x position of the mouse pointer */
    private float oldMouseX;
    /** The old y position of the mouse pointer */
    private float oldMouseY;

    private EntityPlayer player;
    private SelectButton backward;
    private SelectButton forward;
    private IInventory singleSlot;

    public GuiPlayerMenuMain(Container inventorySlotsIn, EntityPlayer player) {
        super(inventorySlotsIn);
        this.player = player;
        this.singleSlot = ((ContainerPlayerMenuMain)inventorySlotsIn).singleSlot;
    }

    public void initGui()
    {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        this.addButton(new MenuButton(0, i + this.imageWidth, j, "主菜单")).enabled = false;
        this.addButton(new MenuButton(1, i + this.imageWidth, j + 16+1, "属性")).enabled = true;
        this.addButton(new MenuButton(2, i + this.imageWidth, j + 16*2+1, "技能")).enabled = true;
        this.addButton(new MenuButton(3, i + this.imageWidth, j + 16*3+1, "武器")).enabled = true;

        this.addButton(new GuiButton(4, i + 97, j + 10, 20, 11, "吸收"));
        backward = this.addButton(new SelectButton(5, i + 145, j + 66, false));
        forward = this.addButton(new SelectButton(6, i + 145 + 9 + 5, j + 66, true));

        backward.enabled = true;
        forward.enabled = true;

        super.initGui();
    }
    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                // todo
            }
            else if (button.id == 1)
            {
                mc.displayGuiScreen(new GuiPlayerMenuAttribute());
            }
            else if (button.id == 2)
            {
                mc.displayGuiScreen(new GuiPlayerMenuSkill());
            }
            else if (button.id == 3)
            {
                ; // todo
            }
            else if (button.id == 4)
            {
//                ISkillManagerImpl.instance.addSkillPoint(player);
                CPacketSkillPoint packet = new CPacketSkillPoint(player.getEntityWorld().provider.getDimension(), player.getEntityId());
                NetworkLoader.instance.sendToServer(packet);
            }
            else if(button.id == 5)
            {
                WorldDifficultyData data = WorldDifficultyData.get(Minecraft.getMinecraft().world);
                int difficulty = data.getWorldDifficulty() - 1;
                data.setWorldDifficulty(difficulty); // client update
                CPacketChangeDifficulty packet = new CPacketChangeDifficulty(difficulty); // server update
                NetworkLoader.instance.sendToServer(packet);
            }
            else if (button.id == 6)
            {
                WorldDifficultyData data = WorldDifficultyData.get(Minecraft.getMinecraft().world);
                int difficulty = data.getWorldDifficulty() + 1;
                data.setWorldDifficulty(difficulty);
                CPacketChangeDifficulty packet = new CPacketChangeDifficulty(difficulty);
                NetworkLoader.instance.sendToServer(packet);
            }
        }
    }
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        this.oldMouseX = (float)mouseX;
        this.oldMouseY = (float)mouseY;
    }


    /**
     * Draws an entity on the screen looking toward the cursor.
     */
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
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
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        // 画背景
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i, j, 0, 0, this.imageWidth, imageHeight);

        drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.mc.player);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String tmp = DIFFICULT[WorldDifficultyData.get(mc.world).getWorldDifficulty()];
        fontRenderer.drawString(tmp, 125,  68, 0x000000);

        ItemStack stack = this.singleSlot.getStackInSlot(0);
        if (stack.getItem() instanceof ICanGetSkillPoint) {
            String tmp1 = TextFormatting.YELLOW + "点数: " + ((ICanGetSkillPoint) stack.getItem()).getSkillPoint(stack, player) * stack.getCount();
            fontRenderer.drawString(tmp1, 76, 26, 0x111111);
        }

        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null))
        {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null) {
                int curSkillPoint = skillCapability.getSkillPoint();
//                float scaled = 0.9f;
//                GlStateManager.scale(scaled, 1, 1);
                fontRenderer.drawString(TextFormatting.YELLOW + "当前持有 " + curSkillPoint, (int) (120), (int) (11), 0x111111);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class MenuButton extends GuiButton
    {
        public MenuButton(int buttonID, int x, int y, String text)
        {
            super(buttonID, x, y, 38, 16, text);
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                mc.getTextureManager().bindTexture(GuiPlayerMenuMain.BACKGROUND);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

                int i = 0;
                int j = 166;
                int color = 14737632;

                if (!this.enabled)
                {
                    i += this.width;
                    color = 10526880;
                }
                else if (this.hovered)
                {
                    color = 16777120;
                }

                this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);
                this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
            }


        }
    }

    @SideOnly(Side.CLIENT)
    static class SelectButton extends GuiButton
    {
        private final boolean forward;

        public SelectButton(int buttonID, int x, int y, boolean forward)
        {
            super(buttonID, x, y, 10, 15, "");
            this.forward = forward;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                mc.getTextureManager().bindTexture(GuiPlayerMenuMain.BACKGROUND);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                int i = 193;
                int j = 0;

                if (!this.enabled)
                {
                    j += this.width * 2;
                }
                else if (flag)
                {
                    j += this.width;
                }

                if (!this.forward)
                {
                    i += this.height;
                }

                this.drawTexturedModalRect(this.x, this.y, j, i, this.width, this.height);
            }
        }
    }

}
