package com.xwm.magicmaid.gui.player;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.gui.GuiNextPageButton;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.skill.CPacketLearnSkill;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.registry.MagicMenuElementRegistry;
import com.xwm.magicmaid.registry.MagicSkillRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;

public class GuiPlayerMenuWeapon extends GuiScreen
{
    public static final String NAME = "武器";
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_attribute.png");
    private final int imageWidth = 176;
    private final int imageHeight = 166;

    private int currPage = 0;

    public void initGui()
    {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        for (String value : MagicMenuElementRegistry.MENU_INDEX.values())
        {
            int index = MagicMenuElementRegistry.getMenuIndex(value);
            this.addButton(new GuiPlayerMenuMain.MenuButton(index, i + this.imageWidth, j + 16 * index, value)).enabled = (!value.equals(NAME));
        }
        int menuSize = MagicMenuElementRegistry.MENU_INDEX.size();
        this.addButton(new GuiNextPageButton(menuSize, i + imageWidth / 2 - 10 - 3, j + imageHeight - 12, false));
        this.addButton(new GuiNextPageButton(menuSize+1, i + imageWidth / 2 + 10 - 3, j + imageHeight - 12, true));

        this.addButton(new GuiButton(menuSize+2, i + 55, j + 100, 60, 20,"学习"));

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
        int menuSize = MagicMenuElementRegistry.MENU_INDEX.size();
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.mc.player.openGui(Main.instance, Reference.GUI_PLAYER_MENU_MAIN, this.mc.world, (int)this.mc.player.posX, (int)this.mc.player.posY, (int)this.mc.player.posZ);
            }
            else if (button.id < menuSize)
            {
                String name = MagicMenuElementRegistry.getIndexMenu(button.id);
                if (!name.equals(NAME))
                {
                    GuiScreen screen = MagicMenuElementRegistry.getIndexGui(button.id);
                    if (screen != null)
                        mc.displayGuiScreen(screen);
                }
            }

            else if (button.id == menuSize) {
                currPage = (MagicSkillRegistry.WEAPON_SKILL_MAP.size() + currPage - 1) % MagicSkillRegistry.WEAPON_SKILL_MAP.size();
            }
            else if (button.id == menuSize + 1) {
                currPage = (currPage + 1) % MagicSkillRegistry.WEAPON_SKILL_MAP.size();
            }
            else if (button.id == menuSize + 2){
                ISkill skill = MagicSkillRegistry.getSkill(MagicSkillRegistry.WEAPON_SKILL_MAP.get(currPage));
                if (skill != null) {
//                    ISkillCapability capability = Minecraft.getMinecraft().player
                    if (checkSkillAvaliable(Minecraft.getMinecraft().player)) {
                        CPacketLearnSkill packet = new CPacketLearnSkill(Minecraft.getMinecraft().player.getUniqueID(), skill.getName());
                        NetworkLoader.instance.sendToServer(packet);
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("学习技能" + skill.getDescription()));
                    }
                    else {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("只能学习一次"));
                    }
                }
                else {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString("不能学习空技能"));
                }
            }
        }
    }

    private boolean checkSkillAvaliable(EntityPlayerSP player)
    {
        ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
        if (skillCapability == null)
            return false;
        else
        {
            for (String name : MagicSkillRegistry.WEAPON_SKILL_MAP)
            {
                if (skillCapability.getPerformSkill(name) != null)
                    return false;
            }

            return true;
        }
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRenderer.drawString("注意，武器技能只能学习一个!", 30, 20, 0x000000);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        // 画背景
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i, j, 0, 0, this.imageWidth, imageHeight);

        // 画技能
        int pre = (MagicSkillRegistry.WEAPON_SKILL_MAP.size() + currPage - 1) % MagicSkillRegistry.WEAPON_SKILL_MAP.size();
        int nex = (currPage + 1) % MagicSkillRegistry.WEAPON_SKILL_MAP.size();

        ISkill preSKill = MagicSkillRegistry.getSkill(MagicSkillRegistry.WEAPON_SKILL_MAP.get(pre));
        ISkill curSKill = MagicSkillRegistry.getSkill(MagicSkillRegistry.WEAPON_SKILL_MAP.get(currPage));
        ISkill nexSKill = MagicSkillRegistry.getSkill(MagicSkillRegistry.WEAPON_SKILL_MAP.get(nex));

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1, 1, 1, 0.25f);
        preSKill.drawIcon(i + 35, j + 40, 1);
        nexSKill.drawIcon(i + 95, j + 40, 1);
        GlStateManager.color(1, 1, 1, 1);
        curSKill.drawIcon(i + 65, j + 40, 1);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.translate(i, j, 0);
        RenderHelper.disableStandardItemLighting();
        drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
