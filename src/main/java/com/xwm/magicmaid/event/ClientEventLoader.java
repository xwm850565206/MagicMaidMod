package com.xwm.magicmaid.event;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.init.PotionInit;
import com.xwm.magicmaid.key.KeyLoader;
import com.xwm.magicmaid.network.ClientEntityDataPacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.registry.MagicRenderRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//控制各种渲染
@Mod.EventBusSubscriber
public class ClientEventLoader
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

    /**
     * 控制打开player menu的事宜
     * @param event
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (KeyLoader.OPEN_MENU.isPressed()) {
            player.openGui(Main.instance, Reference.GUI_PLAYER_MENU_MAIN, player.getEntityWorld(), (int)player.posX, (int)player.posY, (int)player.posZ);
        }
    }

    private int cameratick = 0;
    private int rotateSpeed = 0;
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        float f = player.hurtTime;
        if (f >= 10) {
            rotateSpeed = 5;
        }
        if (rotateSpeed == 0)
            return;

        PotionEffect effect = player.getActivePotionEffect(PotionInit.GHOST_EFFECT);
        if (effect == null || effect.getDuration() <= 0) {
            rotateSpeed = 0;
            cameratick = 0;
            return;
        }

        event.setRoll(event.getRoll() + cameratick);
        cameratick += rotateSpeed;
        if (cameratick >= 360 * 2) {
            cameratick = 0;
            rotateSpeed = Math.max(0, rotateSpeed-1);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onFogColorRender(EntityViewRenderEvent.FogColors event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        PotionEffect effect = player.getActivePotionEffect(PotionInit.BIAN_FLOWER_EFFECT);
        if (effect != null && effect.getDuration() > 0){
            event.setBlue(0);
            event.setGreen(0);
            event.setRed(158.0f / 255.0f);
            return;
        }

        effect = player.getActivePotionEffect(PotionInit.GHOST_EFFECT);
        if (effect != null && effect.getDuration() > 0){
            event.setBlue(122.0f/ 255.0f);
            event.setGreen(0);
            event.setRed(0);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onFirstPersonRender(EntityViewRenderEvent.RenderFogEvent event)
    {

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        PotionEffect effect = player.getActivePotionEffect(PotionInit.BIAN_FLOWER_EFFECT);
        if (effect != null && effect.getDuration() > 0) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(7 * 0.8F);
            GlStateManager.setFogDensity(0.3F);
            return;
        }

        effect = player.getActivePotionEffect(PotionInit.GHOST_EFFECT);
        if (effect != null && effect.getDuration() > 0){
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(8 * 0.8F);
            GlStateManager.setFogDensity(0.2F);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onEntityRenderPre(RenderLivingEvent.Pre event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        PotionEffect effect = player.getActivePotionEffect(PotionInit.BIAN_FLOWER_EFFECT);
        if (effect != null && effect.getDuration() > 0){
            renderBianBuff(event, mc);
            return;
        }

        effect = player.getActivePotionEffect(PotionInit.GHOST_EFFECT);
        if (effect != null && effect.getDuration() > 0){
            renderGhostBuff(event, mc);
            return;
        }

    }

    public static final ResourceLocation QUARTZ = new ResourceLocation("minecraft", "textures/blocks/quartz_block_chiseled_top.png");

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onConvictionRender(RenderLivingEvent.Pre event)
    {
        EntityLivingBase entityLivingBase = event.getEntity();
        NBTTagCompound entityData = entityLivingBase.getEntityData();

        if (entityData.hasKey(Reference.EFFECT_CONVICTION))
        {
            AxisAlignedBB bb = entityLivingBase.getEntityBoundingBox();
            double renderPosX = (bb.maxX + bb.minX) / 2.0 - TileEntityRendererDispatcher.staticPlayerX - 0.5;
            double renderPosY = bb.maxY - TileEntityRendererDispatcher.staticPlayerY;
            double renderPosZ = (bb.maxZ + bb.minZ) / 2.0 - TileEntityRendererDispatcher.staticPlayerZ - 0.5;

            boolean flag = entityData.getBoolean(Reference.EFFECT_CONVICTION);
            float[] color = null;
            if (flag) {
                color = new float[]{0.2f, 0.2f, 0.2f};
                Minecraft.getMinecraft().getTextureManager().bindTexture(QUARTZ);
                }
            else{
                color = new float[]{1.0f, 1.0f, 0.0f};
                event.getRenderer().bindTexture(QUARTZ);
            }
            TileEntityBeaconRenderer.renderBeamSegment(renderPosX, renderPosY, renderPosZ, 1.0, 1.0, (double) entityLivingBase.getEntityWorld().getTotalWorldTime(), 0, 256, color);

        }
    }

    //渲染红色警告区域
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event)
    {
//        MagicRenderRegistry.addRenderBox(0, Minecraft.getMinecraft().player.getEntityBoundingBox());
        MagicRenderRegistry.renderBoxList();
    }

    @SideOnly(Side.CLIENT)
    private void renderBianBuff(RenderLivingEvent.Pre event, Minecraft mc)
    {
        EntityLivingBase entityLivingBase = event.getEntity();
        NBTTagCompound entityData = entityLivingBase.getEntityData();
//        event.getRenderer().setRenderOutlines(true);
//        GlStateManager.enableOutlineMode(0xffffff);
        GlStateManager.pushMatrix();
        int tick = entityData.getInteger(Reference.MODID + "tick");
        int red = entityData.getInteger(Reference.MODID + "red");
        int green = entityData.getInteger(Reference.MODID + "green");
        int blue = entityData.getInteger(Reference.MODID + "blue");

        if (tick == 0) {
            int i = random.nextInt(3);
            List<Integer> color = biancolor.get(i);
            red = color.get(0);
            green = color.get(1);
            blue = color.get(2);
            entityData.setInteger(Reference.MODID + "red", red);
            entityData.setInteger(Reference.MODID + "green", green);
            entityData.setInteger(Reference.MODID + "blue", blue);
        }

        int f = reverseTick(tick);
        tick++;

        if (tick == 120) {
            tick = 0;
        }
        entityData.setInteger(Reference.MODID + "tick", tick);
        GlStateManager.translate(event.getX(), event.getY() + event.getEntity().height + 0.2 + f * 0.01, event.getZ());
        GlStateManager.rotate((int)(tick / 120.0 * 360), 0, 1, 0);

        boolean flag = false;
        if (entityLivingBase instanceof EntityZombie)
        {
            if (entityData.hasKey(Reference.MODID + "obsession"))
                flag = entityData.getBoolean(Reference.MODID + "obsession");
            else {
                flag = random.nextDouble() < 0.2;
                entityData.setBoolean(Reference.MODID + "obsession", flag);
                ClientEntityDataPacket packet = new ClientEntityDataPacket(entityLivingBase.getEntityId(),
                        entityLivingBase.getEntityWorld().provider.getDimension(),
                        0,
                        flag ? "true" : "false",
                        Reference.MODID + "obsession");
                NetworkLoader.instance.sendToServer(packet);
            }
        }


        if (flag) {
            event.getRenderer().setRenderOutlines(true);
            mc.getItemRenderer().
                    renderItemSide(event.getEntity(), new ItemStack(ItemInit.ITEM_OBSESSION), ItemCameraTransforms.TransformType.GROUND, false);
        }
        else if (entityLivingBase instanceof EntityMob){
            GlStateManager.scale(0.5, 0.5, 0.5);
            mc.getItemRenderer().
                    renderItemSide(event.getEntity(), new ItemStack(Item.getItemFromBlock(BlockInit.BLOCK_BIAN_FLOWER)), ItemCameraTransforms.TransformType.GROUND, false);
        }
        GlStateManager.color(red / 255f, green / 255f, blue / 255f, 0.6f);
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    private void renderGhostBuff(RenderLivingEvent.Pre event, Minecraft mc)
    {
        EntityLivingBase entityLivingBase = event.getEntity();
        NBTTagCompound entityData = entityLivingBase.getEntityData();
//        event.getRenderer().setRenderOutlines(true);
//        GlStateManager.enableOutlineMode(0xffffff);
        GlStateManager.pushMatrix();
        int tick = entityData.getInteger(Reference.MODID + "tick");
        int red = entityData.getInteger(Reference.MODID + "red");
        int green = entityData.getInteger(Reference.MODID + "green");
        int blue = entityData.getInteger(Reference.MODID + "blue");

        if (tick == 0) {
            List<Integer> color = Arrays.asList(255, 0, 0);
            red = color.get(0);
            green = color.get(1);
            blue = color.get(2);
            entityData.setInteger(Reference.MODID + "red", red);
            entityData.setInteger(Reference.MODID + "green", green);
            entityData.setInteger(Reference.MODID + "blue", blue);
        }

        int f = reverseTick(tick);
        tick++;

        if (tick == 120) {
            tick = 0;
        }
        entityData.setInteger(Reference.MODID + "tick", tick);

        boolean flag = false;
        if (entityLivingBase instanceof EntitySkeleton)
        {
            if (entityData.hasKey(Reference.MODID + "ghost"))
                flag = entityData.getBoolean(Reference.MODID + "ghost");
            else {
                flag = random.nextDouble() < 0.2;
                entityData.setBoolean(Reference.MODID + "ghost", flag);
                ClientEntityDataPacket packet = new ClientEntityDataPacket(entityLivingBase.getEntityId(),
                        entityLivingBase.getEntityWorld().provider.getDimension(),
                        0,
                        flag ? "true" : "false",
                        Reference.MODID + "ghost");
                NetworkLoader.instance.sendToServer(packet);
            }
        }


        if (flag) {
            GlStateManager.translate(event.getX(), event.getY() + entityLivingBase.height * 2.0 / 3.0, event.getZ());
            event.getRenderer().setRenderOutlines(true);
            mc.getItemRenderer().
                    renderItemSide(event.getEntity(), new ItemStack(ItemInit.ITEM_ELIMINATE_SOUL_NAIL), ItemCameraTransforms.TransformType.GROUND, false);
        }
        else if (entityLivingBase instanceof EntityMob){
            GlStateManager.translate(event.getX(), event.getY() + event.getEntity().height + 0.2 + f * 0.01, event.getZ());
            GlStateManager.rotate((int)(tick / 120.0 * 360), 0, 1, 0);
            GlStateManager.scale(0.5, 0.5, 0.5);
            mc.getItemRenderer().
                    renderItemSide(event.getEntity(), new ItemStack(Item.getItemFromBlock(BlockInit.BLOCK_BIAN_FLOWER)), ItemCameraTransforms.TransformType.GROUND, false);
        }
        GlStateManager.color(red / 255f, green / 255f, blue / 255f, 0.6f);
        GlStateManager.popMatrix();
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void cancelAbsorbEntityRender(RenderLivingEvent.Pre event)
    {
        EntityLivingBase entity = event.getEntity();
        try {
            if (entity.getEntityData().hasKey(Reference.EFFECT_ABSORB)) {
                if (entity.getEntityData().getBoolean(Reference.EFFECT_ABSORB))
                    event.setCanceled(true);
            }
        } catch (Exception e)
        {
            ;
        }
    }
}
