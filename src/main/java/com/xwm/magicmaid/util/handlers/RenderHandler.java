package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.entity.mob.maid.*;
import com.xwm.magicmaid.entity.mob.weapon.*;
import com.xwm.magicmaid.entity.model.Rett.ModelMagicMaidRett;
import com.xwm.magicmaid.entity.model.Selina.ModelMagicMaidSeline;
import com.xwm.magicmaid.entity.model.martha.ModelMagicMaidMartha;
import com.xwm.magicmaid.entity.model.weapon.ModelConviction;
import com.xwm.magicmaid.entity.model.weapon.ModelPandorasBox;
import com.xwm.magicmaid.entity.model.weapon.ModelRepentance;
import com.xwm.magicmaid.entity.model.weapon.ModelWhisper;
import com.xwm.magicmaid.entity.render.RenderMagicMaid;
import com.xwm.magicmaid.entity.render.RenderMaidWeapon;
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
    }
}
