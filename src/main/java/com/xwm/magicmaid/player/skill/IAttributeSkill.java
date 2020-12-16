package com.xwm.magicmaid.player.skill;

import net.minecraft.entity.EntityLivingBase;

/**
 * 数值技能，一般用于玩家的基本属性提升
 */
public interface IAttributeSkill extends ISkill
{
    /**
     * 修改自身的某种属性
     * @param player 技能持有者
     */
    void perform(EntityLivingBase player);
}
