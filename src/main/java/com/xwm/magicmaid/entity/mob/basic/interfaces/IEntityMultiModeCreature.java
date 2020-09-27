package com.xwm.magicmaid.entity.mob.basic.interfaces;

/**
 * 让生物可以拥有多个模式和状态的接口
 */
public interface IEntityMultiModeCreature
{
    /**
     * 设置模式
     * @param mode
     */
    void setMode(int mode);

    /**
     * 得到当前模式
     * @return
     */
    int getMode();

    /**
     * 转换模式
     */
    void switchMode();

    /**
     * 得到当前状态
     * @return
     */
    int getState();

    /**
     * 设置当前状态
     * @param state
     */
    void setState(int state);

}
