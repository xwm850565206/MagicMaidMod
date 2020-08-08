package com.xwm.magicmaid.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;

public class ParticleSpawner
{
    public static final int DISTANCE = 20;
    private static Minecraft mc = Minecraft.getMinecraft();

    public static Particle spawnParticle(EnumCustomParticles type, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            int var14 = mc.gameSettings.particleSetting;

            if (var14 == 1 && mc.world.rand.nextInt(3) == 0)
            {
                var14 = 2;
            }

            double var15 = mc.getRenderViewEntity().posX - par2;
            double var17 = mc.getRenderViewEntity().posY - par4;
            double var19 = mc.getRenderViewEntity().posZ - par6;
            double var22 = DISTANCE;
            Particle var21 = null;

            if (var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22)
            {
                return null;
            }
            else if (var14 > 1)
            {
                return null;
            }
            else
            {
                var21 = getParticleFromEnumParticels(type, par2, par4, par6, par8, par10, par12);
                mc.effectRenderer.addEffect(var21);
                return var21;
            }
        }
        return null;
    }

    private static Particle getParticleFromEnumParticels(EnumCustomParticles type, double d0, double d1, double d2, double d3, double d4, double d5)
    {
        switch (type) {
            case SOUL:
                return new SoulParticle(mc.world, d0, d1, d2);
            case CROSS:
                return new CrossParticle(mc.world, d0, d1, d2);
            case CONVICTION:
                return new ConvictionParticle(mc.world, d0, d1, d2);
            case STAR:
                return new StarParticle(mc.world, d0, d1, d2);
//            case RED_STRIP: return new SuperHimAttackParticle(mc.world, d0, d1, d2, 0xDC143C);
//            case ORANGE_STRIP: return new SuperHimAttackParticle(mc.world, d0, d1, d2, 0xFFA500);
//            case PURPLE_STRIP: return new SuperHimAttackParticle(mc.world, d0, d1, d2, 0xD15FEE);
//            case WIND: return new WindParticle(mc.world, d0, d1, d2, d3);
//            case BRILLIANT: return new BrilliantParticle(mc.world, d0, d1, d2);
//            case GRAY_SOUL: return new DistinationParticle(mc.world, d0, d1, d2, d3, d4, d5, 0xA3A3A3);
//            case YELLO_SOUL: return new DistinationParticle(mc.world, d0, d1, d2, d3, d4, d5, 0xFFC125);
//            default:return new SuperHimAttackParticle(mc.world, d0, d1, d2, 0xD15FEE);
            default: return new SoulParticle(mc.world, d0, d1, d2);
        }
    }
}
