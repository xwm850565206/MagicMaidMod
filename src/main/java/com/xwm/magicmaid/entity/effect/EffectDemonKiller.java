package com.xwm.magicmaid.entity.effect;

import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.particle.SPacketParticle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class EffectDemonKiller extends EntityThrowable
{
    public static ItemStack DEMON_KILLER = new ItemStack(ItemInit.ITEM_DEMON_KILLER_SWORD);

    private EntityLivingBase owner;
    private static final DataParameter<Integer> LEVEL = EntityDataManager.<Integer>createKey(EffectDemonKiller.class, DataSerializers.VARINT);

    public EffectDemonKiller(World worldIn) {
        super(worldIn);
        this.motionY = -5;
    }

    public EffectDemonKiller(World world, EntityLivingBase owner, int level) {
        this(world);
        this.owner = owner;
        this.setLevel(level);
        this.setSize(0.85f*4*getLevel(), 1.0f*4*getLevel());
    }

    public void setLevel(int level)
    {
        this.getDataManager().set(LEVEL, level);
    }

    public int getLevel() {
        return this.getDataManager().get(LEVEL);
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(LEVEL, 0);
    }

    @Override
    public void onUpdate() {
        this.setSize(0.85f*2*getLevel(), 1.0f*2*getLevel());
        super.onUpdate();
    }

    @Override
    protected void onImpact(RayTraceResult result) {

        if (result.entityHit != null && result.entityHit.equals(owner))
            return;

        if (!world.isRemote) {
            int n = getLevel() * 12;
            int r = getLevel() * 4;
            double perAngle = 360.0 / n;
            for (int i = 0; i < n; i++) {
                SPacketParticle packet = new SPacketParticle(posX + r * Math.sin(perAngle * i), posY, posZ + r * Math.cos(perAngle * i), EnumParticleTypes.CLOUD);
                NetworkLoader.instance.sendToAllAround(packet, new NetworkRegistry.TargetPoint(dimension, posX, posY, posZ, r * 4));
            }
            this.setDead();
        }
    }
}
