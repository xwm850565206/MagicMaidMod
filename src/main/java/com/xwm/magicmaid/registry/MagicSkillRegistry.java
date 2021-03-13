package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.player.skill.attributeskill.*;
import com.xwm.magicmaid.player.skill.perfomskill.*;
import com.xwm.magicmaid.player.skill.perfomskill.normal.PerformSkillFireBall;
import com.xwm.magicmaid.player.skill.perfomskill.normal.PerformSkillJump;
import com.xwm.magicmaid.player.skill.perfomskill.normal.PerformSkillLightning;
import com.xwm.magicmaid.player.skill.perfomskill.normal.PerformSkillRepel;
import com.xwm.magicmaid.player.skill.perfomskill.rare.PerformSkillBoost;
import com.xwm.magicmaid.player.skill.perfomskill.rare.PerformSkillFireBallRain;
import com.xwm.magicmaid.player.skill.perfomskill.rare.PerformSkillLightningMove;
import com.xwm.magicmaid.player.skill.perfomskill.rare.PerformSkillWitcherStorm;
import com.xwm.magicmaid.player.skill.perfomskill.secret.PerformSkillFlash;
import com.xwm.magicmaid.player.skill.perfomskill.secret.PerformSkillSteal;
import com.xwm.magicmaid.player.skill.perfomskill.unreachable.PerformSkillWaterPrison;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册，查询等 todo
 */
public class MagicSkillRegistry
{
    /** 储存技能的表 **/
    public static final Map<String, Class<? extends ISkill>> SKILL_MAP = new HashMap<>();

    /** 储存赠送技能的表，第一项是modid，第二项是技能名 **/
    public static final Map<String, String> GIFTED_MAP = new HashMap<>();

    public static final ISkill ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE = new AttributeSkillNormalDamageRate();
    public static final ISkill ATTRIBUTE_SKILL_SKILL_DAMAGE_RATE = new AttributeSkillSkillDamageRate();
    public static final ISkill ATTRIBUTE_SKILL_MAX_ENERGY = new AttributeSkillMaxEnergy();
    public static final ISkill ATTRIBUTE_SKILL_SKILL_SPEED = new AttributeSkillSkillSpeed();
    public static final ISkill ATTRIBUTE_SKILL_INJURY_REDUCTION = new AttributeSkillInjuryReduction();
    public static final ISkill ATTRIBUTE_SKILL_IGNORE_REDUCTION = new AttributeSkillIgnoreReduction();

    public static final ISkill PERFORM_SKILL_NONE = new PerformSkillNone();
    public static final ISkill PERFORM_SKILL_REPEL = new PerformSkillRepel();
    public static final ISkill PERFORM_SKILL_JUMP = new PerformSkillJump();
    public static final ISkill PERFORM_SKILL_FIREBALL = new PerformSkillFireBall();
    public static final ISkill PERFORM_SKILL_LIGHTNING = new PerformSkillLightning();

    public static final ISkill PERFORM_SKILL_LIGHTNING_MOVE = new PerformSkillLightningMove();
    public static final ISkill PERFORM_SKILL_FIREBALL_RAIN = new PerformSkillFireBallRain();
    public static final ISkill PERFORM_SKILL_WITHER_STORM = new PerformSkillWitcherStorm();
    public static final ISkill PERFORM_SKILL_BOOST = new PerformSkillBoost();

    public static final ISkill PERFORM_SKILL_FLASH = new PerformSkillFlash();
    public static final ISkill PERFORM_SKILL_STEAL = new PerformSkillSteal();

    public static final ISkill PERFORM_SKILL_WATER_PRISON = new PerformSkillWaterPrison();


    public static void register(String name, Class<? extends ISkill> skill)
    {
        if (SKILL_MAP.containsKey(name)) {
            Main.logger.warn("register an already skill!");
        }

        SKILL_MAP.put(name, skill);
    }

    public static void registerGiftedSkill(String modid, String skillName)
    {
        if (SKILL_MAP.containsKey(modid)) {
            Main.logger.warn("register an already gifted mod!");
        }

        GIFTED_MAP.put(modid, skillName);
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
        // perform skill
        register(ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE.getName(), ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE.getClass());
        register(ATTRIBUTE_SKILL_SKILL_DAMAGE_RATE.getName(), ATTRIBUTE_SKILL_SKILL_DAMAGE_RATE.getClass());
        register(ATTRIBUTE_SKILL_MAX_ENERGY.getName(), ATTRIBUTE_SKILL_MAX_ENERGY.getClass());
        register(ATTRIBUTE_SKILL_INJURY_REDUCTION.getName(), ATTRIBUTE_SKILL_INJURY_REDUCTION.getClass());
        register(ATTRIBUTE_SKILL_IGNORE_REDUCTION.getName(), ATTRIBUTE_SKILL_IGNORE_REDUCTION.getClass());
        register(ATTRIBUTE_SKILL_SKILL_SPEED.getName(), ATTRIBUTE_SKILL_SKILL_SPEED.getClass());

        register(PERFORM_SKILL_NONE.getName(), PERFORM_SKILL_NONE.getClass());
        register(PERFORM_SKILL_REPEL.getName(), PERFORM_SKILL_REPEL.getClass());
        register(PERFORM_SKILL_JUMP.getName(), PERFORM_SKILL_JUMP.getClass());
        register(PERFORM_SKILL_FIREBALL.getName(), PERFORM_SKILL_FIREBALL.getClass());
        register(PERFORM_SKILL_LIGHTNING.getName(), PERFORM_SKILL_LIGHTNING.getClass());

        register(PERFORM_SKILL_LIGHTNING_MOVE.getName(), PERFORM_SKILL_LIGHTNING_MOVE.getClass());
        register(PERFORM_SKILL_FIREBALL_RAIN.getName(), PERFORM_SKILL_FIREBALL_RAIN.getClass());
        register(PERFORM_SKILL_WITHER_STORM.getName(), PERFORM_SKILL_WITHER_STORM.getClass());
        register(PERFORM_SKILL_BOOST.getName(), PERFORM_SKILL_BOOST.getClass());

        register(PERFORM_SKILL_FLASH.getName(), PERFORM_SKILL_FLASH.getClass());
        register(PERFORM_SKILL_STEAL.getName(), PERFORM_SKILL_STEAL.getClass());

        register(PERFORM_SKILL_WATER_PRISON.getName(), PERFORM_SKILL_WATER_PRISON.getClass());

        // gifted skill
        registerGiftedSkill("easy_building", PERFORM_SKILL_WATER_PRISON.getName());

        // todo
//        register(AttributeSkillNormalDamageRate.class);
    }
}
