package com.xwm.magicmaid.event;

import com.xwm.magicmaid.entity.model.weapon.ModelRepentance;
import com.xwm.magicmaid.entity.render.RenderMaidWeapon;
import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.init.PotionInit;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//控制各种渲染
@Mod.EventBusSubscriber
public class EventRenderLoader
{
    private List<List<Integer>> biancolor = new ArrayList<List<Integer>>(){{
        add(Arrays.asList(67, 235, 217));
        add(Arrays.asList(255, 253, 100));
        add(Arrays.asList(231, 25, 171));
    }};

    private Random random = new Random();

    private int reverseTick(int tick)
    {
        if (tick < 0)
            return 0;
        if (tick < 60)
            return tick;
        if (tick >= 120)
            return 120;

        return 120 - tick;
    }

    private int cameratick = 0;
    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        PotionEffect effect = player.getActivePotionEffect(PotionInit.BIAN_FLOWER_EFFECT);
        if (effect == null || effect.getDuration() <= 0)
            return;

//        event.setRoll(event.getRoll() + cameratick);
//        cameratick++;
//        if (cameratick >= 360)
//            cameratick = 0;
    }

    @SubscribeEvent
    public void onFogColorRender(EntityViewRenderEvent.FogColors event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        PotionEffect effect = player.getActivePotionEffect(PotionInit.BIAN_FLOWER_EFFECT);
        if (effect == null || effect.getDuration() <= 0)
            return;

        event.setBlue(0);
        event.setGreen(0);
        event.setRed(158.0f / 255.0f);
    }

    @SubscribeEvent
    public void onFirstPersonRender(EntityViewRenderEvent.RenderFogEvent event)
    {

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        PotionEffect effect = player.getActivePotionEffect(PotionInit.BIAN_FLOWER_EFFECT);
        if (effect == null || effect.getDuration() <= 0)
            return;

        GlStateManager.setFog(GlStateManager.FogMode.EXP);
        GlStateManager.setFogStart(0.0F);
        GlStateManager.setFogEnd(5 * 0.8F);
        GlStateManager.setFogDensity(0.4F);
    }

    @SubscribeEvent
    public void onEntityRenderPre(RenderLivingEvent.Pre event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        PotionEffect effect = player.getActivePotionEffect(PotionInit.BIAN_FLOWER_EFFECT);
        if (effect == null || effect.getDuration() <= 0)
            return;

        EntityLivingBase entityLivingBase = event.getEntity();
        GlStateManager.pushMatrix();
        int tick = entityLivingBase.getEntityData().getInteger(Reference.MODID + "tick");
        int red = entityLivingBase.getEntityData().getInteger(Reference.MODID + "red");
        int green = entityLivingBase.getEntityData().getInteger(Reference.MODID + "green");
        int blue = entityLivingBase.getEntityData().getInteger(Reference.MODID + "blue");

        if (tick == 0) {
            int i = random.nextInt(3);
            List<Integer> color = biancolor.get(i);
            red = color.get(0);
            green = color.get(1);
            blue = color.get(2);
            entityLivingBase.getEntityData().setInteger(Reference.MODID + "red", red);
            entityLivingBase.getEntityData().setInteger(Reference.MODID + "green", green);
            entityLivingBase.getEntityData().setInteger(Reference.MODID + "blue", blue);
        }

        int f = reverseTick(tick);
        tick++;

        if (tick == 120) {
            tick = 0;
        }
        entityLivingBase.getEntityData().setInteger(Reference.MODID + "tick", tick);
        GlStateManager.translate(event.getX(), event.getY() + event.getEntity().height + 0.2 + f * 0.01, event.getZ());
        GlStateManager.rotate((int)(tick / 120.0 * 360), 0, 1, 0);
        GlStateManager.scale(0.5, 0.5, 0.5);
        mc.getItemRenderer().
                renderItemSide(event.getEntity(), new ItemStack(Item.getItemFromBlock(BlockInit.bianFlower)), ItemCameraTransforms.TransformType.GROUND, false);


        GlStateManager.color(red / 255f, green / 255f, blue / 255f, 0.6f);
        GlStateManager.popMatrix();
    }

}
