package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsHandler
{
    public static SoundEvent ENTITY_RIDERHERO_AMBIENT, ENTITY_RIDERHERO_HURT;
    public static SoundEvent ENTITY_SHADOWHERO_AMBIENT;//, ENTITY_SHADOWHERO_HURT, ENTITY_SHADOWHERO_DEATH;
    public static SoundEvent ENTITY_MAGICIAN_AMBIENT, ENTITY_MAGICIAN_DEATH;

    public static SoundEvent ENTITY_BERSERKER_AMBIENT, ENTITY_BERSERKER_HURT, ENTITY_BERSERKER_DEATH;
    public static SoundEvent ENTITY_EMPEROR_AMBIENT;

    public static SoundEvent ON_GAME;

    public static void registerSounds()
    {
        ON_GAME = registerSound("fighting");
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
