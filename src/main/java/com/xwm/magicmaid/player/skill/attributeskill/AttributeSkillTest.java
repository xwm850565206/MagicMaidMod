package com.xwm.magicmaid.player.skill.attributeskill;

import com.xwm.magicmaid.player.skill.IAttributeSkill;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class AttributeSkillTest implements IAttributeSkill
{

    @Override
    public void perform(EntityLivingBase player) {
        System.out.println("test attribute skill perform");
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getRequirePoint() {
        return 0;
    }

    @Override
    public String getName() {
        return "attribute.test";
    }

    @Override
    public String getDescription() {
        return "属性测试";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {

    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        return new NBTTagCompound();
    }

    @Override
    public ResourceLocation getIcon() {
        return null;
    }
}
