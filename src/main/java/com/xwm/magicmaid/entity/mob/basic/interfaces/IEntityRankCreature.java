package com.xwm.magicmaid.entity.mob.basic.interfaces;

public interface IEntityRankCreature
{
    /**
     * 设置经验值
     * @param exp
     */
    void setExp(int exp);

    /**
     * 得到经验值
     * @return
     */
    int getExp();

    /**
     * 增加经验
     */
    void plusExp();

    /**
     * 设置等级
     * @param level
     */
    void setLevel(int level);

    /**
     * 得到等级
     * @return
     */
    int getLevel();

    /**
     * 设置品阶
     * @param rank
     */
    void setRank(int rank);

    /**
     * 得到品阶
     * @return
     */
    int getRank();

    /**
     * 升阶
     */
    void plusRank();
}
