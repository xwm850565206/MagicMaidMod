package com.xwm.magicmaid.player.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 主动技能
 */
public interface IPerformSkill extends ISkill, ITickable
{
    /**
     * 得到施法需要的能量
     * @return 能量
     */
    int getPerformEnergy();

    /**
     * 技能冷却
     * @return 冷却时间
     */
    int getColdTime();

    /**
     * 当前技能已经冷却的时间
     * @return 已经冷却的时间 当已经冷却的时间降为0时，冷却完毕
     */
    int getCurColdTime();

    /**
     * 设置当前技能已经冷却的时间
     * @param curColdTime 当前技能已经冷却的时间
     */
    void setCurColdTime(int curColdTime);


    /**
     * 释放技能
     * @param playerIn 释放者
     * @param worldIn 释放技能的世界
     * @param posIn 释放技能的位置
     */
    void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn);
}
