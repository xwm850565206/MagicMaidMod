package com.xwm.magicmaid.entity.mob.weapon;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import net.minecraft.world.World;

public class EntityMaidWeaponRepantence extends EntityMaidWeapon
{
    private int tick = 0;
    private boolean gap = true;
    private double perAngle = 360 / 12;
    private double radius = 1;
    private double perHeight = height / 12;

    public EntityMaidWeaponRepantence(World worldIn) {
        super(worldIn);
        enumEquipment = EnumEquipment.REPATENCE;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!world.isRemote)
            return;

        gap = !gap;
        if (gap)
            return;
        this.tick++;
        if (tick > 24) tick = 0;

        int t = tick % 12;

        double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
        double d1 = this.getEntityBoundingBox().minY;
        double d2 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;

        ParticleSpawner.spawnParticle(EnumCustomParticles.SOUL, d0 + radius * Math.sin(Math.toRadians(t * perAngle)), d1 + perHeight * t, d2 + radius * Math.cos(Math.toRadians(t * perAngle)), 0, 0, 0);
    }
}
