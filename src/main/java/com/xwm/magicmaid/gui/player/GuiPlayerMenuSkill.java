package com.xwm.magicmaid.gui.player;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.gui.GuiNextPageButton;
import com.xwm.magicmaid.manager.ISkillManagerImpl;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.registry.MagicMenuElementRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiPlayerMenuSkill extends GuiScreen
{
    public static final String NAME = "技能";
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_attribute.png");
    private final int imageWidth = 176;
    private final int imageHeight = 166;

    private int currPage = 0;
    private List<List<GuiSkillButton>> skillPages = new ArrayList<>();
    private List<GuiSkillButton> activatePerformSkill = new ArrayList<>();
    private GuiSkillButton chooseSkill = null;

    private static int[][] SKILL_REC;

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

        int offsetLeft = 3;
        int offsetTop = 5;
        int gapx = 0;
        int gapy = 6;
        int index = menuSize+2;
        int skillGuiWidth = 84;
        int noDetailSkillGuiWidth = 52;
        int skillGuiHeight = 70;

        EntityPlayer player = this.mc.player;
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null) {
                int k = 0; // 一页4个
                int total = skillCapability.getPerformSkills().size();
                List<GuiSkillButton> skillRects = new ArrayList<>();
                for (ISkill skill : skillCapability.getPerformSkills()) {
                    int t = k % 4;
                    int x = offsetLeft + (t % 2) * (skillGuiWidth + gapx);
                    int y = offsetTop + (t/2) * (skillGuiHeight + gapy);
                    GuiSkillButton skillButton = new GuiSkillButton(index + k , i + x, j + y, skill, true);
                    this.addButton(skillButton);
                    skillRects.add(skillButton);
                    k++;
                    if (k % 4 == 0) {
                        skillPages.add(skillRects);
                        skillRects = new ArrayList<>();
                    }
                }
                if (skillRects.size() > 0)
                    skillPages.add(skillRects);

                k = 0;
                for (IPerformSkill skill : skillCapability.getActivePerformSkills()) {
                    int x = -skillGuiWidth - offsetLeft - 2 - (k / 2) * (noDetailSkillGuiWidth + 4);
                    int y = offsetTop + (k % 2) * (skillGuiHeight + gapy / 2);
                    GuiSkillButton skillWidget = new GuiSkillButton(total + index + k, i + x, j + y, skill, false);
                    skillWidget.enabled = false;
                    activatePerformSkill.add(skillWidget);
                    k++;
                }

                if (SKILL_REC == null)
                {
                    // 记载gui 4个可选技能的位置
                    SKILL_REC = new int[4 + skillCapability.getActivePerformSkills().size()][4];
                    for (k = 0; k < 4; k++)
                    {
                        int x = offsetLeft + (k%2) * (skillGuiWidth + gapx);
                        int y = offsetTop + (k/2) * (skillGuiHeight + gapy);
                        SKILL_REC[k][0] = x;
                        SKILL_REC[k][1] = y;
                        SKILL_REC[k][2] = x + skillGuiWidth;
                        SKILL_REC[k][3] = y + skillGuiHeight;
                    }

                    // 记载gui 技能槽的位置
                    for (k = 0; k < skillCapability.getActivePerformSkills().size(); k++)
                    {
                        int x = -noDetailSkillGuiWidth - offsetLeft - 2 - (k / 2) * (noDetailSkillGuiWidth + 4); // note: no detail skill gui的技能位置与渲染位置不一样
                        int y = offsetTop + (k % 2) * (skillGuiHeight + gapy / 2);
                        SKILL_REC[k+4][0] = x;
                        SKILL_REC[k+4][1] = y;
                        SKILL_REC[k+4][2] = x + noDetailSkillGuiWidth;
                        SKILL_REC[k+4][3] = y + skillGuiHeight;
                    }
                }
            }
        }

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

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int index = clickSkillRect(mouseX, mouseY);
        if (index != -1 && skillPages.size() > 0) {
            if (index < 4 && chooseSkill == null) {
                if (index < skillPages.get(currPage).size()) {
                    GuiSkillButton clickedWidget = skillPages.get(currPage).get(index);
                    if (!clickedWidget.mousePressed(mc, mouseX, mouseY)) { // 点到升级按钮不操作
                        chooseSkill = new GuiSkillButton(-1, mouseX, mouseY, clickedWidget.getSkill(), false);
                        chooseSkill.enabled = false;
                    }
                }
            }
            else if (index >= 4 && chooseSkill != null) {

                EntityPlayer player = Minecraft.getMinecraft().player;
                if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null))
                {
                    ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                    if (skillCapability == null) return;

//                    MinecraftForge.EVENT_BUS.post(new SkillChangedEvent(player, skillCapability.getActivePerformSkill(index-4), chooseSkill.getSkill()));
                    ISkillManagerImpl.getInstance().setActivePerformSkill(player, index-4, (IPerformSkill) chooseSkill.getSkill());

                    // ui更新
                    activatePerformSkill.get(index - 4).setSkill(chooseSkill.getSkill());

                    chooseSkill = null;
                }

            }
            else {
                chooseSkill = null;
            }
        }
        else {
            chooseSkill = null;
        }
    }

    private int clickSkillRect(int mouseX, int mouseY)
    {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        for (int k = 0; k < SKILL_REC.length; k++)
        {
            int x = SKILL_REC[k][0] + i;
            int y = SKILL_REC[k][1] + j;
            int xx = SKILL_REC[k][2] + i;
            int yy = SKILL_REC[k][3] + j;
            if (x <= mouseX && mouseX < xx && y <= mouseY && mouseY < yy) {
                return k;
            }
        }
        return -1;
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
                if (currPage > 0){
                    setLevelUpButtonEnable(false);
                    currPage--;
                    setLevelUpButtonEnable(true);
                }
            }
            else if (button.id == menuSize + 1) {
                if (currPage < skillPages.size() - 1) {
                    setLevelUpButtonEnable(false);
                    currPage++;
                    setLevelUpButtonEnable(true);
                }
            }
            else if (button.id > menuSize + 1){
                List<GuiSkillButton> skillButtons = skillPages.get(currPage);
                int trueButtonId = (button.id - 6) % 4;
                ISkillManagerImpl.getInstance().upSkillLevel(Minecraft.getMinecraft().player, skillButtons.get(trueButtonId).getSkill());
            }
        }
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
        if (skillPages.size() > 0) {
            List<GuiSkillButton> page = skillPages.get(currPage);
            for (GuiSkillButton skillRect : page) {
                skillRect.drawButton(mc, mouseX, mouseY, partialTicks);
            }
        }

        // 画技能栏
        for (GuiSkillButton skillRect : activatePerformSkill) {
            skillRect.drawButton(mc, mouseX, mouseY, partialTicks);
        }

        // 画技能栏提示
        GlStateManager.pushMatrix();
        float scale = 2;
        GlStateManager.scale(scale, scale , 1);
        GlStateManager.translate(0, 0, this.zLevel + 100);
        for (int k = 0; k < 2; k++)
        {
            int x = i + -52 - 3 - 2 - (52 + 4) + 60 / 2; // note: no detail skill gui的技能位置与渲染位置不一样
            int y = j + 5 + k * (72) + 44 / 2;
//            GlStateManager.scale(4, 4, 1);

            this.drawCenteredString(this.fontRenderer, I18n.format("gui.maid.ctrl_info"), (int)(x / scale), (int)(y / scale), 0xffff00);

//            GlStateManager.scale(0.25, 0.25, 1);
        }
        GlStateManager.popMatrix();

        // 画悬浮技能
        if (chooseSkill != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 50);
            chooseSkill.x = mouseX - chooseSkill.width / 2;
            chooseSkill.y = mouseY - chooseSkill.height / 2;
            chooseSkill.drawButton(mc, mouseX, mouseY, partialTicks);
            GlStateManager.popMatrix();
        }



        for (int t = 0; t < this.buttonList.size() && t < 6; ++t)
        {
            ((GuiButton)this.buttonList.get(t)).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (int t = 0; t < this.labelList.size(); ++t)
        {
            ((GuiLabel)this.labelList.get(t)).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    private void setLevelUpButtonEnable(boolean enable)
    {
        List<GuiSkillButton> buttonList = skillPages.get(currPage);
        for (GuiSkillButton button : buttonList)
            button.enabled = enable;
    }
}
