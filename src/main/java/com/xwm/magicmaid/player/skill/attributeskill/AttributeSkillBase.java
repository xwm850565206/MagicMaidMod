package com.xwm.magicmaid.player.skill.attributeskill;

import com.xwm.magicmaid.player.skill.IAttributeSkill;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AttributeSkillBase implements IAttributeSkill
{
    protected static final int MAX_LEVEL = 6;
    protected int level = 0;
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

    @Override
    public int getRequirePoint() {
        return 10 * level;
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
