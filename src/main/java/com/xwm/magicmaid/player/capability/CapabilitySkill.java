package com.xwm.magicmaid.player.capability;

import com.xwm.magicmaid.player.skill.*;
import com.xwm.magicmaid.player.skill.attributeskill.*;
import com.xwm.magicmaid.player.skill.perfomskill.PerformSkillNone;
import com.xwm.magicmaid.registry.MagicSkillRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
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
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("skill_point", instance.getSkillPoint());

            compound.setTag("skill_inventory", ((InventorySingleSlot)instance.getSkillPointInventory()).serializeNBT());

            NBTTagList tagList = new NBTTagList();
            NBTTagList attributeSkillTagList = new NBTTagList();
            for (ISkill skill : instance.getAttributeSkills())
            {
                NBTTagCompound skillCompound = new NBTTagCompound();
                skillCompound.setString("skill_name", skill.getName());
                attributeSkillTagList.appendTag(skill.writeToNBTTagCompound(skillCompound));
            }

            NBTTagList passiveSkillTagList = new NBTTagList();
            for (ISkill skill : instance.getPassiveSkills())
            {
                NBTTagCompound skillCompound = new NBTTagCompound();
                skillCompound.setString("skill_name", skill.getName());
                passiveSkillTagList.appendTag(skill.writeToNBTTagCompound(skillCompound));
            }

            NBTTagList performSkillTagList = new NBTTagList();
            for (ISkill skill : instance.getPerformSkills())
            {
                NBTTagCompound skillCompound = new NBTTagCompound();
                skillCompound.setString("skill_name", skill.getName());
                performSkillTagList.appendTag(skill.writeToNBTTagCompound(skillCompound));
            }

            NBTTagList activeSkillTagList = new NBTTagList();
            for (ISkill skill : instance.getActivePerformSkills())
            {
                NBTTagCompound skillCompound = new NBTTagCompound();
                skillCompound.setString("skill_name", skill.getName());
                activeSkillTagList.appendTag(skill.writeToNBTTagCompound(skillCompound));
            }

            tagList.appendTag(attributeSkillTagList);
            tagList.appendTag(passiveSkillTagList);
            tagList.appendTag(performSkillTagList);
            tagList.appendTag(activeSkillTagList);

            compound.setTag("skill_list", tagList);
            return compound;
        }

        @Override
        public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound data = (NBTTagCompound) nbt;

            // 技能点
            instance.setSkillPoint(data.getInteger("skill_point"));

            // 技能点相关的背包
            ((InventorySingleSlot)instance.getSkillPointInventory()).deserializeNBT(data.getCompoundTag("skill_inventory"));

            // 技能
            NBTTagList tagList = (NBTTagList) data.getTag("skill_list");
            NBTTagList attributeSkillTagList = (NBTTagList) tagList.get(0);
            NBTTagList passiveSkillTagList = (NBTTagList) tagList.get(1);
            NBTTagList performSkillTagList = (NBTTagList) tagList.get(2);
            NBTTagList activeSkillTagList = (NBTTagList) tagList.get(3);

            for (int i = 0; i < attributeSkillTagList.tagCount(); i++)
            {
                NBTTagCompound compound = attributeSkillTagList.getCompoundTagAt(i);
                String name = compound.getString("skill_name");
                ISkill skill = MagicSkillRegistry.getSkill(name);
                if (skill != null) {
                    skill.readFromNBTTagCompound(compound);
                    instance.setAttributeSkill(name, (IAttributeSkill) skill);
                }
            }

            for (int i = 0; i < passiveSkillTagList.tagCount(); i++)
            {
                NBTTagCompound compound = passiveSkillTagList.getCompoundTagAt(i);
                String name = compound.getString("skill_name");
                ISkill skill = MagicSkillRegistry.getSkill(name);
                if (skill != null) {
                    skill.readFromNBTTagCompound(compound);
                    instance.setPassiveSkill(name, (IPassiveSkill) skill);
                }
            }

            for (int i = 0; i < performSkillTagList.tagCount(); i++)
            {
                NBTTagCompound compound = performSkillTagList.getCompoundTagAt(i);
                String name = compound.getString("skill_name");
                ISkill skill = MagicSkillRegistry.getSkill(name);
                if (skill != null) {
                    skill.readFromNBTTagCompound(compound);
                    instance.setPerformSkill(name, (IPerformSkill) skill);
                }
            }

            for (int i = 0; i < activeSkillTagList.tagCount(); i++)
            {
                NBTTagCompound compound = activeSkillTagList.getCompoundTagAt(i);
                String name = compound.getString("skill_name");
                IPerformSkill skill = instance.getPerformSkill(name);
                if (skill != null) {
                    instance.setActivePerformSkill(i, skill);
                }
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

        private NonNullList<IPerformSkill> activePerformSkill =  NonNullList.withSize(2, PerformSkillNone.NONE); //同时持有两个技能

        private int skillPoint = 0;

        private IInventory singleSlot = new InventorySingleSlot();

        @Override
        public int getSkillPoint() {
            return skillPoint;
        }

        @Override
        public IInventory getSkillPointInventory() {
            return singleSlot;
        }

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
        public void setSkillPoint(int skillPoint) {
            this.skillPoint = skillPoint;
        }

        @Override
        public void setSkillPointInventory(IInventory inventory) {
            this.singleSlot = inventory;
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

        @Override
        public NonNullList<IPerformSkill> getActivePerformSkills() {
            return this.activePerformSkill;
        }

        @Override
        public IPerformSkill getActivePerformSkill(int index) {
            return this.activePerformSkill.get(index);
        }

        @Override
        public Map<String, IAttributeSkill> getAtributeSkillMap() {
            return attributeSkillMap;
        }

        @Override
        public Map<String, IPassiveSkill> getPassiveSkillMap() {
            return passiveSkillMap;
        }

        @Override
        public Map<String, IPerformSkill> getPerformSkillMap() {
            return performSkillMap;
        }

        @Override
        public void setActivePerformSkill(int index, IPerformSkill performSkill) {
            this.activePerformSkill.set(index, performSkill);
        }

        @Override
        public void setAttributeSkills(Map<String, IAttributeSkill> attributeSkillMap) {
            this.attributeSkillMap = attributeSkillMap;
        }

        @Override
        public void setPassiveSkills(Map<String, IPassiveSkill> passiveSkillMap) {
            this.passiveSkillMap = passiveSkillMap;
        }

        @Override
        public void setPerformSkills(Map<String, IPerformSkill> performSkillMap) {
            this.performSkillMap = performSkillMap;
        }

        @Override
        public void setActivePerformSkills(NonNullList<IPerformSkill> activePerformSkill) {
            this.activePerformSkill = activePerformSkill;
        }


        @Override
        public void fromSkillCapability(ISkillCapability other) {
            this.setAttributeSkills(other.getAtributeSkillMap());
            this.setPassiveSkills(other.getPassiveSkillMap());
            this.setPerformSkills(other.getPerformSkillMap());
            this.setSkillPoint(other.getSkillPoint());
            this.setSkillPointInventory(other.getSkillPointInventory());
            this.setActivePerformSkills(other.getActivePerformSkills());
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
            return (NBTTagCompound) storage.writeNBT(CapabilityLoader.SKILL_CAPABILITY, capability, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound)
        {
            storage.readNBT(CapabilityLoader.SKILL_CAPABILITY, capability, null, compound);
        }
    }

    public static class Factory implements Callable<ISkillCapability> {

        @Override
        public ISkillCapability call() throws Exception {
            return new Implementation();
        }
    }

}
