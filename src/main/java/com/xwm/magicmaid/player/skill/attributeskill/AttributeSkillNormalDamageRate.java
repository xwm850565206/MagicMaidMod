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

public class AttributeSkillNormalDamageRate extends AttributeSkillBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    private double normal_damage_rate = MagicCreatureAttributes.NORMAL_DAMAGE_RATE.getDefaultValue();

    @Override
    public void updateAttribute()
    {
        normal_damage_rate = 1 + 0.5 * (level - 1);
    }

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
    public void drawIcon(int x, int y, float scale) {
        // 0 77 46 46
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 77, 46, 46);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDescription() {
        return "攻击倍率" + ": " + normal_damage_rate;
    }
}
