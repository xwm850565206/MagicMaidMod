package com.xwm.magicmaid.entity.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.swing.text.html.parser.Entity;

public class EffectThrowableBase extends EntityThrowable
{
    public EffectThrowableBase(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    public EffectThrowableBase(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EffectThrowableBase(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     *
     * @param result
     */
    @Override
    protected void onImpact(RayTraceResult result) {
        System.out.println("on impact " + this.ticksExisted); //todo
    }

    protected float getGravityVelocity()
    {
        return 0.00F;
    }
}
