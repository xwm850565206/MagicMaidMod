package com.xwm.magicmaid.particle;

import com.xwm.magicmaid.init.TextureInit;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.world.World;

import java.awt.*;

public class SoulParticle extends MoveUpParticle
{

    protected SoulParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn, 0, 0, 0);
    }

    public SoulParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    @Override
    protected void bindTexture() {
        this.setParticleTexture(TextureInit.SOUL);
    }


}
