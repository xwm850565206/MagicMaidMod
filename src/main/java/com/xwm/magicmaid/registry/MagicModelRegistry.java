package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.entity.model.Rett.ModelMagicMaidRettSitting;
import com.xwm.magicmaid.entity.model.Selina.ModelMagicMaidSelineSitting;
import com.xwm.magicmaid.entity.model.martha.ModelMagicMaidMarthaNormalStand;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * 这个注册是用来帮助说明书渲染生物实体的
 */
public class MagicModelRegistry
{
    private static HashMap<ResourceLocation, ModelBase> MODEL = new HashMap<>();
    private static HashMap<ResourceLocation, ResourceLocation> TEXTURE = new HashMap<>();

    public static void register(ResourceLocation resourceLocation, ModelBase modelBase, ResourceLocation texture)
    {
        MODEL.put(resourceLocation, modelBase);
        TEXTURE.put(resourceLocation, texture);
    }

    public static ModelBase getModel(ResourceLocation resourceLocation)
    {
        return MODEL.get(resourceLocation);
    }

    public static ResourceLocation getTexture(ResourceLocation resourceLocation)
    {
        return TEXTURE.get(resourceLocation);
    }

    public static void registerAll()
    {
        register(new ResourceLocation(Reference.MODID + ":" + Reference.MODID + "_maid_martha"), new ModelMagicMaidMarthaNormalStand(), new ResourceLocation(Reference.MODID + ":textures/entities/magicmaidmartha.png"));
        register(new ResourceLocation(Reference.MODID + ":" + Reference.MODID + "_maid_rett"), new ModelMagicMaidRettSitting(), new ResourceLocation(Reference.MODID + ":textures/entities/magicmaidrett.png"));
        register(new ResourceLocation(Reference.MODID + ":" + Reference.MODID + "_maid_selina"), new ModelMagicMaidSelineSitting(), new ResourceLocation(Reference.MODID + ":textures/entities/magicmaidselina.png"));
    }
}
