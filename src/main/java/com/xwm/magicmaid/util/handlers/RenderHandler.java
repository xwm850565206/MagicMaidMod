package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.entity.effect.EffectDemonKiller;
import com.xwm.magicmaid.entity.mob.basic.EntityNun;
import com.xwm.magicmaid.entity.mob.maid.*;
import com.xwm.magicmaid.entity.mob.weapon.*;
import com.xwm.magicmaid.entity.model.Rett.ModelMagicMaidRett;
import com.xwm.magicmaid.entity.model.Selina.ModelMagicMaidSeline;
import com.xwm.magicmaid.entity.model.martha.ModelMagicMaidMartha;
import com.xwm.magicmaid.entity.model.weapon.ModelConviction;
import com.xwm.magicmaid.entity.model.weapon.ModelPandorasBox;
import com.xwm.magicmaid.entity.model.weapon.ModelRepentance;
import com.xwm.magicmaid.entity.model.weapon.ModelWhisper;
import com.xwm.magicmaid.entity.throwable.EntityEvilBall;
import com.xwm.magicmaid.entity.throwable.EntityJusticeBall;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.registry.MagicModelRegistry;
import com.xwm.magicmaid.render.effect.RenderEffectDemonKiller;
import com.xwm.magicmaid.render.entity.RenderBossBall;
import com.xwm.magicmaid.render.entity.RenderMagicMaid;
import com.xwm.magicmaid.render.entity.RenderMaidWeapon;
import com.xwm.magicmaid.render.entity.RenderNun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler
{
    public static void registerEntityRenders() {
        //martha
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidMartha.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidMartha());
            }
        });
        //martha boss
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidMarthaBoss.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidMartha());
            }
        });

        //selina
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidSelina.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidSeline());
            }
        });

        //selina boss
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidSelinaBoss.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidSeline());
            }
        });

        //rett
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidRett.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidRett());
            }
        });

        //rett boss
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidRettBoss.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidRett());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityMaidWeaponRepantence.class, new IRenderFactory<EntityMaidWeapon>() {
            @Override
            public Render<? super EntityMaidWeapon> createRenderFor(RenderManager manager) {
                return new RenderMaidWeapon(manager, new ModelRepentance());
            }
        });


        RenderingRegistry.registerEntityRenderingHandler(EntityMaidWeaponConviction.class, new IRenderFactory<EntityMaidWeapon>() {
            @Override
            public Render<? super EntityMaidWeapon> createRenderFor(RenderManager manager) {
                return new RenderMaidWeapon(manager, new ModelConviction());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityMaidWeaponPandorasBox.class, new IRenderFactory<EntityMaidWeapon>() {
            @Override
            public Render<? super EntityMaidWeapon> createRenderFor(RenderManager manager) {
                return new RenderMaidWeapon(manager, new ModelPandorasBox());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityMaidWeaponWhisper.class, new IRenderFactory<EntityMaidWeapon>() {
            @Override
            public Render<? super EntityMaidWeapon> createRenderFor(RenderManager manager) {
                return new RenderMaidWeapon(manager, new ModelWhisper());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityJusticeBall.class, new IRenderFactory<EntityJusticeBall>() {
            @Override
            public Render<? super EntityJusticeBall> createRenderFor(RenderManager manager) {
                return new RenderBossBall(manager, ItemInit.ITEM_JUSTICE, Minecraft.getMinecraft().getRenderItem());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityEvilBall.class, new IRenderFactory<EntityEvilBall>() {
            @Override
            public Render<? super EntityEvilBall> createRenderFor(RenderManager manager) {
                return new RenderBossBall(manager, ItemInit.ITEM_EVIL, Minecraft.getMinecraft().getRenderItem());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityNun.class, new IRenderFactory<EntityNun>() {
            @Override
            public Render<? super EntityNun> createRenderFor(RenderManager manager) {
                return new RenderNun(manager);
            }
        });

//        RenderingRegistry.registerEntityRenderingHandler(EffectThrowableBase.class, new IRenderFactory<Entity>() {
//            @Override
//            public Render<? super Entity> createRenderFor(RenderManager manager) {
//                return new RenderEffect(manager, new EntityEffectTest(), 1.0f, new ResourceLocation(Reference.MODID + ":textures/entities/portal.png"));
//            }
//        });
//
//        RenderingRegistry.registerEntityRenderingHandler(EffectBox.class, new IRenderFactory<Entity>() {
//            @Override
//            public Render<? super Entity> createRenderFor(RenderManager manager) {
//                return new RenderEffect(manager, new ModelEffectBox(), 0.0f, new ResourceLocation(Reference.MODID + ":textures/effect/effect_box.png"));
//            }
//        });

        RenderingRegistry.registerEntityRenderingHandler(EffectDemonKiller.class, new IRenderFactory<EffectDemonKiller>() {
            @Override
            public Render<? super EffectDemonKiller> createRenderFor(RenderManager manager) {
                return new RenderEffectDemonKiller(manager);
            }
        });

        MagicModelRegistry.registerAll();
    }
}
