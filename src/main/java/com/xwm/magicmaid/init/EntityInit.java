package com.xwm.magicmaid.init;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.maid.EntityMagicMaidBanana;
import com.xwm.magicmaid.entity.maid.EntityMagicMaidStrawberry;
import com.xwm.magicmaid.entity.maid.EntityMagicMaidBlue;
import com.xwm.magicmaid.entity.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit
{
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
        registerEntity(event, Reference.MODID + "_maid_strawberry", EntityMagicMaidStrawberry.class, Reference.ENTITY_STRAWBERRY, 50, 0xFFFF00, 0xFFD700);
        registerEntity(event, Reference.MODID + "_maid_blue", EntityMagicMaidBlue.class, Reference.ENTITY_BLUE, 50, 0xFFFF00, 0xFFD700);
        registerEntity(event, Reference.MODID + "_maid_yellow", EntityMagicMaidBanana.class, Reference.ENTITY_YELLOW, 50, 0xFFFF00, 0xFFD700);

        registerEntityWithoutEgg(event, Reference.MODID + "_repentance", EntityMaidWeapon.class, Reference.ENTITY_REPENTANCE, 50);
    }

    private static void registerEntity(RegistryEvent.Register<EntityEntry> event, String name, Class<? extends Entity> entity, int id, int range, int color1, int color2) {
        event.getRegistry().register(
                EntityEntryBuilder.create()
                        .entity(entity)
                        .name(name)
                        .id(new ResourceLocation(Reference.MODID + ":" + name), id)
                        .tracker(range, 1, true)
                        .egg(color1, color2)
                        .build()
        );
    }

    private static void registerEntityWithoutEgg(RegistryEvent.Register<EntityEntry> event, String name, Class<? extends Entity> entity, int id, int range)
    {
        event.getRegistry().register(
                EntityEntryBuilder.create()
                        .entity(entity)
                        .name(name)
                        .id(new ResourceLocation(Reference.MODID + ":" + name), id)
                        .tracker(range, 1, true)
                        .build()
        );
    }
}
