package com.xwm.magicmaid.particle;

import com.xwm.magicmaid.init.TextureInit;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class WispParticle extends Particle
{
    protected WispParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
    }

    public WispParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, double red, double green, double blue) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.particleRed = (float) red;
        this.particleGreen = (float) green;
        this.particleBlue = (float) blue;
        this.particleAlpha = 0.65f;
        this.particleScale = 0.75f;
        this.particleMaxAge = 100;
        this.setParticleTexture(TextureInit.WISP);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
