package com.xwm.magicmaid.player.skill.perfomskill.normal;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class PerformSkillLightning extends PerformSkillNormalBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");
    private Random random = new Random();

    @Override
    public int getPerformEnergy() {
        return 200 - 10 * level;
    }

    @Override
    public int getColdTime() {
        return 50 - 10 * level;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;
        if (worldIn.isRemote)
        {
            for (int i = 0; i < 5; i++)
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, playerIn.posX + random.nextDouble(), playerIn.posY + playerIn.height / 2.0 + random.nextDouble(), playerIn.posZ + random.nextDouble(), 0.1*(random.nextDouble()-0.5), 0.1*random.nextDouble(), 0.1*(random.nextDouble()-0.5));
        }

        else {
            Vec3d vec3d = playerIn.getLook(0.5F);
            double d2 = vec3d.x;
            double d3 = vec3d.y;
            double d4 = vec3d.z;
            EntityLightningBolt entityLightningBolt = new EntityLightningBolt(worldIn, playerIn.posX + d2*6, playerIn.posY, playerIn.posZ + d4*6, false);
            worldIn.addWeatherEffect(entityLightningBolt);
            worldIn.spawnEntity(entityLightningBolt);
        }

        curColdTime = getColdTime();
    }

    @Override
    public int getRequirePoint() {
        return getLevel() < getMaxLevel() ? 2000 * level * level : -1;
    }

    @Override
    public String getName() {
        return super.getName() + ".lightning";
    }

    @Override
    public String getDescription() {
        return "落雷术";
    }

    @Override
    public void drawIcon(float x, float y, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 181, 0, 46, 46);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDetailDescription() {
        return "前方一定区域降下一道闪电\n升级会降低冷却，减少蓝耗";
    }

}
