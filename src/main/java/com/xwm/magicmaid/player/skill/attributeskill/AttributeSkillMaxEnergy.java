package com.xwm.magicmaid.player.skill.attributeskill;

import com.xwm.magicmaid.player.MagicCreatureAttributes;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class AttributeSkillMaxEnergy extends AttributeSkillBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");
    private static final int[] ATTRIBUTE = new int[]{200, 400, 800, 1000, 1500};
    private double max_energy = MagicCreatureAttributes.MAX_ENERGY.getDefaultValue();

    @Override
    public void updateAttribute()
    {
        max_energy = ATTRIBUTE[level-1];
    }

    @Override
    public void perform(EntityLivingBase player) {
        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {

            ICreatureCapability capability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (capability != null)
                capability.setMaxEnergy(max_energy);
        }
    }

    @Override
    public String getName() {
        return super.getName() + ".max_energy";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {
        super.readFromNBTTagCompound(compound);
        max_energy = compound.getDouble("max_energy");
    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        super.writeToNBTTagCompound(compound);
        compound.setDouble("max_energy", max_energy);
        return compound;
    }

    @Override
    public void drawIcon(float x, float y, float scale) {
        // 87 45 46 46
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 87, 47, 46, 46);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDescription() {
        return "最大能量" + ": " + max_energy;
    }

    @Override
    public String getDetailDescription() {
        return "";
    }

}
