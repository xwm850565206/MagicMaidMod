package com.xwm.magicmaid.player.skill.attributeskill;

import com.xwm.magicmaid.player.MagicCreatureAttributes;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class AttributeSkillInjuryReduction extends AttributeSkillBase
{
    private double injury_reduction = MagicCreatureAttributes.INJURY_REDUCTION.getDefaultValue();

    @Override
    public void perform(EntityLivingBase player) {
        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {
            ICreatureCapability capability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (capability != null)
                capability.setInjuryReduction(injury_reduction);
        }
    }

    @Override
    public String getName() {
        return super.getName() + ".injury_reduction";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {
        super.readFromNBTTagCompound(compound);
        injury_reduction = compound.getDouble("injury_reduction");
    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        super.writeToNBTTagCompound(compound);
        compound.setDouble("injury_reduction", injury_reduction);
        return compound;
    }

    @Override
    public void drawIcon(int x, int y) {

    }

    @Override
    public String getDescription() {
        return "减伤率" + ": " + injury_reduction;
    }
}
