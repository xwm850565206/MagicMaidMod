package com.xwm.magicmaid.player.skill.attributeskill;

import com.xwm.magicmaid.player.skill.IAttributeSkill;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AttributeSkillBase implements IAttributeSkill
{
    protected static final int MAX_LEVEL = 5;
    protected int level = 1;

    /**
     * 更新技能的属性
     */
    public void updateAttribute()
    {

    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        updateAttribute();
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    @Override
    public int getRequirePoint() {
        return getLevel() < getMaxLevel() ? 100 * level * level : -1;
    }

    @Override
    public String getName() {
        return "attribute";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {
        setLevel(compound.getInteger("level"));
    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        compound.setInteger("level", getLevel());
        return compound;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof IAttributeSkill))
            return false;
        return getName().equals(((IAttributeSkill) o).getName());
    }

    @Override
    public int hashCode()
    {
        return getName().hashCode();
    }
}
