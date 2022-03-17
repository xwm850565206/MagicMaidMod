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
import com.xwm.magicmaid.player.skill.perfomskill.secret.PerformProcessSkillMercy;
import com.xwm.magicmaid.player.skill.perfomskill.secret.PerformSkillFlash;
import com.xwm.magicmaid.player.skill.perfomskill.secret.PerformSkillSteal;
import com.xwm.magicmaid.player.skill.perfomskill.unreachable.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /** 储存武器专精技能的数组 **/
    public static final List<String> WEAPON_SKILL_MAP = new ArrayList<>();

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
    public static final ISkill PERFORM_SKILL_MERCY = new PerformProcessSkillMercy();

    public static final ISkill PERFORM_SKILL_WATER_PRISON = new PerformSkillWaterPrison();

    public static final ISkill PERFORM_SKILL_DEMON_KILLER = new PerformSkillDemonKiller();
    public static final ISkill PERFORM_SKILL_REPENTANCE = new PerformSkillRepentance();
    public static final ISkill PERFORM_SKILL_PANDORA = new PerformSkillPandora();
    public static final ISkill PERFORM_SKILL_CONVICTION = new PerformSkillConviction();
    public static final ISkill PERFORM_SKILL_WHISPER = new PerformSkillWhisper();

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

    public static void registerWeaponSkill(String name)
    {
        if (!SKILL_MAP.containsKey(name)) {
            Main.logger.warn("Not registered skill in skill map! bad weapon skill");
        }
        else {
            WEAPON_SKILL_MAP.add(name);
        }
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
        // attribute skill
        register(ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE.getName(), ATTRIBUTE_SKILL_NORMAL_DAMAGE_RATE.getClass());
        register(ATTRIBUTE_SKILL_SKILL_DAMAGE_RATE.getName(), ATTRIBUTE_SKILL_SKILL_DAMAGE_RATE.getClass());
        register(ATTRIBUTE_SKILL_MAX_ENERGY.getName(), ATTRIBUTE_SKILL_MAX_ENERGY.getClass());
        register(ATTRIBUTE_SKILL_INJURY_REDUCTION.getName(), ATTRIBUTE_SKILL_INJURY_REDUCTION.getClass());
        register(ATTRIBUTE_SKILL_IGNORE_REDUCTION.getName(), ATTRIBUTE_SKILL_IGNORE_REDUCTION.getClass());
        register(ATTRIBUTE_SKILL_SKILL_SPEED.getName(), ATTRIBUTE_SKILL_SKILL_SPEED.getClass());

        // perform skill
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
        register(PERFORM_SKILL_MERCY.getName(), PERFORM_SKILL_MERCY.getClass());

        register(PERFORM_SKILL_DEMON_KILLER.getName(), PERFORM_SKILL_DEMON_KILLER.getClass());
        register(PERFORM_SKILL_REPENTANCE.getName(), PERFORM_SKILL_REPENTANCE.getClass());
        register(PERFORM_SKILL_PANDORA.getName(), PERFORM_SKILL_PANDORA.getClass());
        register(PERFORM_SKILL_CONVICTION.getName(), PERFORM_SKILL_CONVICTION.getClass());
        register(PERFORM_SKILL_WHISPER.getName(), PERFORM_SKILL_WHISPER.getClass());

        register(PERFORM_SKILL_WATER_PRISON.getName(), PERFORM_SKILL_WATER_PRISON.getClass());


        // gifted skill
        registerGiftedSkill("easy_building", PERFORM_SKILL_WATER_PRISON.getName());
//        registerGiftedSkill("minecraft", PERFORM_SKILL_DEMON_KILLER.getName());
//        registerGiftedSkill("minecraft", PERFORM_SKILL_REPENTANCE.getName());
//        registerGiftedSkill("minecraft", PERFORM_SKILL_PANDORA.getName());
//        registerGiftedSkill("minecraft", PERFORM_SKILL_WHISPER.getName());

        // weapon skill
        registerWeaponSkill(PERFORM_SKILL_DEMON_KILLER.getName());
        registerWeaponSkill(PERFORM_SKILL_REPENTANCE.getName());
        registerWeaponSkill(PERFORM_SKILL_PANDORA.getName());
        registerWeaponSkill(PERFORM_SKILL_CONVICTION.getName());
        registerWeaponSkill(PERFORM_SKILL_WHISPER.getName());

        // todo
//        register(AttributeSkillNormalDamageRate.class);
    }
}
