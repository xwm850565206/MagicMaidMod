package com.xwm.magicmaid.entity.throwable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityEvilBall extends EntityBossBall
{
    public EntityEvilBall(World worldIn) {
        super(worldIn);
    }

    public EntityEvilBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityEvilBall(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    protected int getCamp() {
        return 0;
    }
}
