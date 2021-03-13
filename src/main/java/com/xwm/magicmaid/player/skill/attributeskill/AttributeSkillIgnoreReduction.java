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

public class AttributeSkillIgnoreReduction extends AttributeSkillBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    private double ignore_reduction = MagicCreatureAttributes.IGNORE_REDUCTION.getDefaultValue();

    @Override
    public void updateAttribute()
    {
        ignore_reduction = 0.05 * (level);
    }

    @Override
    public void perform(EntityLivingBase player) {
        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {
            ICreatureCapability capability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (capability != null)
                capability.setIgnoreReduction(ignore_reduction);
        }
    }

    @Override
    public String getName() {
        return super.getName() + ".ignore_reduction";
    }

    @Override
    public void readFromNBTTagCompound(NBTTagCompound compound) {
        super.readFromNBTTagCompound(compound);
        ignore_reduction = compound.getDouble("ignore_reduction");
    }

    @Override
    public NBTTagCompound writeToNBTTagCompound(NBTTagCompound compound) {
        super.writeToNBTTagCompound(compound);
        compound.setDouble("ignore_reduction", ignore_reduction);
        return compound;
    }

    @Override
    public void drawIcon(int x, int y, float scale) {
        // 0 172 46 46
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 87, 92, 46, 46);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDescription() {
        return "减伤无视" + ": " + ignore_reduction;
    }

    @Override
    public String getDetailDescription() {
        return "";
    }

}
