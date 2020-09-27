package com.xwm.magicmaid.entity.mob.basic.interfaces;

import net.minecraft.util.DamageSource;

/**
 * 生物防止意外伤害或者意外操作的接口
 */
public interface IEntityAvoidThingCreature
{
    boolean shouldAvoidSetHealth(int healthnum);

    boolean shouldAvoidDamage(int damage, DamageSource source);

    void setAvoidSetHealth(int avoid);

    int getAvoidSetHealth();

    void setAvoidDamage(int avoid);

    int getAvoidDamage();
}
