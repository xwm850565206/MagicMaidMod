package com.xwm.magicmaid.player.capability;

import com.xwm.magicmaid.player.skill.IAttributeSkill;
import com.xwm.magicmaid.player.skill.IPassiveSkill;
import com.xwm.magicmaid.player.skill.IPerformSkill;

import java.util.Collection;

public interface ISkillCapability
{
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
}
