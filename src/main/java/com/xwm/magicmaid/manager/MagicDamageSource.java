package com.xwm.magicmaid.manager;

import net.minecraft.util.DamageSource;

/**
 * 枚举常用的伤害源
 */
public class MagicDamageSource extends DamageSource
{
    // magic_maid
    public static final DamageSource DEATH_IMMEDIATELY = new MagicDamageSource("death_immediately").setDeathImmediately();
    public static final DamageSource IGNORE_REDUCTION = new MagicDamageSource("ignore_reduction").setIgnoreReduction();

    /** 立刻死亡 **/
    private boolean isDeathImmediately;

    /** 真实伤害 **/
    private boolean isIgnoreReduction;

    public MagicDamageSource(String damageTypeIn) {
        super(damageTypeIn);
    }

    public boolean isDeathImmediately() {
        return isDeathImmediately;
    }

    public MagicDamageSource setDeathImmediately() {
        isDeathImmediately = true;
        return this;
    }

    public boolean isIgnoreReduction() {
        return isIgnoreReduction;
    }

    public MagicDamageSource setIgnoreReduction() {
        isIgnoreReduction = true;
        return this;
    }
}
