package com.xwm.magicmaid.util.helper;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAvoidThingCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityMultiHealthCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class MagicCreatureUtils
{
    public static void setDead(IEntityAvoidThingCreature creature)
    {
        creature.setAvoidDamage(-1);
        creature.setAvoidSetHealth(-1);
        if (creature instanceof IEntityMultiHealthCreature)
        {
            ((IEntityMultiHealthCreature) creature).setHealthbarnum(0);
        }
        if (creature instanceof EntityLivingBase){
            ((EntityLivingBase) creature).setHealth(0);
        }
        else if (creature instanceof Entity){
            ((Entity) creature).setDead();
        }
    }

    public static void lock(IEntityAvoidThingCreature creature, int amount)
    {
        creature.setAvoidDamage(amount);
        creature.setAvoidSetHealth(amount);
    }

    public static void lock(IEntityAvoidThingCreature creature)
    {
        creature.setAvoidDamage(50);
        creature.setAvoidSetHealth(50);
    }

    public static void unLock(IEntityAvoidThingCreature creature)
    {
        creature.setAvoidDamage(-1);
        creature.setAvoidSetHealth(-1);
    }
}
