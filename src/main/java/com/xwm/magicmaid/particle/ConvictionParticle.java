package com.xwm.magicmaid.particle;

import com.xwm.magicmaid.init.TextureInit;
import net.minecraft.world.World;

public class ConvictionParticle extends MoveUpParticle
{
    protected ConvictionParticle(World worldIn, double posXIn, double posYIn, double posZIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, posXIn, posYIn, posZIn);
        this.particleMaxAge = 20;
        if (xSpeedIn != 0 || ySpeedIn != 0 || zSpeedIn != 0) {
            this.motionX = xSpeedIn;
            this.motionY = ySpeedIn;
            this.motionZ = zSpeedIn;
        }
    }

    @Override
    protected void bindTexture() {
        this.setParticleTexture(TextureInit.CONVICTION);
    }
}
