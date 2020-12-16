package com.xwm.magicmaid.player.capability;

import com.xwm.magicmaid.player.MagicCreatureAttributes;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.concurrent.Callable;

public class CapabilityMagicCreature
{
    public static class Storage implements Capability.IStorage<ICreatureCapability>
    {
        @Override
        public NBTBase writeNBT(Capability<ICreatureCapability> capability, ICreatureCapability instance, EnumFacing side)
        {
            return SharedMonsterAttributes.writeBaseAttributeMapToNBT(instance.getAttributeMap());
        }

        @Override
        public void readNBT(Capability<ICreatureCapability> capability, ICreatureCapability instance, EnumFacing side, NBTBase nbt)
        {
            SharedMonsterAttributes.setAttributeModifiers(instance.getAttributeMap(), (NBTTagList) nbt);
        }
    }

    public static class Implementation implements ICreatureCapability
    {
        private AbstractAttributeMap attributeMap;
        
        @Override
        public AbstractAttributeMap getAttributeMap() {

            if (this.attributeMap == null)
            {
                this.attributeMap = new AttributeMap();
                this.attributeMap.registerAttribute(MagicCreatureAttributes.NORMAL_DAMAGE_RATE).setBaseValue(1);
                this.attributeMap.registerAttribute(MagicCreatureAttributes.SKILL_DAMAGE_RATE).setBaseValue(1);
                this.attributeMap.registerAttribute(MagicCreatureAttributes.ENERGY).setBaseValue(0);
                this.attributeMap.registerAttribute(MagicCreatureAttributes.PER_ENERGY).setBaseValue(1);
                this.attributeMap.registerAttribute(MagicCreatureAttributes.MAX_ENERGY).setBaseValue(100);
                this.attributeMap.registerAttribute(MagicCreatureAttributes.SKILL_SPEED).setBaseValue(1);
                this.attributeMap.registerAttribute(MagicCreatureAttributes.INJURY_REDUCTION).setBaseValue(0);
                this.attributeMap.registerAttribute(MagicCreatureAttributes.IGNORE_REDUCTION).setBaseValue(0);
            }

            return this.attributeMap;
        }

        @Override
        public double getNormalDamageRate() {
            return this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.NORMAL_DAMAGE_RATE).getAttributeValue();
        }

        @Override
        public double getSkillDamageRate() {
            return this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.SKILL_DAMAGE_RATE).getAttributeValue();
        }

        @Override
        public double getEnergy() {
            return this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.ENERGY).getAttributeValue();
        }

        @Override
        public double getMaxEnergy() {
            return this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.MAX_ENERGY).getAttributeValue();
        }

        @Override
        public double getPerEnergy() {
            return this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.PER_ENERGY).getAttributeValue();
        }

        @Override
        public double getSkillSpeed() {
            return this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.SKILL_SPEED).getAttributeValue();
        }

        @Override
        public double getInjuryReduction() {
            return this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.INJURY_REDUCTION).getAttributeValue();
        }

        @Override
        public double getIgnoreReduction() {
            return this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.IGNORE_REDUCTION).getAttributeValue();
        }

        @Override
        public void setAttributeMap(AbstractAttributeMap attributeMap) {
            this.attributeMap = attributeMap;
        }

        @Override
        public void setNormalDamageRate(double normalDamageRate) {
            this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.NORMAL_DAMAGE_RATE).setBaseValue(normalDamageRate);
        }

        @Override
        public void setSkillDamageRate(double skillDamageRate) {
            this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.SKILL_DAMAGE_RATE).setBaseValue(skillDamageRate);
        }

        @Override
        public void setEnergy(double energy) {
            this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.ENERGY).setBaseValue(energy);
        }

        @Override
        public void setMaxEnergy(double maxEnergy) {
            this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.MAX_ENERGY).setBaseValue(maxEnergy);
        }

        @Override
        public void setPerEnergy(double perEnergy) {
            this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.PER_ENERGY).setBaseValue(perEnergy);
        }

        @Override
        public void setSkillSpeed(double skillSpeed) {
            this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.SKILL_SPEED).setBaseValue(skillSpeed);
        }

        @Override
        public void setInjuryReduction(double injuryReduction) {
            this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.INJURY_REDUCTION).setBaseValue(injuryReduction);
        }

        @Override
        public void setIgnoreReduction(double ignoreReduction) {
            this.getAttributeMap().getAttributeInstance(MagicCreatureAttributes.IGNORE_REDUCTION).setBaseValue(ignoreReduction);
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        private ICreatureCapability capability = new Implementation();
        private Capability.IStorage<ICreatureCapability> storage = CapabilityLoader.CREATURE_CAPABILITY.getStorage();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing)
        {
            if (capability == null) return false;
            return CapabilityLoader.CREATURE_CAPABILITY.equals(capability);
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing)
        {
            if (capability == null) return null;

            if (CapabilityLoader.CREATURE_CAPABILITY.equals(capability))
            {
                return (T) this.capability;
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("magic_creature", storage.writeNBT(CapabilityLoader.CREATURE_CAPABILITY, capability, null));
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound)
        {
            NBTTagList list = (NBTTagList) compound.getTag("magic_creature");
            storage.readNBT(CapabilityLoader.CREATURE_CAPABILITY, capability, null, list);
        }
    }

    public static class Factory implements Callable<ICreatureCapability> {

        @Override
        public ICreatureCapability call() throws Exception {
            return new CapabilityMagicCreature.Implementation();
        }
    }
}
