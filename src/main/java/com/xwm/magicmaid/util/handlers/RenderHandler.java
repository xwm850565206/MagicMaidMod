package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.entity.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.maid.EntityMagicMaidBanana;
import com.xwm.magicmaid.entity.maid.EntityMagicMaidStrawberry;
import com.xwm.magicmaid.entity.maid.EntityMagicMaidBlue;
import com.xwm.magicmaid.entity.model.strawberry.ModelMagicMaidStrawberry;
import com.xwm.magicmaid.entity.model.ModelMagicMaidBlue;
import com.xwm.magicmaid.entity.model.ModelMagicMaidYellow;
import com.xwm.magicmaid.entity.model.weapon.ModelConviction;
import com.xwm.magicmaid.entity.model.weapon.ModelRepentance;
import com.xwm.magicmaid.entity.render.RenderMagicMaid;
import com.xwm.magicmaid.entity.render.RenderMaidWeapon;
import com.xwm.magicmaid.entity.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.weapon.EntityMaidWeaponConviction;
import com.xwm.magicmaid.entity.weapon.EntityMaidWeaponRepantence;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler
{
    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidStrawberry.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidStrawberry());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidBlue.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidBlue());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMaidBanana.class, new IRenderFactory<EntityMagicMaid>() {
            @Override
            public Render<? super EntityMagicMaid> createRenderFor(RenderManager manager) {
                return new RenderMagicMaid(manager, new ModelMagicMaidYellow());
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
    }
}
