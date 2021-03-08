package com.xwm.magicmaid.player.skill.perfomskill;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class PerformSkillBoost extends PerformSkillBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    @Override
    public int getPerformEnergy() {
        return 100;
    }

    @Override
    public int getColdTime() {
        return 120;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;

        if (getLevel() >= 0)
        {
            playerIn.addPotionEffect(new PotionEffect(MobEffects.SPEED, 120, 1 + getLevel()));
        }

        if (getLevel() >= 1)
        {
            playerIn.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 120, 1 + getLevel()));
        }

        if (getLevel() >= 2)
        {
            playerIn.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 120, 1 + getLevel()));
        }

        if (getLevel() >= 3)
        {
            playerIn.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 120, 1 + getLevel()));
        }

        curColdTime = getColdTime();
    }

    @Override
    public int getRequirePoint() {
        return getLevel() < getMaxLevel() ? 20 + 20 * getLevel() : -1;
    }

    @Override
    public String getDescription() {
        return "潜能激发";
    }

    @Override
    public void drawIcon(int x, int y, float scale) {
        // 134 48 46 46
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 134, 48, 46, 46);
        GlStateManager.popMatrix();
    }
}
