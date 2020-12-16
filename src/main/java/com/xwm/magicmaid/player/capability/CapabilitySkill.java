package com.xwm.magicmaid.player.capability;

import com.xwm.magicmaid.player.skill.IAttributeSkill;
import com.xwm.magicmaid.player.skill.IPassiveSkill;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.player.skill.attributeskill.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CapabilitySkill
{
    public static class Storage implements Capability.IStorage<ISkillCapability>
    {
        @Override
        public NBTBase writeNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side)
        {
            NBTTagList tagList = new NBTTagList();

            NBTTagList attributeSkillTagList = new NBTTagList();
            for (ISkill skill : instance.getAttributeSkills())
            {
                attributeSkillTagList.appendTag(skill.writeToNBTTagCompound(new NBTTagCompound()));
            }

            NBTTagList passiveSkillTagList = new NBTTagList();
            for (ISkill skill : instance.getPassiveSkills())
            {
                passiveSkillTagList.appendTag(skill.writeToNBTTagCompound(new NBTTagCompound()));
            }

            NBTTagList performSkillTagList = new NBTTagList();
            for (ISkill skill : instance.getPerformSkills())
            {
                performSkillTagList.appendTag(skill.writeToNBTTagCompound(new NBTTagCompound()));
            }

            tagList.appendTag(attributeSkillTagList);
            tagList.appendTag(passiveSkillTagList);
            tagList.appendTag(performSkillTagList);
            return tagList;
        }

        @Override
        public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagList tagList = (NBTTagList) nbt;
            NBTTagList attributeSkillTagList = (NBTTagList) tagList.get(0);
            NBTTagList passiveSkillTagList = (NBTTagList) tagList.get(1);
            NBTTagList performSkillTagList = (NBTTagList) tagList.get(2);

            for (int i = 0; i < attributeSkillTagList.tagCount(); i++)
            {
                NBTTagCompound compound = attributeSkillTagList.getCompoundTagAt(i);
                String name = compound.getString("skill_name");
                if (instance.getAttributeSkill(name) != null)
                    instance.getAttributeSkill(name).readFromNBTTagCompound(compound);
            }

            for (int i = 0; i < passiveSkillTagList.tagCount(); i++)
            {
                NBTTagCompound compound = passiveSkillTagList.getCompoundTagAt(i);
                String name = compound.getString("skill_name");
                if (instance.getPassiveSkill(name) != null)
                    instance.getPassiveSkill(name).readFromNBTTagCompound(compound);
            }

            for (int i = 0; i < performSkillTagList.tagCount(); i++)
            {
                NBTTagCompound compound = performSkillTagList.getCompoundTagAt(i);
                String name = compound.getString("skill_name");
                if (instance.getPerformSkill(name) != null)
                    instance.getPerformSkill(name).readFromNBTTagCompound(compound);
            }
        }
    }

    public static class Implementation implements ISkillCapability
    {
        private Map<String, IAttributeSkill> attributeSkillMap = new HashMap<String, IAttributeSkill>(){{
            IAttributeSkill normalDamageRate = new AttributeSkillNormalDamageRate();
            put(normalDamageRate.getName(), normalDamageRate);

            IAttributeSkill skillDamageRate = new AttributeSkillSkillDamageRate();
            put(skillDamageRate.getName(), skillDamageRate);

            IAttributeSkill maxEnergy = new AttributeSkillMaxEnergy();
            put(maxEnergy.getName(), maxEnergy);

            IAttributeSkill skillSpeed = new AttributeSkillSkillSpeed();
            put(skillSpeed.getName(), skillSpeed);

            IAttributeSkill injuryReduction = new AttributeSkillInjuryReduction();
            put(injuryReduction.getName(), injuryReduction);

            IAttributeSkill ignoreReduction = new AttributeSkillIgnoreReduction();
            put(ignoreReduction.getName(), ignoreReduction);
        }};
        private Map<String, IPassiveSkill> passiveSkillMap = new HashMap<>();
        private Map<String, IPerformSkill> performSkillMap = new HashMap<>();

        @Override
        public Collection<IAttributeSkill> getAttributeSkills() {
            return attributeSkillMap.values();
        }

        @Override
        public Collection<IPassiveSkill> getPassiveSkills() {
            return passiveSkillMap.values();
        }

        @Override
        public Collection<IPerformSkill> getPerformSkills() {
            return performSkillMap.values();
        }

        @Override
        public IAttributeSkill getAttributeSkill(String name) {
            return attributeSkillMap.get(name);
        }

        @Override
        public IPassiveSkill getPassiveSkill(String name) {
            return passiveSkillMap.get(name);
        }

        @Override
        public IPerformSkill getPerformSkill(String name) {
            return performSkillMap.get(name);
        }

        @Override
        public void setAttributeSkill(String name, IAttributeSkill attributeSkill) {
            attributeSkillMap.put(name, attributeSkill);
        }

        @Override
        public void setPassiveSkill(String name, IPassiveSkill passiveSkill) {
            passiveSkillMap.put(name, passiveSkill);
        }

        @Override
        public void setPerformSkill(String name, IPerformSkill performSkill) {
            performSkillMap.put(name, performSkill);
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        private ISkillCapability capability = new Implementation();
        private Capability.IStorage<ISkillCapability> storage = CapabilityLoader.SKILL_CAPABILITY.getStorage();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing)
        {
            if (capability == null) return false;
            return CapabilityLoader.SKILL_CAPABILITY.equals(capability);
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing)
        {
            if (capability == null) return null;
            if (CapabilityLoader.SKILL_CAPABILITY.equals(capability))
            {
                return (T) this.capability;
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("magic_skill", storage.writeNBT(CapabilityLoader.SKILL_CAPABILITY, capability, null));
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound)
        {
            NBTTagList list = (NBTTagList) compound.getTag("magic_skill");
            storage.readNBT(CapabilityLoader.SKILL_CAPABILITY, capability, null, list);
        }
    }

    public static class Factory implements Callable<ISkillCapability> {

        @Override
        public ISkillCapability call() throws Exception {
            return new Implementation();
        }
    }

}
