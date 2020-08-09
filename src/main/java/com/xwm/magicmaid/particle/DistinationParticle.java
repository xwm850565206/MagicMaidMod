package com.xwm.magicmaid.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

import java.awt.*;

public abstract class DistinationParticle extends Particle
{
    private double dx = 0;
    private double dy = 0;
    private double dz = 0;

    private double tx;
    private double ty;
    private double tz;

    protected DistinationParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46349_8_, p_i46349_9_, p_i46349_10_);
        this.tx = xCoordIn;
        this.ty = yCoordIn;
        this.tz = zCoordIn;
    }

    protected DistinationParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double dx, double dy, double dz) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0, 0);

        this.particleMaxAge = 40;
        this.tx = dx;
        this.ty = dy;
        this.tz = dz;

        this.dx = (tx - xCoordIn) / particleMaxAge;
        this.dy = (ty - yCoordIn) / particleMaxAge;
        this.dz = (tz - zCoordIn) / particleMaxAge;

        this.bindTexture();
    }

    protected abstract void bindTexture();

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.setPosition(this.prevPosX + dx, this.prevPosY + dy, this.prevPosZ + dz);
    }


    @Override
    public int getFXLayer() {
        return 1;
    }
}
