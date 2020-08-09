package com.xwm.magicmaid.init;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class TextureInit
{
    public static TextureAtlasSprite SOUL;
    public static TextureAtlasSprite CROSS;
    public static TextureAtlasSprite CONVICTION;
    public static TextureAtlasSprite STAR;
    public static TextureAtlasSprite ATOM;

    public static void register(TextureMap map)
    {
        SOUL = map.registerSprite(new ResourceLocation(Reference.MODID, "particle/soul"));
        CROSS = map.registerSprite(new ResourceLocation(Reference.MODID, "particle/cross"));
        CONVICTION = map.registerSprite(new ResourceLocation(Reference.MODID, "particle/conviction"));
        STAR = map.registerSprite(new ResourceLocation(Reference.MODID, "particle/star"));
        ATOM = map.registerSprite(new ResourceLocation(Reference.MODID, "particle/atom"));
    }
}
