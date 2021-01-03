package com.xwm.magicmaid.player.skill.perfomskill;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PerformSkillBoost extends PerformSkillBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    @Override
    public int getPerformEnergy() {
        return 200;
    }

    @Override
    public int getColdTime() {
        return 120;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

        if (curColdTime > 0) return;

        curColdTime = getColdTime();

//        if (worldIn.isRemote) return;

        if (getLevel() >= 0)
        {
            playerIn.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 1 + getLevel()));
        }

        if (getLevel() >= 1)
        {
            playerIn.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1200, 1 + getLevel()));
        }

        if (getLevel() >= 2)
        {
            playerIn.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 1200, 1 + getLevel()));
        }

        if (getLevel() >= 3)
        {
            playerIn.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1200, 1 + getLevel()));
        }
    }

    @Override
    public int getRequirePoint() {
        return 20 + 20 * getLevel();
    }

    @Override
    public String getDescription() {
        return "潜能激发";
    }

    @Override
    public void drawIcon(int x, int y) {
        // 0 90 65 101
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 24.0 / 65.0;
        double scaley = 29.0 / 101.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 90, 65, 101);
        GlStateManager.popMatrix();
    }
}
