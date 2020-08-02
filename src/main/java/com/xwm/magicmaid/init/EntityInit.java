package com.xwm.magicmaid.init;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidBanana;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidStrawberry;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidBlue;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponConviction;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponRepantence;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.List;
import java.util.Vector;

public class EntityInit
{
    public static List<Class> entityClass = new Vector<>();

    public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
        registerEntity(event, Reference.MODID + "_maid_strawberry", EntityMagicMaidStrawberry.class, Reference.ENTITY_STRAWBERRY, 50, 0xFFFF00, 0xFFD700);
        registerEntity(event, Reference.MODID + "_maid_blue", EntityMagicMaidBlue.class, Reference.ENTITY_BLUE, 50, 0xFFFF00, 0xFFD700);
        registerEntity(event, Reference.MODID + "_maid_yellow", EntityMagicMaidBanana.class, Reference.ENTITY_YELLOW, 50, 0xFFFF00, 0xFFD700);

        registerEntityWithoutEgg(event, Reference.MODID + "_repentance", EntityMaidWeaponRepantence.class, Reference.ENTITY_REPENTANCE, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_conviction", EntityMaidWeaponConviction.class, Reference.ENTITY_CONVICTION, 50);

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

        entityClass.add(entity);
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

        entityClass.add(entity);
    }
}
