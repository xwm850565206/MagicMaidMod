package com.xwm.magicmaid.player.skill.perfomskill;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class PerformSkillRepel extends PerformSkillBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    @Override
    public int getPerformEnergy() {
        return 200;
    }

    @Override
    public int getColdTime() {
        return 100 - 20 * level;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {
        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;

        List<EntityLivingBase> entityLivingBases = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, playerIn.getEntityBoundingBox().grow(2+level, 1, 2+level));
        for (EntityLivingBase entityLivingBase : entityLivingBases) {
            if (entityLivingBase == playerIn) continue;
            try {
                double motionX = (entityLivingBase.posX - playerIn.posX) / Math.abs(entityLivingBase.posX - playerIn.posX) * Math.abs(2 - Math.abs(entityLivingBase.posX - playerIn.posX));
                double motionY = 0.01;
                double motionZ = (entityLivingBase.posZ - playerIn.posZ) / Math.abs(entityLivingBase.posZ - playerIn.posZ) * Math.abs(2 - Math.abs(entityLivingBase.posZ - playerIn.posZ));
                entityLivingBase.addVelocity(motionX, motionY, motionZ);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        curColdTime = getColdTime();
    }

    @Override
    public int getRequirePoint() {
        return getLevel() < getMaxLevel() ? 100 * getLevel() : -1;
    }

    @Override
    public String getName() {
        return super.getName() + ".normal.repel";
    }

    @Override
    public String getDescription() {
        return "击退";
    }

    @Override
    public void drawIcon(int x, int y, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 134, 93, 46, 46);
        GlStateManager.popMatrix();
    }
}
