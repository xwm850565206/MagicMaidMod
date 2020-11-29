package com.xwm.magicmaid.entity.throwable;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAvoidThingCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityBossCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityEquipmentCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityMultiHealthCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMarthaBoss;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRettBoss;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelinaBoss;
import com.xwm.magicmaid.util.helper.MagicCreatureUtils;
import com.xwm.magicmaid.util.helper.MagicEquipmentUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class EntityBossBall extends EntityThrowable
{
    public EntityBossBall(World worldIn) {
        super(worldIn);
    }

    public EntityBossBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityBossBall(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    protected abstract int getCamp();

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for (int i = 0; i < 8; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }


    /**
     * Called when this EntityThrowable hits a block or entity.
     *
     * @param result
     */
    @Override
    protected void onImpact(RayTraceResult result) {

        BlockPos pos = null;
        switch (result.typeOfHit)  {
            case MISS: return;
            case ENTITY:
                pos = result.entityHit.getPosition();
                break;
            case BLOCK:
                pos = result.getBlockPos();
                break;
        }
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).grow(1));
        for (Entity entity : entities)
        {
            try {
                impactEntity(entity);
            } catch (Exception e) {
                ; //遇到攻击失效的就算了
            }
        }

        if (!world.isRemote) {
            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }
    }

    public void impactEntity(Entity entity)
    {
        if (entity == null)
            return;

        if (entity instanceof EntityMob) {
            entity.attackEntityFrom(DamageSource.causeMobDamage(thrower), 100);
        }

        int camp;
        if (entity instanceof IEntityBossCreature)
        {
            entity.attackEntityFrom(DamageSource.causeMobDamage(thrower), 1);
            camp = ((IEntityBossCreature) entity).getBossCamp();
            if (camp == getCamp())
            {
                if (entity instanceof IEntityMultiHealthCreature)
                {
                    fixProblem((IEntityMultiHealthCreature) entity);

                    int f = ((IEntityMultiHealthCreature) entity).getHealthBarNum();
                    int f1 = ((IEntityMultiHealthCreature) entity).getMaxHealthBarnum();

                    //减少10%的血条
                    f = f - f1 / 4;
                    if (f >= 0) {
                        ((IEntityMultiHealthCreature) entity).setHealthbarnum(f);
                    }
                    else
                    {
                        ((IEntityMultiHealthCreature) entity).setHealthbarnum(0);
                        if (entity instanceof IEntityAvoidThingCreature)
                        {
                            MagicCreatureUtils.unLock((IEntityAvoidThingCreature) entity);
                            entity.attackEntityFrom(DamageSource.causeMobDamage(thrower), 100); //默认所有生物的最大生命值是100
                            MagicCreatureUtils.lock((IEntityAvoidThingCreature) entity);
                        }
                    }
                }
            }

            if (entity instanceof IEntityEquipmentCreature)
            {
                MagicEquipmentUtils.dropEquipment(((IEntityEquipmentCreature) entity).getWeaponType(), 1, world, getPosition());
                MagicEquipmentUtils.dropEquipment(((IEntityEquipmentCreature) entity).getArmorType(), 1, world, getPosition());
            }
        }
    }

    //解决之前血条最大值没有写进nbt造成的问题 version-6.0
    private boolean fixProblem(IEntityMultiHealthCreature creature) {
        int f = creature.getHealthBarNum();
        int f1 = creature.getMaxHealthBarnum();

        if (f1 < 200)
        {
            if (creature instanceof EntityMagicMaidMarthaBoss)
                creature.setMaxHealthbarnum(200);
            else if (creature instanceof EntityMagicMaidRettBoss)
                creature.setMaxHealthbarnum(1000);
            else if (creature instanceof EntityMagicMaidSelinaBoss)
                creature.setMaxHealthbarnum(200);
            return true;
        }
        else
            return false;
    }
}
