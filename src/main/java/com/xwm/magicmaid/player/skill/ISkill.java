package com.xwm.magicmaid.player.skill;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISkill
{
    /**
     * 技能等级
     * @return 等级
     */
    int getLevel();

    /**
     * 设置技能等级
     * @param level 等级
     */
    void setLevel(int level);

    /**
     * 技能的最大等级
     * @return 最大等级
     */
    int getMaxLevel();

    /**
     * 升级所需点数
     * @return 点数
     */
    int getRequirePoint();

    /**
     * 技能名字，技能的唯一标识符，技能的名字通过'.'连接，第一个表示的是技能所属的类别{perform, attribute, passive}，第二个表示的是技能的稀有度{normal, rare, secret, unreachable}
     * @return
     */
    String getName();

    /**
     * 获得技能说明
     * @return 技能说明
     */
    String getDescription();

    /**
     * 从nbt中恢复对象
     * @param compound nbt数据
     */
    void readFromNBTTagCompound(NBTTagCompound compound);

    /**
     * 把对象信息写入nbt
     * @param compound 要写入的nbt
     * @return 写入后的nbt
     */
    NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound);

    /**
     * @param x 绘制起始点x
     * @param y 绘制起始点y
     * @param scale 缩放大小
     * 绘制技能图标
     */
    @SideOnly(Side.CLIENT)
    void drawIcon(int x, int y, float scale);
}
