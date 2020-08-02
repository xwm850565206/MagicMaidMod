package com.xwm.magicmaid.entity.mob.weapon;

import com.xwm.magicmaid.event.NetworkLoader;
import com.xwm.magicmaid.util.ParticlePacket;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class EntityMaidWeaponRepantence extends EntityMaidWeapon
{
    private int tick = 0;
    private boolean gap = true;
    private double perAngle = 360 / 12;
    private double radius = 1;
    private double perHeight = height / 12;

    public EntityMaidWeaponRepantence(World worldIn) {
        super(worldIn);
        enumEquipment = EnumEquipments.REPATENCE;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        gap = !gap;
        if (gap)
            return;
        this.tick++;
        if (tick > 24) tick = 0;

        int t = tick % 12;

        double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
        double d1 = this.getEntityBoundingBox().minY;
        double d2 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
        ParticlePacket particlePacket = new ParticlePacket(
                d0 + radius * Math.sin(Math.toRadians(t * perAngle)),
                d1 + perHeight * t,
                d2 + radius * Math.cos(Math.toRadians(t * perAngle)), EnumParticleTypes.SMOKE_LARGE);
        NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(this.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
        NetworkLoader.instance.sendToAllAround(particlePacket, target);

    }
}
