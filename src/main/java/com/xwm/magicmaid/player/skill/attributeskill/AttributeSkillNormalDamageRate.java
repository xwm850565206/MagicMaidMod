package com.xwm.magicmaid.player.skill.attributeskill;

import com.xwm.magicmaid.player.MagicCreatureAttributes;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class AttributeSkillNormalDamageRate extends AttributeSkillBase
{
    private double normal_damage_rate = MagicCreatureAttributes.NORMAL_DAMAGE_RATE.getDefaultValue();

    @Override
    public void perform(EntityLivingBase player) {
        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {
            ICreatureCapability capability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (capability != null)
                capability.setNormalDamageRate(normal_damage_rate);
        }
    }

    @Override
    public String getName() {
        return super.getName() + ".normal_damage_rate";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {
        super.readFromNBTTagCompound(compound);
        normal_damage_rate = compound.getDouble("normal_damage_rate");
    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        super.writeToNBTTagCompound(compound);
        compound.setDouble("normal_damage_rate", normal_damage_rate);
        return compound;
    }

    @Override
    public String getDescription() {
        return "攻击倍率";
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "texture/gui/icon/normal_damage_rate");
    }

}
