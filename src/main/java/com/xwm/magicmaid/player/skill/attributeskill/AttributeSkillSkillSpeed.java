package com.xwm.magicmaid.player.skill.attributeskill;

import com.xwm.magicmaid.player.MagicCreatureAttributes;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class AttributeSkillSkillSpeed extends AttributeSkillBase
{
    private double skill_speed = MagicCreatureAttributes.SKILL_SPEED.getDefaultValue();

    @Override
    public void perform(EntityLivingBase player) {
        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {
            ICreatureCapability capability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (capability != null)
                capability.setSkillSpeed(skill_speed);
        }
    }

    @Override
    public String getName() {
        return super.getName() + ".skill_speed";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {
        super.readFromNBTTagCompound(compound);
        skill_speed = compound.getDouble("skill_speed");
    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        super.writeToNBTTagCompound(compound);
        compound.setDouble("skill_speed", skill_speed);
        return compound;
    }

    @Override
    public void drawIcon(int x, int y) {

    }

    @Override
    public String getDescription() {
        return "技能急速" + ": " + skill_speed;
    }
}
