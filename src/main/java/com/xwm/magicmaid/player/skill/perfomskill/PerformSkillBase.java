package com.xwm.magicmaid.player.skill.perfomskill;

import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class PerformSkillBase implements IPerformSkill
{
    protected static final int MAX_LEVEL = 3;
    protected int level = 0;
    protected int curColdTime = 0; // 释放技能后，技能进入冷却 0表示冷却完毕


    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    /**
     * 当前技能已经冷却的时间
     * @return 已经冷却的时间 当已经冷却的时间降为0时，冷却完毕
     */
    @Override
    public int getCurColdTime(){
        return curColdTime;
    }

    /**
     * 设置当前技能已经冷却的时间
     * @param curColdTime 当前技能已经冷却的时间
     */
    @Override
    public void setCurColdTime(int curColdTime) {
        this.curColdTime = curColdTime;
    }


    @Override
    public void update() {
        this.curColdTime = Math.max(0, curColdTime - 1);
    }

    /**
     * 释放技能时消耗能量
     * @param playerIn
     * @param worldIn
     * @param posIn
     * @return true表示成功消耗 false表示失败，失败就不会接着执行技能
     */
    @Override
    public boolean consumEnergy(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {
        if (!playerIn.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null)) return false;

        ICreatureCapability creatureCapability = playerIn.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
        if (creatureCapability == null) return false;
        if (creatureCapability.getEnergy() < this.getPerformEnergy()) return false;
        creatureCapability.setEnergy(Math.max(0, creatureCapability.getEnergy() - this.getPerformEnergy()));

        return true;
    }

    @Override
    public String getName() {
        return "perform";
    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        compound.setInteger("level", getLevel());
        compound.setInteger("curColdTime", getCurColdTime());
        return compound;
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {
        setLevel(compound.getInteger("level"));
        setCurColdTime(compound.getInteger("curColdTime"));
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof IPerformSkill))
            return false;
        return getName().equals(((IPerformSkill) o).getName());
    }

    @Override
    public int hashCode()
    {
        return getName().hashCode();
    }
}
