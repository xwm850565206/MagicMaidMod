package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.player.skill.attributeskill.AttributeSkillNormalDamageRate;
import com.xwm.magicmaid.player.skill.attributeskill.AttributeSkillTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册，查询等 todo
 */
public class MagicSkillRegistry
{
    public static final Map<String, Class<ISkill>> SKILL_MAP = new HashMap<>();
    public static final ISkill SKILL_TEST = new AttributeSkillTest();
    public static final ISkill ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE = new AttributeSkillNormalDamageRate();

    public static void register(Class<ISkill> skill)
    {
        if (SKILL_MAP.containsKey(skill.getName())) {
            Main.logger.warn("register an already skill!");
        }

        SKILL_MAP.put(skill.getName(), skill);
    }

    public static ISkill getSkill(String name)
    {
        try {
            return SKILL_MAP.get(name).newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static void registerAll()
    {
        // todo
//        register(AttributeSkillTest.class);
//        register(AttributeSkillNormalDamageRate.class);
    }
}
