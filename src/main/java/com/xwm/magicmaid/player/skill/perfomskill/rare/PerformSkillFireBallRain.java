package com.xwm.magicmaid.player.skill.perfomskill.rare;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class PerformSkillFireBallRain extends PerformSkillRareBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");
    private Random random = new Random();

    @Override
    public int getPerformEnergy() {
        return 200 - level * 10;
    }

    @Override
    public int getColdTime() {
        return 100 - level * 10;
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
            for (int i = 0; i < level * 3; i++) {
                EntityLargeFireball entitylargefireball = new EntityLargeFireball(worldIn, playerIn, d2, d3, d4);
                entitylargefireball.explosionPower = this.level;
                BlockPos ballPos = playerIn.getPosition().offset(playerIn.getHorizontalFacing(), 4 + 4 * (i / 3)).offset(playerIn.getHorizontalFacing().rotateY(), 3* (-1 + i % 3)).up(4);
                entitylargefireball.posX = ballPos.getX() ;
                entitylargefireball.posY = ballPos.getY();
                entitylargefireball.posZ = ballPos.getZ();
                worldIn.spawnEntity(entitylargefireball);
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
        return super.getName() + ".fireball_rain";
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

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 181, 94, 46, 46);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDetailDescription() {
        return "召唤一片火球雨\n升级会减少蓝耗，减少技能冷却，增加火球数量和威力";
    }
}
