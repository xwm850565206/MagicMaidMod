package com.xwm.magicmaid.event.loader;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.event.SkillChangedEvent;
import com.xwm.magicmaid.gui.player.GuiSkillHDU;
import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.init.PotionInit;
import com.xwm.magicmaid.key.KeyLoader;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.entity.CPacketEntityData;
import com.xwm.magicmaid.network.gui.CPacketOpenGui;
import com.xwm.magicmaid.network.skill.CPacketSkill;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.registry.MagicRenderRegistry;
import com.xwm.magicmaid.registry.MagicSkillRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.settings.GameSettings;
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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


//控制各种渲染
@Mod.EventBusSubscriber
public class ClientEventLoader {
    private List<List<Integer>> biancolor = new ArrayList<List<Integer>>() {{
        add(Arrays.asList(67, 235, 217));
        add(Arrays.asList(255, 253, 100));
        add(Arrays.asList(231, 25, 171));
    }};

    private Random random = new Random();

    /**
     * 右下角技能图标
     **/
    private List<GuiSkillHDU> skillHDUList;
    /**
     * ctrl释放的另外两个技能图标
     **/
    private List<GuiSkillHDU> ctrlSkillHDUList;
    /**
     * 技能列表是否需要更新，用于更新右下角的技能图标
     **/
    public boolean skillListDirt = true;
    /**
     * toast
     **/
    public String toast = "";
    /**
     * toast的剩余tick
     **/
    public int toastTick = 0;

    /**
     * 是否渲染技能图标
     **/
    public boolean showSkillWidget = true;
    /**
     * 绘制技能按键图标变量
     **/
    private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");


    public ClientEventLoader() {

    }

    public void toastMessage(String message, int tick) {
        this.toast = message;
        this.toastTick = tick;
    }

    private int reverseTick(int tick) {
        if (tick < 0)
            return 0;
        if (tick < 60)
            return tick;
        if (tick >= 120)
            return 120;

        return 120 - tick;
    }

    private void drawBlurBackground(float x, float y, float width, float height) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
//        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(1, 1, 1, 0);
        GL11.glVertex2d(x, y);
        GL11.glColor4f(1, 1, 1, 0);
        GL11.glVertex2d(x, y + height);
        GL11.glColor4f(1, 1, 1, 0.6f);
        GL11.glVertex2d(x + width / 2, y + height);
        GL11.glColor4f(1, 1, 1, 0.6f);
        GL11.glVertex2d(x + width / 2, y);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(1, 1, 1, 0.6f);
        GL11.glVertex2d(x + width / 2, y);
        GL11.glColor4f(1, 1, 1, 0.6f);
        GL11.glVertex2d(x + width / 2, y + height);
        GL11.glColor4f(1, 1, 1, 0);
        GL11.glVertex2d(x + width, y + height);
        GL11.glColor4f(1, 1, 1, 0);
        GL11.glVertex2d(x + width, y);
        GL11.glEnd();

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }

    // 技能按键图标
    private void drawSkillPerformButtion(float x, float y, String key) {
        int width = 20;
        int height = 20;
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontrenderer = mc.fontRenderer;
        mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(0, 0, 100);
        GlStateManager.color(1, 1, 1);
        RenderHelper.disableStandardItemLighting();
        mc.ingameGUI.drawTexturedModalRect(x, y, 0, 66, width, height);
        mc.ingameGUI.drawTexturedModalRect(x + width / 2.0f, y, 200 - width / 2, 66, width / 2, height);
        mc.ingameGUI.drawCenteredString(fontrenderer, key, (int) x + width / 2, (int) y + (height - 8) / 2, 14737632);
        GlStateManager.popMatrix();
    }

    /**
     * 控制打开player menu的事宜
     * 控制技能的释放
     *
     * @param event
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        EntityPlayer player = Minecraft.getMinecraft().player;
        if (KeyLoader.OPEN_MENU.isPressed()) {
            player.openGui(Main.instance, Reference.GUI_PLAYER_MENU_MAIN, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
            CPacketOpenGui packet = new CPacketOpenGui(player.getEntityId(), Reference.GUI_PLAYER_MENU_MAIN, player.getEntityWorld().provider.getDimension(), (int) player.posX, (int) player.posY, (int) player.posZ);
            NetworkLoader.instance.sendToServer(packet);
        }

        // 判断是否按下ctrl键
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        boolean flag = false;
        if (gameSettings.keyBindSprint.isKeyDown())
        {
            flag = true;
            skillListDirt = true;
        }
        else if (gameSettings.keyBindSprint.isPressed())
        {
            skillListDirt = true;
        }


        int begin = flag ? 2 : 0;
        if (KeyLoader.SKILL_1.isPressed()) {
            if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
                ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                if (skillCapability == null) return;

                if (!skillCapability.getActivePerformSkill(begin).getName().equals(MagicSkillRegistry.PERFORM_SKILL_NONE.getName())) {
                    if (skillCapability.getActivePerformSkill(begin).getCurColdTime() > 0) {
                        toastMessage("技能冷却中", 40);
                    } else {
                        ICreatureCapability creatureCapability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
                        if (creatureCapability != null && creatureCapability.getEnergy() < skillCapability.getActivePerformSkill(begin).getPerformEnergy())
                            toastMessage("蓝量不足", 40);
                    }
                }

                // 服务端运行
                CPacketSkill packet = new CPacketSkill(player.getUniqueID(), skillCapability.getActivePerformSkill(begin).getName(), begin, player.getEntityWorld().provider.getDimension(), player.getPosition(), 0);
                NetworkLoader.instance.sendToServer(packet);

                // 客户端运行
                skillCapability.getActivePerformSkill(begin).perform(player, player.getEntityWorld(), player.getPosition());
            }
        }
        if (KeyLoader.SKILL_2.isPressed()) {
            if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
                ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                if (skillCapability == null) return;

                if (!skillCapability.getActivePerformSkill(begin + 1).getName().equals(MagicSkillRegistry.PERFORM_SKILL_NONE.getName())) {
                    if (skillCapability.getActivePerformSkill(begin + 1).getCurColdTime() > 0) {
                        toastMessage("技能冷却中", 40);
                    } else {
                        ICreatureCapability creatureCapability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
                        if (creatureCapability != null && creatureCapability.getEnergy() < skillCapability.getActivePerformSkill(1).getPerformEnergy())
                            toastMessage("蓝量不足", 40);
                    }
                }

                // 服务端运行
                CPacketSkill packet = new CPacketSkill(player.getUniqueID(), skillCapability.getActivePerformSkill(begin + 1).getName(), begin + 1, player.getEntityWorld().provider.getDimension(), player.getPosition(), 0);
                NetworkLoader.instance.sendToServer(packet);

                // 客户端运行
                skillCapability.getActivePerformSkill(begin + 1).perform(player, player.getEntityWorld(), player.getPosition());
            }
        }

    }

    /**
     * 监听技能是否变化，目前主要是用来修改客户端渲染
     *
     * @param event
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerActiveSkillChanged(SkillChangedEvent event) {
        skillListDirt = true;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerLoggin(PlayerEvent.PlayerLoggedInEvent event) {
        skillListDirt = true;
    }


    public static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MODID, "textures/gui/player_menu_main.png");

    /**
     * 控制技能冷却显示等的绘制
     * toast的绘制
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerScreenRender(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;

            int totalDrawToken = 2;
            // 画技能的背景
            if (showSkillWidget && player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
                ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                if (skillCapability != null) {
                    List<IPerformSkill> performSkills = skillCapability.getActivePerformSkills();
                    for (int i = 0; i < totalDrawToken; i++) {
                        drawBlurBackground(event.getResolution().getScaledWidth() - 70, event.getResolution().getScaledHeight() - 19 + ((i - totalDrawToken) * 30), 100, 26);
                    }
                    for (int i = 0; i < totalDrawToken; i++) {
                        String key = i == 0 ? KeyLoader.SKILL_1.getDisplayName() : KeyLoader.SKILL_2.getDisplayName();
                        drawSkillPerformButtion(event.getResolution().getScaledWidth() - 55, event.getResolution().getScaledHeight() - 16 + ((i - totalDrawToken) * 30), key);
                    }
                }
            }

            GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
            // 画技能
            if (showSkillWidget && player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
                ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                if (skillCapability != null) {
                    List<IPerformSkill> performSkills = skillCapability.getActivePerformSkills();

                    // 画ctrl的技能
                    int begin = 0;
                    if (gameSettings.keyBindSprint.isKeyDown()) {
                        begin = 2;
                    }

                    if (skillHDUList == null || skillListDirt) {
                        skillHDUList = new ArrayList<>();
                        int i = 0;
                        for (int t = 0; t < totalDrawToken; t++) {
                            IPerformSkill performSkill = performSkills.get(begin + t);
                            skillHDUList.add(new GuiSkillHDU(performSkill, event.getResolution().getScaledWidth() - 30, event.getResolution().getScaledHeight() - 20 + ((i - totalDrawToken) * 30)));
                            i++;
                        }
                        skillListDirt = false;
                    }

                    int i = 0;
                    float scale = 0.5f;
                    for (GuiSkillHDU skillHDU : skillHDUList) {
                        skillHDU.setiSkill(performSkills.get(i + begin));
                        skillHDU.setX(event.getResolution().getScaledWidth() - 30);
                        skillHDU.setY(event.getResolution().getScaledHeight() - 20 + ((i - totalDrawToken) * 30));
                        skillHDU.drawScreen(mc, scale);
                        i++;
                    }
                }
            }

            // 画蓝条
            if (showSkillWidget && player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null)) {
                ICreatureCapability creatureCapability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);

                if (creatureCapability != null) {
                    double energy = creatureCapability.getEnergy();
                    double progress = energy / creatureCapability.getMaxEnergy();
                    mc.getTextureManager().bindTexture(BACKGROUND);
                    mc.ingameGUI.drawTexturedModalRect(event.getResolution().getScaledWidth() - 62, event.getResolution().getScaledHeight() - 15, 0, 225, 61, 11);
                    mc.ingameGUI.drawTexturedModalRect(event.getResolution().getScaledWidth() - 62 + 3, event.getResolution().getScaledHeight() - 15 + 3, 0, 236, (int) (55 * progress), 5);
                }
            }

            // 画toast
            if (!toast.isEmpty() && toastTick > 0) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, toastTick < 20 ? toastTick / 20.0f : 1);
                mc.fontRenderer.drawString(toast, (event.getResolution().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(toast) / 2), event.getResolution().getScaledHeight() - 40, 0xffffff);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
            if (toastTick > 0) toastTick--;
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
            event.setRed(120.0f / 255.0f);
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
            GlStateManager.setFogEnd(8 * 0.8F);
            GlStateManager.setFogDensity(0.1F);
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
                flag = random.nextDouble() < 0.4;
                entityData.setBoolean(Reference.MODID + "obsession", flag);
                CPacketEntityData packet = new CPacketEntityData(entityLivingBase.getEntityId(),
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
                CPacketEntityData packet = new CPacketEntityData(entityLivingBase.getEntityId(),
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
