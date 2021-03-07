package com.xwm.magicmaid.player.skill.perfomskill;

import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PerformSkillNone implements IPerformSkill
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon_1.png");

    public static PerformSkillNone NONE = new PerformSkillNone();

    @Override
    public int getPerformEnergy() {
        return 0;
    }

    @Override
    public int getColdTime() {
        return 0;
    }

    @Override
    public int getCurColdTime() {
        return 0;
    }

    @Override
    public void setCurColdTime(int curColdTime) {

    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

    }

    @Override
    public boolean consumEnergy(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {
        return false;
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
        return "perform.none";
    }

    @Override
    public String getDescription() {
        return "空";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {

    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        return new NBTTagCompound();
    }

    @Override
    public void drawIcon(int x, int y, float scale) {
//        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
//
//        double scalex = 24.0 / 60.0;
//        double scaley = 29.0 / 49.0;
//
//        GlStateManager.pushMatrix();
//        GlStateManager.translate(x / scale, y / scale, 90);
//        GlStateManager.scale(scalex, scaley, 1);
//
//        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 41, 60, 49);
//        GlStateManager.popMatrix();
    }

    @Override
    public void update() {

    }
}
