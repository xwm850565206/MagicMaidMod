package com.xwm.magicmaid.entity.ai;

import com.google.common.base.Predicate;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityAINearestAttackableTargetAvoidOwner<T extends EntityLivingBase> extends EntityAIBase
{
    private EntityMagicMaid commander; //如果是selina造的蝙蝠就需要这个变量

    /** The entity that this task belongs to */
    protected final EntityLiving taskOwner;
    /** If true, EntityAI targets must be able to be seen (cannot be blocked by walls) to be suitable targets. */
    protected boolean shouldCheckSight;
    /** When true, only entities that can be reached with minimal effort will be targetted. */
    private final boolean nearbyOnly;
    /** When nearbyOnly is true: 0 -> No target, but OK to search; 1 -> Nearby target found; 2 -> Target too far. */
    private int targetSearchStatus;
    /** When nearbyOnly is true, this throttles target searching to avoid excessive pathfinding. */
    private int targetSearchDelay;
    /**
     * If  @shouldCheckSight is true, the number of ticks before the interuption of this AITastk when the entity does't
     * see the target
     */
    private int targetUnseenTicks;
    protected EntityLivingBase target;
    protected int unseenMemoryTicks;

    protected final Class<T> targetClass;
    private final int targetChance;
    /** Instance of EntityAINearestAttackableTargetSorter. */
    protected final Sorter sorter;
    protected EntityLivingBase targetEntity;


    public EntityAINearestAttackableTargetAvoidOwner(EntityLiving creature, EntityMagicMaid maid, Class classTarget, boolean checkSight) {
        this.unseenMemoryTicks = 60;
        this.taskOwner = creature;
        this.shouldCheckSight = checkSight;
        this.nearbyOnly = false;
        this.targetClass = classTarget;
        this.targetChance = 10;
        this.sorter = new Sorter(creature);
        this.setMutexBits(1);
        this.commander = maid;
    }

    @Override
    public boolean shouldExecute()
    {
        List<EntityLivingBase> list = this.taskOwner.world.<EntityLivingBase>getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()));

        if (list.isEmpty()) {
            return false;
        }
        else {
            Collections.sort(list, this.sorter);
            for(EntityLivingBase entity : list) {
                if (taskOwner instanceof EntityMagicMaid) {
                    if (((EntityMagicMaid) taskOwner).isEnemy(entity)) {
                        this.targetEntity = entity;
                        return true;
                    }
                } else {
                    if (commander.isEnemy(entity)) {
                        this.targetEntity = entity;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    protected double getTargetDistance()
    {
        IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }

    public static class Sorter implements Comparator<Entity>
    {
        private final Entity entity;

        public Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double d0 = this.entity.getDistanceSq(p_compare_1_);
            double d1 = this.entity.getDistanceSq(p_compare_2_);

            if (d0 < d1)
            {
                return -1;
            }
            else
            {
                return d0 > d1 ? 1 : 0;
            }
        }
    }
}
