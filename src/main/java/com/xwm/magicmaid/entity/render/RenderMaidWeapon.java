package com.xwm.magicmaid.entity.render;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponConviction;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponRepantence;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMaidWeapon extends RenderLivingBase<EntityMaidWeapon>
{
    public static final ResourceLocation TEXTURE_REPENTANCE = new ResourceLocation(Reference.MODID + ":textures/entities/repentance.png");
    public static final ResourceLocation TEXTURE_CONVICTION = new ResourceLocation(Reference.MODID + ":textures/entities/conviction.png");
    public static final ResourceLocation TEXTURE_PANDORASBOX = new ResourceLocation(Reference.MODID + ":textures/entities/pandorasbox.png");

    public RenderMaidWeapon(RenderManager manager, ModelBase base) {
        super(manager, base, 0.5F);
    }

    @Override
    protected void applyRotations(EntityMaidWeapon entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
    @Override
    protected ResourceLocation getEntityTexture(EntityMaidWeapon entity)
    {
        if (entity instanceof EntityMaidWeaponRepantence)
            return TEXTURE_REPENTANCE;
        else if (entity instanceof EntityMaidWeaponConviction)
            return TEXTURE_CONVICTION;
        else if (entity instanceof EntityMaidWeaponPandorasBox)
            return TEXTURE_PANDORASBOX;
        else
            return null;
    }

    @Override
    protected void preRenderCallback(EntityMaidWeapon entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}
