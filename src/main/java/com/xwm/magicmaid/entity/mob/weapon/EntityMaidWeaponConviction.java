package com.xwm.magicmaid.entity.mob.weapon;

import com.xwm.magicmaid.network.CustomerParticlePacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class EntityMaidWeaponConviction extends EntityMaidWeapon
{
    private int tick = 0;
    private boolean gap = true;
    private double perAngle = 360 / 6;
    private double radius = 0.5;
    private double perHeight = height / 6;

    public EntityMaidWeaponConviction(World worldIn) {
        super(worldIn);
        enumEquipment = EnumEquipments.CONVICTION;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        gap = !gap;
        if (gap)
            return;
        this.tick++;
        if (tick > 12) tick = 0;

        int t = tick % 6;

        double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
        double d1 = this.getEntityBoundingBox().minY;
        double d2 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
        CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                d0 + radius * Math.sin(Math.toRadians(t * perAngle)),
                d1 + perHeight * t,
                d2 + radius * Math.cos(Math.toRadians(t * perAngle)), EnumCustomParticles.CROSS);
        NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(this.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
        NetworkLoader.instance.sendToAllAround(particlePacket, target);
    }
}