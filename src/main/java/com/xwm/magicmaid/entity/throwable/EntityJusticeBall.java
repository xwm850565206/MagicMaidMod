package com.xwm.magicmaid.entity.throwable;

import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityJusticeBall extends EntityBossBall
{
    private double perAngle = 36;
    private double radius = 2;

    public EntityJusticeBall(World worldIn) {
        super(worldIn);
    }

    public EntityJusticeBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityJusticeBall(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    protected int getCamp() {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for (int i = 0; i < 10; ++i)
            {
                for (int j = 0; j < 4; j++) {
                    ParticleSpawner.spawnParticle(EnumCustomParticles.CONVICTION,
                            posX + radius * Math.sin(Math.toRadians(i * perAngle)),
                            posY + j * 0.4,
                            posZ + radius * Math.cos(Math.toRadians(i * perAngle)), 0, 0.3, 0);
                }
            }
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        ParticleSpawner.spawnParticle(EnumCustomParticles.CROSS, posX, posY, posZ, 0,0, 0);
    }
}
