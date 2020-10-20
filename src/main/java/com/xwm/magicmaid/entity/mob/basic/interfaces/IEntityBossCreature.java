package com.xwm.magicmaid.entity.mob.basic.interfaces;

import com.xwm.magicmaid.world.dimension.MagicCreatureFightManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IEntityBossCreature
{
    MagicCreatureFightManager fightManager = null;

    /**
     * 提供斩杀接口
     * @param player
     */
    boolean killItSelfByPlayerDamage(EntityPlayer player);

    /**
     * 获取战斗管理器
     * @return
     */
    MagicCreatureFightManager getFightManager();

    /**
     * 初始化战斗管理器
     */
    void initFightManager(World world);

    /**
     * 设置攻击倍率
     * @param factor
     */
    void setBossDamageFactor(int factor);

    /**
     * 得到boss的阵营，用来让对应物品识别boss阵营并操作，目前0表示正义阵营
     * @return
     */
    int getBossCamp();
}
