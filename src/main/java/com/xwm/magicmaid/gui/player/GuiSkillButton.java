package com.xwm.magicmaid.gui.player;

import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class GuiSkillButton extends GuiButton
{
    private static final ResourceLocation BUTTON = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_attribute.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");
    private ISkill iSkill;
    private List<ITextComponent> cache;
    private boolean drawDetail;

    public GuiSkillButton(int buttonId, int x, int y, ISkill skill, boolean drawDetail) {
        super(buttonId, x, y, 84, 74, "");
        this.iSkill = skill;
        this.drawDetail = drawDetail;

        if (!drawDetail) {
            this.x += 32;
            this.width = 56;
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        int offsetx = 59;
        int offsety = 66;
        int width = 17;
        int height = 6;
        return this.enabled && this.visible && mouseX >= this.x + offsetx && mouseY >= this.y + offsety && mouseX < this.x + offsetx + width && mouseY < this.y + offsety + height;
    }


    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (drawDetail)
            drawAll(mc, mouseX, mouseY, partialTicks);
        else
            drawHalf(mc, mouseX, mouseY, partialTicks);
    }

    private void drawAll(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            int offsetx = 59;
            int offsety = 66;
            int width = 17;
            int height = 8;
            int i = 0;
            int j = 0;

            this.hovered = mouseX >= this.x + offsetx && mouseY >= this.y + offsety && mouseX < this.x + offsetx + width && mouseY < this.y + offsety + height;

            // 画背景
            mc.getTextureManager().bindTexture(GuiSkillButton.BACKGROUND);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);

            // 画技能
            iSkill.drawIcon(this.x + 5, this.y + 5, 1.0f);

            // 画按钮
            mc.getTextureManager().bindTexture(GuiSkillButton.BUTTON);
            if (this.enabled)
                this.drawTexturedModalRect(this.x + 59, this.y + 74 - 10, 14, 242, 19, 8);

            // 画等级
            this.drawCenteredString(mc.fontRenderer, "Lv: " + iSkill.getLevel(), this.x + 28, this.y + 60, 16777120);

            // 画说明
            float scale = 1.0f;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            if (cache == null)
                cache =  GuiUtilRenderComponents.splitText(new TextComponentString(iSkill.getDescription()), 28, mc.fontRenderer, true, true);

            for (int l = 0; l < cache.size(); l++) {
                ITextComponent iTextComponent = cache.get(l);
                mc.fontRenderer.drawString(iTextComponent.getUnformattedText(), (int) ((this.x + 57) / scale), (int) ((this.y + 2 + l * (mc.fontRenderer.FONT_HEIGHT + 2)) / scale), 0xFFFFFF);
            }

            GlStateManager.popMatrix();

            // 画按钮上的字
            if (!this.enabled)
                return;

            int color = 14737632;

            if (this.hovered)
            {
                color = 16777120;
                drawSkillDetail(mc, mouseX, mouseY, partialTicks);
            }

            this.drawCenteredString(mc.fontRenderer, "+", this.x + 59 + 19 / 2, this.y + 74 - 11  , color);
        }
    }

    private void drawHalf(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            int i = 0;
            int j = 0;

            // 画背景
            mc.getTextureManager().bindTexture(GuiSkillButton.BACKGROUND);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);

            // 画技能
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 50);
            iSkill.drawIcon(this.x + 5, this.y + 5, 1.0f);
            GlStateManager.popMatrix();

            // 画等级
            this.drawCenteredString(mc.fontRenderer, "Lv: " + iSkill.getLevel(), this.x + 28, this.y + 60, 16777120);
        }
    }

    private void drawSkillDetail(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        int i = 0;
        int j = 182;
        int bwidth = this.width - 10;
        int bheight = this.height - 40;
        String[] detailLines = this.iSkill.getDetailDescription().split("\n");
        for (int t = 0; t < detailLines.length; t++)
            bwidth = Math.max(bwidth, mc.fontRenderer.getStringWidth(detailLines[t]));
        bheight = Math.max(bheight, 10 * detailLines.length + 30);

        GlStateManager.pushMatrix();
        GlStateManager.color(0F, 0F, 0F, 0.4F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.translate(0, 0, 50);
        this.drawTexturedModalRect(mouseX - bwidth, mouseY - bheight, i, j, bwidth, bheight);
        GlStateManager.enableTexture2D();
        int requirePoint = this.iSkill.getRequirePoint();
        mc.fontRenderer.drawString("需要点数: " + (requirePoint == -1 ? "已满级" : requirePoint), mouseX - bwidth + 2, mouseY - bheight + 2, 0xffffff);

        for (int t = 0; t < detailLines.length; t++)
            mc.fontRenderer.drawString(detailLines[t], mouseX - bwidth + 2, mouseY - bheight + 20 + 10 * t + 2, 0xffffff);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public ISkill getSkill() {
        return iSkill;
    }

    public void setSkill(ISkill iSkill) {
        this.iSkill = iSkill;
    }
}
