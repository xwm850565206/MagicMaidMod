package com.xwm.magicmaid.player;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class MagicCreatureAttributes
{
    public static final IAttribute NORMAL_DAMAGE_RATE = new RangedAttribute(null, "magic_maid.creature.normal_damage_rate", 1.0D, 0.0D,
            10000.0D).setDescription("Normal Damage Rate").setShouldWatch(true); // 普通攻击伤害率

    public static final IAttribute SKILL_DAMAGE_RATE = new RangedAttribute(null, "magic_maid.creature.skill_damage_rate", 1.0D, 0.0D,
            10000.0D).setDescription("Skill Damage Rate").setShouldWatch(true); // 技能攻击伤害率

    public static final IAttribute ENERGY = new RangedAttribute(null, "magic_maid.creature.energy", 0.0D, 0.0D,
            10000.0D).setDescription("energy").setShouldWatch(true); // 能量

    public static final IAttribute PER_ENERGY = new RangedAttribute(null, "magic_maid.creature.per_energy", 1.0D, 0.0D,
            10000.0D).setDescription("Per Energy").setShouldWatch(true); // 一次获得能量数

    public static final IAttribute MAX_ENERGY = new RangedAttribute(null, "magic_maid.creature.max_energy", 200.0D, 1.0D,
            10000.0D).setDescription("Max Energy").setShouldWatch(true); // 最大能量数

    public static final IAttribute SKILL_SPEED = new RangedAttribute(null, "magic_maid.creature.skill_speed", 1.0D, 0.0D,
            10000.0D).setDescription("Skill Speed").setShouldWatch(true); // 技能充能速度 energy = energy + skill_speed * per_energy

    public static final IAttribute INJURY_REDUCTION = new RangedAttribute(null, "magic_maid.creature.injury_reduction", 0.0D, 0.0D,
            1.0D).setDescription("Injury reduction").setShouldWatch(true); // 减伤率

    public static final IAttribute IGNORE_REDUCTION = new RangedAttribute(null, "magic_maid.creature.ignore_reduction", 0.0D, 0.0D,
            1.0D).setDescription("Ignore reduction").setShouldWatch(true); // 无视减伤率 damage = (damage_rate * damage) * (1 - (injury_reduction - ignore_reduction))


}
