package com.xwm.magicmaid.particle;

import com.xwm.magicmaid.init.TextureInit;
import net.minecraft.world.World;

import java.awt.*;

public class WhisperParticle extends MoveUpParticle
{

    protected WhisperParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
        Color c;
        double f = rand.nextDouble();
        if (f < 0.1)
            c =  new Color(255, 88, 0);
        else
            c =  new Color(255, 188, 0);

        this.particleMaxAge = 20;
        this.particleRed = c.getRed() / 255.0f;
        this.particleBlue = c.getBlue() / 255.0f;
        this.particleGreen = c.getGreen() / 255.0f;
    }


    @Override
    protected void bindTexture() {
        this.setParticleTexture(TextureInit.ATOM);
    }
}
