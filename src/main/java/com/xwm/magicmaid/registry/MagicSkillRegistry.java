package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.player.skill.attributeskill.*;
import com.xwm.magicmaid.player.skill.perfomskill.PerformSkillBoost;
import com.xwm.magicmaid.player.skill.perfomskill.PerformSkillFlash;
import com.xwm.magicmaid.player.skill.perfomskill.PerformSkillNone;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册，查询等 todo
 */
public class MagicSkillRegistry
{
    public static final Map<String, Class<? extends ISkill>> SKILL_MAP = new HashMap<>();

    public static final ISkill ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE = new AttributeSkillNormalDamageRate();
    public static final ISkill ATTRIBUTE_SKILL_SKILL_DAMAGE_RATE = new AttributeSkillSkillDamageRate();
    public static final ISkill ATTRIBUTE_SKILL_MAX_ENERGY = new AttributeSkillMaxEnergy();
    public static final ISkill ATTRIBUTE_SKILL_SKILL_SPEED = new AttributeSkillSkillSpeed();
    public static final ISkill ATTRIBUTE_SKILL_INJURY_REDUCTION = new AttributeSkillInjuryReduction();
    public static final ISkill ATTRIBUTE_SKILL_IGNORE_REDUCTION = new AttributeSkillInjuryReduction();

    public static final ISkill PERFORM_SKILL_NONE = new PerformSkillNone();
    public static final ISkill PERFORM_SKILL_FLASH = new PerformSkillFlash();
    public static final ISkill PERFORM_SKILL_BOOST = new PerformSkillBoost();

    public static void register(String name, Class<? extends ISkill> skill)
    {
        if (SKILL_MAP.containsKey(name)) {
            Main.logger.warn("register an already skill!");
        }

        SKILL_MAP.put(name, skill);
    }

    public static ISkill getSkill(String name)
    {
        try {
            return SKILL_MAP.get(name).newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static void registerAll()
    {
        register(ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE.getName(), ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE.getClass());
        register(ATTRIBUTE_SKILL_SKILL_DAMAGE_RATE.getName(), ATTRIBUTE_SKILL_SKILL_DAMAGE_RATE.getClass());
        register(ATTRIBUTE_SKILL_MAX_ENERGY.getName(), ATTRIBUTE_SKILL_MAX_ENERGY.getClass());
        register(ATTRIBUTE_SKILL_INJURY_REDUCTION.getName(), ATTRIBUTE_SKILL_INJURY_REDUCTION.getClass());
        register(ATTRIBUTE_SKILL_IGNORE_REDUCTION.getName(), ATTRIBUTE_SKILL_IGNORE_REDUCTION.getClass());
        register(ATTRIBUTE_SKILL_SKILL_SPEED.getName(), ATTRIBUTE_SKILL_SKILL_SPEED.getClass());

        register(PERFORM_SKILL_NONE.getName(), PERFORM_SKILL_NONE.getClass());
        register(PERFORM_SKILL_FLASH.getName(), PERFORM_SKILL_FLASH.getClass());
        register(PERFORM_SKILL_BOOST.getName(), PERFORM_SKILL_BOOST.getClass());
        // todo
//        register(AttributeSkillNormalDamageRate.class);
    }
}
