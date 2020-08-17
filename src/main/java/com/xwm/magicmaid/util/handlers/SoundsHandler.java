package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsHandler
{
    public static SoundEvent BELL;
    public static SoundEvent MAID_AMBIENT;

    public static void registerSounds()
    {
        BELL = registerSound("bell");
        MAID_AMBIENT = registerSound("maid_ambient");
    }

    private static SoundEvent registerSound(String name)
    {
        ResourceLocation location = new ResourceLocation(Reference.MODID, name);

        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
