package com.xwm.magicmaid.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EffectBase extends Entity
{
    public EffectBase(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {

    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     *
     * @param compound
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     *
     * @param compound
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

}
