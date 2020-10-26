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
}
