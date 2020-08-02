package com.xwm.magicmaid.particle;

import com.xwm.magicmaid.init.TextureInit;
import net.minecraft.world.World;

public class ConvictionParticle extends MoveUpParticle
{

    protected ConvictionParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
        this.particleMaxAge = 20;
    }

    @Override
    protected void bindTexture() {
        this.setParticleTexture(TextureInit.CONVICTION);
    }
}
