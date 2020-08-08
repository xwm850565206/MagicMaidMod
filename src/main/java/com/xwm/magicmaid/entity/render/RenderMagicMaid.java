package com.xwm.magicmaid.entity.render;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRett;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMartha;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidBlue;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMagicMaid extends RenderLiving<EntityMagicMaid> {

    public static final ResourceLocation TEXTURE_BERRY = new ResourceLocation(Reference.MODID + ":textures/entities/magicmaidstrawberry.png");
    public static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(Reference.MODID + ":textures/entities/magicmaidblue.png");

    public static final ResourceLocation TEXTURE_RETT_DEMONKILLER = new ResourceLocation(Reference.MODID + ":textures/entities/magicmaidrett.png");

    public RenderMagicMaid(RenderManager manager, ModelBase base) {
        super(manager, base, 0.5F);
    }

    @Override
    protected void applyRotations(EntityMagicMaid entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
    @Override
    protected ResourceLocation getEntityTexture(EntityMagicMaid entity)
    {
        if (entity instanceof EntityMagicMaidMartha)
            return TEXTURE_BERRY;
        else if (entity instanceof EntityMagicMaidBlue)
            return TEXTURE_BLUE;
        else if (entity instanceof EntityMagicMaidRett)
            return TEXTURE_RETT_DEMONKILLER;
        return null;
    }

    @Override
    protected void preRenderCallback(EntityMagicMaid entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}
