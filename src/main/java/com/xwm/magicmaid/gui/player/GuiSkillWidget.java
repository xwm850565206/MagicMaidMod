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

public class GuiSkillWidget extends GuiButton
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_attribute.png");
    private ISkill iSkill;
    private List<ITextComponent> cache;

    public GuiSkillWidget(int buttonId, int x, int y, ISkill skill) {
        super(buttonId, x, y, 65, 60, "");
        this.iSkill = skill;
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        int offsetx = 42;
        int offsety = 231 - 182;
        int width = 17;
        int height = 6;
        return this.enabled && this.visible && mouseX >= this.x + offsetx && mouseY >= this.y + offsety && mouseX < this.x + offsetx + width && mouseY < this.y + offsety + height;
    }


    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = 0;
            int j = 182;

            // 画背景
            mc.getTextureManager().bindTexture(GuiSkillWidget.BACKGROUND);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);

            // 画等级
            this.drawCenteredString(mc.fontRenderer, "Lv: " + iSkill.getLevel(), this.x + 18, this.y + 230 - 182 - 2, 16777120);

            // 画说明
            float scale = 1.0f;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            if (cache == null)
                cache =  GuiUtilRenderComponents.splitText(new TextComponentString(iSkill.getDescription()), 28, mc.fontRenderer, true, true);

            for (int l = 0; l < cache.size(); l++) {
                ITextComponent iTextComponent = cache.get(l);
                mc.fontRenderer.drawString(iTextComponent.getUnformattedText(), (int) ((this.x + 37) / scale), (int) ((this.y + 2 + l * (mc.fontRenderer.FONT_HEIGHT + 2)) / scale), 0xFFFFFF);
            }

            GlStateManager.popMatrix();

            // 画按钮
            int color = 14737632;
            if (!this.enabled)
            {
                color = 10526880;
            }
            else if (this.hovered)
            {
                color = 16777120;
            }

            this.drawCenteredString(mc.fontRenderer, "+", this.x + 42 + 17 / 2, this.y +  231 - 182 - 2  , color);
        }
    }
}
