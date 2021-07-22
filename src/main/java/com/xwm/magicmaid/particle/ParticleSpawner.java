package com.xwm.magicmaid.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleSpawner
{
    public static final int DISTANCE = 20;
    private static Minecraft mc = Minecraft.getMinecraft();

    @SideOnly(Side.CLIENT)
    public static Particle spawnParticle(EnumCustomParticles type, double par0, double par1, double par2, double par3, double par4, double par5)
    {
        return spawnParticle(type, par0, par1, par2, par3, par4, par5, 0, 0, 0);
    }
    @SideOnly(Side.CLIENT)
    public static Particle spawnParticle(EnumCustomParticles type, double par0, double par1, double par2, double par3, double par4, double par5, double par6, double par7, double par8)
    {
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            int var14 = mc.gameSettings.particleSetting;

            if (var14 == 1 && mc.world.rand.nextInt(3) == 0)
            {
                var14 = 2;
            }

            double var15 = mc.getRenderViewEntity().posX - par0;
            double var17 = mc.getRenderViewEntity().posY - par1;
            double var19 = mc.getRenderViewEntity().posZ - par2;
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
                var21 = getParticleFromEnumParticels(type, par0, par1, par2, par3, par4, par5, par6, par7, par8);
                mc.effectRenderer.addEffect(var21);
                return var21;
            }
        }
        return null;
    }

    private static Particle getParticleFromEnumParticels(EnumCustomParticles type, double d0, double d1, double d2, double d3, double d4, double d5, double d6, double d7, double d8)
    {
        switch (type) {
            case SOUL:
                return new SoulParticle(mc.world, d0, d1, d2);
            case CROSS:
                return new CrossParticle(mc.world, d0, d1, d2);
            case CONVICTION:
                return new ConvictionParticle(mc.world, d0, d1, d2, d3, d4, d5);
            case STAR:
                return new StarParticle(mc.world, d0, d1, d2);
            case PANDORA:
                return new PandoraParticle(mc.world, d0, d1, d2, d3, d4, d5);
            case WHISPER:
                return new WhisperParticle(mc.world, d0, d1, d2);
            case SWEEP:
                return new SweepAttackParticle(mc.world, d0, d1, d2, d3, (float) d4);
            case MAGIC:
                return new WispParticle(mc.world, d0, d1, d2, d3, d4, d5, d6, d7, d8);

            default: return new SoulParticle(mc.world, d0, d1, d2);
        }
    }
}
