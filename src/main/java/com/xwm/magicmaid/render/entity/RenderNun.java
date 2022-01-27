package com.xwm.magicmaid.render.entity;

import com.xwm.magicmaid.entity.mob.basic.EntityNun;
import com.xwm.magicmaid.entity.model.mob.ModelNun;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderNun extends RenderBiped<EntityNun>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":textures/entities/nun.png");

    public RenderNun(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelNun(), 0.5F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityNun entity) {
        return TEXTURE;
    }

    @Override
    protected void preRenderCallback(EntityNun entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
    }
}
