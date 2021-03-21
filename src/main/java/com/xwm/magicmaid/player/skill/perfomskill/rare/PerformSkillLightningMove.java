package com.xwm.magicmaid.player.skill.perfomskill.rare;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class PerformSkillLightningMove extends PerformSkillRareBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    @Override
    public int getPerformEnergy() {
        return 100 - level * 10;
    }

    @Override
    public int getColdTime() {
        return 10;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;
        if (worldIn.isRemote)
        {
            Vec3d vec3d = playerIn.getLook(0.8F);
            playerIn.addVelocity(vec3d.x * level, 0.5 + 0.05 * level, vec3d.z * level);
        }

        else {
            Vec3d vec3d = playerIn.getLook(0.8F);
            double d2 = vec3d.x * level;
            double d3 = vec3d.y * level;
            double d4 = vec3d.z * level;
            for (int i = 0; i < 10; i++) {
                EntityLightningBolt entityLightningBolt = new EntityLightningBolt(worldIn, playerIn.posX + d2 * i, playerIn.posY, playerIn.posZ + d4 * i, true);
                worldIn.addWeatherEffect(entityLightningBolt);
                worldIn.spawnEntity(entityLightningBolt);
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
        return super.getName() + ".lightning_move";
    }

    @Override
    public String getDescription() {
        return "雷动";
    }

    @Override
    public void drawIcon(float x, float y, float scale) {
        // 134 48 46 46
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 181, 47, 46, 46);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDetailDescription() {
        return "使用雷电的力量向前迅速突进\n升级可以降低蓝耗，提高突进距离";
    }

}
