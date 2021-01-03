package com.xwm.magicmaid.player.skill.attributeskill;

import com.xwm.magicmaid.player.MagicCreatureAttributes;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class AttributeSkillSkillDamageRate extends AttributeSkillBase
{
    private double skill_damage_rate = MagicCreatureAttributes.SKILL_DAMAGE_RATE.getDefaultValue();

    @Override
    public void perform(EntityLivingBase player) {
        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {
            ICreatureCapability capability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (capability != null)
                capability.setSkillDamageRate(skill_damage_rate);
        }
    }

    @Override
    public String getName() {
        return super.getName() + ".skill_damage_rate";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {
        super.readFromNBTTagCompound(compound);
        skill_damage_rate = compound.getDouble("skill_damage_rate");
    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        super.writeToNBTTagCompound(compound);
        compound.setDouble("skill_damage_rate", skill_damage_rate);
        return compound;
    }

    @Override
    public void drawIcon(int x, int y) {

    }

    @Override
    public String getDescription() {
        return "技能倍率" + ": " + skill_damage_rate;
    }
}
