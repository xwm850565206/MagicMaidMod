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
}
