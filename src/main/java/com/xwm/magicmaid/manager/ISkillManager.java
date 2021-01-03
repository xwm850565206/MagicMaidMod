package com.xwm.magicmaid.manager;


import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * 控制技能的释放，升级等等
 */
public interface ISkillManager
{
    /**
     * 从skillInventory中增加技能点
     * @param player 玩家
     * @return 是否成功
     */
    boolean addSkillPoint(EntityPlayer player);

    /**
     * 增加技能点
     * @param player 玩家
     * @param stack 从stack中获得技能点
     * @return 是否成功增加
     */
    boolean addSkillPoint(EntityPlayer player, ItemStack stack);

    /**
     * 增加技能点
     * @param player 玩家
     * @param skillPoint 技能点
     * @return 是否成功增加
     */
    boolean addSkillPoint(EntityPlayer player, int skillPoint);

    /**
     * 设置玩家目前使用的第index个主动技能为performSkill
     * @param index 序号
     * @param performSkill 要设置的技能
     */
    void setActivePerformSkill(EntityPlayer player, int index, IPerformSkill performSkill);

    /**
     * 升级技能
     * @param player 要升级的玩家
     * @param iSkill 要升级的技能
     * @return 是否升级成功
     */
    boolean upSkillLevel(EntityPlayer player, ISkill iSkill);


    /**
     * 升级技能
     * @param player 要升级的玩家
     * @param skillName 要升级的技能名称
     * @return 是否升级成功
     */
    boolean upSkillLevel(EntityPlayer player, String skillName);

    /**
     * 客户端调用就更新服务端 服务端调用就更新客户端
     * @param player 要更新的玩家
     */
    void updateToOtherSide(EntityPlayer player);

    /**
     * 客户端操作后的数据，需要让服务器同步时调用
     */
    void updateToServer(EntityPlayer player);

    /**
     * 服务器操作后的数据，需要让客户端同步时调用
     * @param player
     */
    void updateToClient(EntityPlayer player);

    /**
     * 客户端操作后的数据，需要让服务器同步时调用
     */
    void updateToServer(ISkillCapability instance, EntityPlayer player);

    /**
     * 服务器操作后的数据，需要让客户端同步时调用
     * @param player
     */
    void updateToClient(ISkillCapability instance, EntityPlayer player);
}
