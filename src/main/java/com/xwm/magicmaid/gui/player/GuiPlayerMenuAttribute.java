package com.xwm.magicmaid.gui.player;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.gui.GuiNextPageButton;
import com.xwm.magicmaid.manager.ISkillManagerImpl;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiPlayerMenuAttribute extends GuiScreen
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_attribute.png");
    private final int imageWidth = 176;
    private final int imageHeight = 166;

    private int currPage = 0;
    private List<List<GuiSkillButton>> skillPages = new ArrayList<>();

    public void initGui()
    {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        this.addButton(new GuiPlayerMenuMain.MenuButton(0, i + this.imageWidth, j, "主菜单")).enabled = true;
        this.addButton(new GuiPlayerMenuMain.MenuButton(1, i + this.imageWidth, j + 16+1, "属性")).enabled = false;
        this.addButton(new GuiPlayerMenuMain.MenuButton(2, i + this.imageWidth, j + 16*2+1, "技能")).enabled = true;
        this.addButton(new GuiPlayerMenuMain.MenuButton(3, i + this.imageWidth, j + 16*3+1, "武器")).enabled = true;
        this.addButton(new GuiNextPageButton(4, i + imageWidth / 2 - 10 - 3, j + imageHeight - 12, false));
        this.addButton(new GuiNextPageButton(5, i + imageWidth / 2 + 10 - 3, j + imageHeight - 12, true));

        int offsetLeft = 3;
        int offsetTop = 5;
        int gapx = 0;
        int gapy = 6;
        int index = 6;
        int skillGuiWidth = 84;
        int skillGuiHeight = 70;

        EntityPlayer player = this.mc.player;
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null) {
                int k = 0; // 一页4个
                List<GuiSkillButton> skillRects = new ArrayList<>();
                for (ISkill skill : skillCapability.getAttributeSkills()) {
                    int t = k % 4;
                    int x = offsetLeft + (t%2) * (skillGuiWidth + gapx);
                    int y = offsetTop + (t/2) * (skillGuiHeight + gapy);
                    GuiSkillButton skillButton = new GuiSkillButton(index + k , i + x, j + y, skill, true);
                    skillButton.enabled = false; // 初始化先全都把按钮设置为不可用
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
            }
        }

        setLevelUpButtonEnable(true); // 第一页的按钮可用

        super.initGui();
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
                this.mc.player.openGui(Main.instance, Reference.GUI_PLAYER_MENU_MAIN, this.mc.world, (int)this.mc.player.posX, (int)this.mc.player.posY, (int)this.mc.player.posZ);
            }
            else if (button.id == 1)
            {

            }
            else if (button.id == 2)
            {
                mc.displayGuiScreen(new GuiPlayerMenuSkill());
            }
            else if (button.id == 4) {
                if (currPage > 0){
                    setLevelUpButtonEnable(false);
                    currPage--;
                    setLevelUpButtonEnable(true);
                }

            }
            else if (button.id == 5) {
                if (currPage < skillPages.size() - 1) {
                    setLevelUpButtonEnable(false);
                    currPage++;
                    setLevelUpButtonEnable(true);
                }

            }
            else if (button.id > 5){
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
