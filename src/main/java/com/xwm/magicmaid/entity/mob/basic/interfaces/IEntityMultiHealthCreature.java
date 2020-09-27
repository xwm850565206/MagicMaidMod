package com.xwm.magicmaid.entity.mob.basic.interfaces;

/**
 * 让生物可以拥有多个血条的接口
 */
public interface IEntityMultiHealthCreature {

    /**
     * 设置血条最大值
     * @param maxhealthbarnum
     */
    void setMaxHealthbarnum(int maxhealthbarnum);

    /**
     * 得到血条最大值
     * @return
     */
    int getMaxHealthBarnum();

    /**
     * 设置当前血条数
     * @param healthbarnum
     */
    void setHealthbarnum(int healthbarnum);

    /**
     * 得到当前血条数
     * @return
     */
    int getHealthBarNum();

    /**
     * 得到当前真正血量
     * @return
     */
    float getTrueHealth();

    /**
     * 得到当前最大血量
     * @return
     */
    float getTrueMaxHealth();

    /**
     * 直接死亡
     */
    void killSelf();

    /**
     * 治疗
     * @param healAmount
     */
    void heal(float healAmount);

    /**
     * 是否死亡
     * @return
     */
    boolean isEntityAlive();

    /**
     * 设置死亡
     */
    void setDead();

    /**
     * 死亡的回收步骤
     */
    void onDeathUpdate();
}
