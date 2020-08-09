package com.xwm.magicmaid.particle;

import com.xwm.magicmaid.init.TextureInit;
import net.minecraft.world.World;

import java.awt.*;

public class PandoraParticle extends DistinationParticle
{

    protected PandoraParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }

    protected PandoraParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double dx, double dy, double dz) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, dx, dy, dz);

        Color c;
        double f = rand.nextDouble();
        if (f < 0.25)
            c = new Color(255, 255, 255);
        else
            c = new Color(0, 0, 0);

        this.particleRed = c.getRed() / 255.0f;
        this.particleBlue = c.getBlue() / 255.0f;
        this.particleGreen = c.getGreen() / 255.0f;
    }

    @Override
    protected void bindTexture() {
        this.setParticleTexture(TextureInit.ATOM);
    }
}
