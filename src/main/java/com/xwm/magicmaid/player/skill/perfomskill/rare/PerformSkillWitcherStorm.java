package com.xwm.magicmaid.player.skill.perfomskill.rare;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class PerformSkillWitcherStorm extends PerformSkillRareBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    @Override
    public int getPerformEnergy() {
        return 100 + 50 * level;
    }

    @Override
    public int getColdTime() {
        return 50;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;

        worldIn.playEvent(1015, playerIn.getPosition(), 0);

        if (worldIn.isRemote)
        {

        }

        else {
            double radius = 2.0f;
            for (int i = 0; i < 4 * level; i++) {
                double angle = i * (360 / (4*level));
                double tx = Math.sin(angle);
                double tz = Math.cos(angle);
                EntityWitherSkull witherSkull = new EntityWitherSkull(worldIn, playerIn, tx, 0, tz);
                witherSkull.setPosition(playerIn.posX + radius * tx, playerIn.posY + playerIn.height / 2.0F, playerIn.posZ + radius * tz);
                worldIn.spawnEntity(witherSkull);
            }

        }

        curColdTime = getColdTime();
    }

    @Override
    public int getRequirePoint() {
        return getLevel() < getMaxLevel() ? 1000 * level * level : -1;
    }

    @Override
    public String getName() {
        return super.getName() + ".witch_storm";
    }

    @Override
    public String getDescription() {
        return "弱者退散";
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

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 182, 189, 46, 46);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDetailDescription() {
        return "向周围发射凋零骷髅\n升级会增大蓝耗，增加凋零骷髅的数量";
    }
}
