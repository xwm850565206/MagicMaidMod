package com.xwm.magicmaid.init;

import com.xwm.magicmaid.entity.effect.EffectBox;
import com.xwm.magicmaid.entity.effect.EffectThrowableBase;
import com.xwm.magicmaid.entity.mob.maid.*;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponConviction;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponRepantence;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.entity.throwable.EntityEvilBall;
import com.xwm.magicmaid.entity.throwable.EntityJusticeBall;
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
        registerEntityWithoutEgg(event, Reference.MODID + "_maid_martha", EntityMagicMaidMartha.class, Reference.ENTITY_MARTHA, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_maid_martha_boss", EntityMagicMaidMarthaBoss.class, Reference.ENTITY_MARTHA_BOSS, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_maid_rett", EntityMagicMaidRett.class, Reference.ENTITY_RETT, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_maid_rett_boss", EntityMagicMaidRettBoss.class, Reference.ENTITY_RETT_BOSS, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_maid_selina", EntityMagicMaidSelina.class, Reference.ENTITY_SELINA, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_maid_selina_boss", EntityMagicMaidSelinaBoss.class, Reference.ENTITY_SELINA_BOSS, 50);

        registerEntityWithoutEgg(event, Reference.MODID + "_repentance", EntityMaidWeaponRepantence.class, Reference.ENTITY_REPENTANCE, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_conviction", EntityMaidWeaponConviction.class, Reference.ENTITY_CONVICTION, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_pandora", EntityMaidWeaponPandorasBox.class, Reference.ENTITY_PANDORA, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_whisper", EntityMaidWeaponWhisper.class, Reference.ENTITY_WHISPER, 50);

        registerEntityWithoutEgg(event, Reference.MODID + "_justice", EntityJusticeBall.class, Reference.ENTITY_JUSTICE_BALL, 50);
        registerEntityWithoutEgg(event, Reference.MODID + "_evil", EntityEvilBall.class, Reference.ENTITY_EVIL_BALL, 50);

        registerEntityWithoutEgg(event, Reference.MODID + "_test", EffectThrowableBase.class, Reference.ENTITY_EFFECT_TEST, 50); // todo test
        registerEntity(event, Reference.MODID + "_effect_box", EffectBox.class, Reference.ENTITY_EFFECT_BOX, 50, 1, 1); // todo test
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
