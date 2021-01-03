package com.xwm.magicmaid.gui.player;

import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSkillHDU extends Gui
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_attribute.png");
    private ISkill iSkill;
    private String key;
    private int x;
    private int y;
    private int width; //36
    private int height; // 42

    // 技能图标位置 6 189 24 29

    public GuiSkillHDU(ISkill iSkill, int x, int y)
    {
        this(iSkill, "", x, y, 36, 42);
    }

    public GuiSkillHDU(ISkill iSkill, String key, int x, int y, int width, int height) {
        this.iSkill = iSkill;
        this.key = key;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawScreen(Minecraft mc)
    {
        int i = 0;
        int j = 182;
        mc.getTextureManager().bindTexture(GuiSkillHDU.BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 画背景
        this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);

        try {
            // 画技能
            iSkill.drawIcon(this.x + 6, this.y + 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            mc.getTextureManager().bindTexture(GuiSkillHDU.BACKGROUND);
            // 画冷却
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 90);
            if (iSkill instanceof IPerformSkill) {
                if (((IPerformSkill) iSkill).getColdTime() == 0)
                    return;
                double progress = ((IPerformSkill) iSkill).getCurColdTime() * 1.0 / ((IPerformSkill) iSkill).getColdTime();
                GlStateManager.color(1, 1, 1, 0.8F);
                this.drawTexturedModalRect(this.x + 6, this.y + 7, 76, 166, 24, (int)(29 * progress));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GlStateManager.popMatrix();
        }
    }

    public ISkill getiSkill() {
        return iSkill;
    }

    public void setiSkill(ISkill iSkill) {
        this.iSkill = iSkill;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
