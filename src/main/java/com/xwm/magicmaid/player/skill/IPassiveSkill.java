package com.xwm.magicmaid.player.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 被动技能
 */
public interface IPassiveSkill extends ISkill
{
    /**
     * 触发技能
     * @param playerIn 使用者
     * @param worldIn 使用技能的世界
     * @param posIn 使用技能的位置
     */
    void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn);
}
