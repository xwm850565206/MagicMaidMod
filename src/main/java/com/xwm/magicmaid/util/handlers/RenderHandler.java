package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRett;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMartha;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelina;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.model.Rett.ModelMagicMaidRett;
import com.xwm.magicmaid.entity.model.Selina.ModelMagicMaidSeline;
import com.xwm.magicmaid.entity.model.martha.ModelMagicMaidMartha;
import com.xwm.magicmaid.entity.model.weapon.ModelConviction;
import com.xwm.magicmaid.entity.model.weapon.ModelPandorasBox;
import com.xwm.magicmaid.entity.model.weapon.ModelRepentance;
import com.xwm.magicmaid.entity.render.RenderMagicMaid;
import com.xwm.magicmaid.entity.render.RenderMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponConviction;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponRepantence;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler
{
    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidMartha.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidMartha());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidSelina.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidSeline());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidRett.class, new IRenderFactory<EntityMagicMaid>() {
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
    }
}
