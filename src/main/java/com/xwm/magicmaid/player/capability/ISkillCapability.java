package com.xwm.magicmaid.player.capability;

import com.xwm.magicmaid.player.skill.IAttributeSkill;
import com.xwm.magicmaid.player.skill.IPassiveSkill;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.NonNullList;

import java.util.Collection;
import java.util.Map;

public interface ISkillCapability
{
    /**
     * 得到当前的技能点数(用于升级技能)
     * @return 当前技能点数
     */
    int getSkillPoint();

    /**
     * 因为需要得到技能点，在ui里面要有个功能，然后东西储存在这里
     * @return 储存槽
     */
    IInventory getSkillPointInventory();

    /**
     * 得到全部attribute skill
     * @return
     */
    Collection<IAttributeSkill> getAttributeSkills();

    /**
     * 得到全部passive skill
     * @return
     */
    Collection<IPassiveSkill> getPassiveSkills();

    /**
     * 得到全部perform skill
     * @return
     */
    Collection<IPerformSkill> getPerformSkills();

    /**
     * 根据技能名称得到attribute技能
     * @param name 技能名称
     * @return 技能
     */
    IAttributeSkill getAttributeSkill(String name);

    /**
     * 根据技能名称得到passive技能
     * @param name 技能名称
     * @return 技能
     */
    IPassiveSkill getPassiveSkill(String name);

    /**
     * 根据技能名称得到perform技能
     * @param name 技能名称
     * @return 技能
     */
    IPerformSkill getPerformSkill(String name);

    /**
     * 玩家目前使用的主动技能列表
     * @return 主动技能列表
     */
    NonNullList<IPerformSkill> getActivePerformSkills();

    /**
     * 得到玩家目前使用的主动技能列表中第index个技能
     * @param index 主动技能的序号
     * @return 主动技能
     */
    IPerformSkill getActivePerformSkill(int index);

    Map<String, IAttributeSkill> getAtributeSkillMap();

    Map<String, IPassiveSkill> getPassiveSkillMap();

    Map<String, IPerformSkill> getPerformSkillMap();

    /**
     * 设置当前技能点数
     * @param skillPoint 技能点数
     */
    void setSkillPoint(int skillPoint);

    /**
     * 设置inventory
     * @param inventory inventory
     */
    void setSkillPointInventory(IInventory inventory);

    /**
     * 设置技能，可以更新，或者是添加新的技能
     * @param name 技能名称
     * @param attributeSkill 技能
     */
    void setAttributeSkill(String name, IAttributeSkill attributeSkill);

    /**
     * 设置技能，可以更新，或者是添加新的技能
     * @param name 技能名称
     * @param passiveSkill 技能
     */
    void setPassiveSkill(String name, IPassiveSkill passiveSkill);

    /**
     * 设置技能，可以更新，或者是添加新的技能
     * @param name 技能名称
     * @param performSkill 技能
     */
    void setPerformSkill(String name, IPerformSkill performSkill);

    /**
     * 设置玩家目前使用的第index个主动技能为performSkill
     * @param index 序号
     * @param performSkill 要设置的技能
     */
    void setActivePerformSkill(int index, IPerformSkill performSkill);

    void setAttributeSkills(Map<String, IAttributeSkill> attributeSkillMap);

    void setPassiveSkills(Map<String, IPassiveSkill> passiveSkillMap);

    void setPerformSkills(Map<String, IPerformSkill> performSkillMap);

    void setActivePerformSkills(NonNullList<IPerformSkill> activePerformSkill);

    /**
     * 将自身数据设置为传入的skillCapability
     * @param other 从这个参数中得到数据
     */
    void fromSkillCapability(ISkillCapability other);
}
