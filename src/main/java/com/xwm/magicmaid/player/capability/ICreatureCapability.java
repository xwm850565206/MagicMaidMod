package com.xwm.magicmaid.player.capability;

import net.minecraft.entity.ai.attributes.AbstractAttributeMap;

public interface ICreatureCapability
{

    /**
     * 得到属性表
     * @return 属性表
     */
    AbstractAttributeMap getAttributeMap();

    /**
     * 普通攻击伤害率
     * @return 普通攻击伤害率
     */
    double getNormalDamageRate();

    /**
     * 技能攻击伤害率
     * @return 技能攻击伤害率
     */
    double getSkillDamageRate();

    /**
     * 当前能量值
     * @return 当前能量值
     */
    double getEnergy();

    /**
     * 最大的能量值(能量上限，对玩家来说是可储存的最大能量)
     * @return 最大能量值
     */
    double getMaxEnergy();

    /**
     * 每秒回复的能量
     * @return 每秒回复的能量
     */
    double getPerEnergy();


    /**
     * 技能冷却速度(对怪物来说，每秒回复的能量乘以技能冷却速度就是真正每秒回复能量。对玩家来说，这是技能冷却速度)
     * @return 技能冷却速度
     */
    double getSkillSpeed();

    /**
     * 减伤率
     * @return 减伤率
     */
    double getInjuryReduction();


    /**
     * 攻击时的无视减少率
     * @return 攻击时的无视减少率
     */
    double getIgnoreReduction();

    /**
     * 设置属性表
     * @param attributeMap 属性表
     */
    void setAttributeMap(AbstractAttributeMap attributeMap);

    /**
     * @param normalDamageRate 普通攻击伤害率
     */
    void setNormalDamageRate(double normalDamageRate);

    /**
     * @param skillDamageRate  技能攻击伤害率
     */
    void setSkillDamageRate(double skillDamageRate);

    /**
     * @param energy  当前能量值
     */
    void setEnergy(double energy);

    /**
     * 最大的能量值(能量上限，对玩家来说是可储存的最大能量)
     * @param maxEnergy  最大能量值
     */
    void setMaxEnergy(double maxEnergy);

    /**
     * @param perEnergy 每秒回复的能量
     */
    void setPerEnergy(double perEnergy);

    /**
     * @param skillSpeed 技能冷却速度(对怪物来说，每秒回复的能量乘以技能冷却速度就是真正每秒回复能量。对玩家来说，这是技能冷却速度)
     */
    void setSkillSpeed(double skillSpeed);

    /**
     * @param injuryReduction  减伤率
     */
    void setInjuryReduction(double injuryReduction);

    /**
     * @param ignoreReduction  攻击时的无视减少率
     */
    void setIgnoreReduction(double ignoreReduction);
}
