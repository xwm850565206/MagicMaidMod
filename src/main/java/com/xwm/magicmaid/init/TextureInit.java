package com.xwm.magicmaid.init;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class TextureInit
{
    public static ResourceLocation soul = new ResourceLocation(Reference.MODID, "particle/soul");

    public static TextureAtlasSprite SOUL;


    public static void register(TextureMap map)
    {
        SOUL = map.registerSprite(soul);
    }
}
