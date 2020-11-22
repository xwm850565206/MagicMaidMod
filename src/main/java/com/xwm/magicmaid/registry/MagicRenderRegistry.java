package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.entity.model.effect.ModelEffectBox;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;


public class MagicRenderRegistry
{
    private static ResourceLocation WARNING = new ResourceLocation(Reference.MODID + ":textures/effect/effect_box.png");
    private static ModelBase BOX = new ModelEffectBox();
    private static int areaID = 0;
    /**
     * hashmap 方便添加和删除 渲染完后需要删除
     */
    private static HashMap<Integer, AxisAlignedBB> RENDER_BOX_LIST = new HashMap<>();

    private static float vertex_list[][] = new float[][]{
            {-0.5f, -0.5f, -0.5f},
            {0.5f, -0.5f, -0.5f},
            {-0.5f, 0.5f, -0.5f},
            {0.5f, 0.5f, -0.5f},
            {-0.5f, -0.5f, 0.5f},
            {0.5f, -0.5f, 0.5f},
            {-0.5f, 0.5f, 0.5f},
            {0.5f, 0.5f, 0.5f},
    };

    private static int[] index_list = new int[]{
            0, 2, 3, 1,
            0, 4, 6, 2,
            0, 1, 5, 4,
            4, 5, 7, 6,
            1, 3, 7, 5,
            2, 6, 7, 3,
    };

    private static void renderBox(int scalex, int scaley, int scalez) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        for (int i = 0; i < index_list.length; i += 4) {
            for (int j = 0; j < 4; j++) {
                int k = index_list[i+j];
                bufferbuilder.pos(vertex_list[k][0], vertex_list[k][1], vertex_list[k][2]).endVertex();
            }
        }
        Tessellator.getInstance().draw();

        GlStateManager.color(1f, 0, 0, 1f);
        GL11.glLineWidth(0.01f);
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        if (scalex == 0 || scaley == 0 || scalez == 0)
            return;

        double dx = 0.5 / scalex;
        double dy = 0.5 / scaley;
        double dz = 0.5 / scalez;

//        double dx = 1;
//        double dy = 1;
//        double dz = 1;

        // FRONT
        for (double x = - 0.5; x <= 0.5; x += dx ){
            bufferbuilder.pos(x, -0.5, -0.5).endVertex();
            bufferbuilder.pos(x, 0.5, -0.5).endVertex();
        }

        for (double y = - 0.5; y <= 0.5; y += dy ){
            bufferbuilder.pos(-0.5, y, -0.5).endVertex();
            bufferbuilder.pos(0.5, y, -0.5).endVertex();
        }
//        bufferbuilder.pos(-0.5, -0.5, -0.5).endVertex();
//        bufferbuilder.pos(-0.5, 0.5, -0.5).endVertex();

//        bufferbuilder.pos(-0.5, 0.5, -0.5).endVertex();
//        bufferbuilder.pos(0.5, 0.5, -0.5).endVertex();

//        bufferbuilder.pos(0.5, 0.5, -0.5).endVertex();
//        bufferbuilder.pos(0.5, -0.5, -0.5).endVertex();

//        bufferbuilder.pos(0.5, -0.5, -0.5).endVertex();
//        bufferbuilder.pos(-0.5, -0.5, -0.5).endVertex();

        // BACK
        for (double x = - 0.5; x <= 0.5; x += dx ){
            bufferbuilder.pos(x, -0.5, 0.5).endVertex();
            bufferbuilder.pos(x, 0.5, 0.5).endVertex();
        }

        for (double y = - 0.5; y<= 0.5; y += dy ){
            bufferbuilder.pos(-0.5, y, 0.5).endVertex();
            bufferbuilder.pos(0.5, y, 0.5).endVertex();
        }
//        bufferbuilder.pos(-0.5, -0.5, 0.5).endVertex();
//        bufferbuilder.pos(-0.5, 0.5, 0.5).endVertex();
//
//        bufferbuilder.pos(-0.5, 0.5, 0.5).endVertex();
//        bufferbuilder.pos(0.5, 0.5, 0.5).endVertex();
//
//        bufferbuilder.pos(0.5, 0.5, 0.5).endVertex();
//        bufferbuilder.pos(0.5, -0.5, 0.5).endVertex();
//
//        bufferbuilder.pos(0.5, -0.5, 0.5).endVertex();
//        bufferbuilder.pos(-0.5, -0.5, 0.5).endVertex();

        // betweens.
        for (double z = - 0.5; z <= 0.5; z += dz ){
            bufferbuilder.pos(0.5, -0.5, z).endVertex();
            bufferbuilder.pos(0.5, 0.5, z).endVertex();
        }

        for (double y = - 0.5; y<= 0.5; y += dy ){
            bufferbuilder.pos(0.5, y, -0.5).endVertex();
            bufferbuilder.pos(0.5, y, 0.5).endVertex();
        }

        for (double z = - 0.5; z <= 0.5; z += dz ){
            bufferbuilder.pos(-0.5, -0.5, z).endVertex();
            bufferbuilder.pos(-0.5, 0.5, z).endVertex();
        }

        for (double y = - 0.5; y <= 0.5; y += dy ){
            bufferbuilder.pos(-0.5, y, -0.5).endVertex();
            bufferbuilder.pos(-0.5, y, 0.5).endVertex();
        }

        for (double z = - 0.5; z <= 0.5; z += dz ){
            bufferbuilder.pos(-0.5, -0.5, z).endVertex();
            bufferbuilder.pos(0.5, -0.5, z).endVertex();
        }

        for (double x = - 0.5; x <= 0.5; x += dx ){
            bufferbuilder.pos(x, -0.5, -0.5).endVertex();
            bufferbuilder.pos(x, -0.5, 0.5).endVertex();
        }


        for (double z = - 0.5; z <= 0.5; z += dz ){
            bufferbuilder.pos(-0.5, 0.5, z).endVertex();
            bufferbuilder.pos(0.5, 0.5, z).endVertex();
        }

        for (double x = - 0.5; x <= 0.5; x += dx ){
            bufferbuilder.pos(x, 0.5, -0.5).endVertex();
            bufferbuilder.pos(x, 0.5, 0.5).endVertex();
        }
//        bufferbuilder.pos(0.5, 0.5, -0.5).endVertex();
//        bufferbuilder.pos(0.5, 0.5, 0.5).endVertex();
//
//        bufferbuilder.pos(0.5, -0.5, -0.5).endVertex();
//        bufferbuilder.pos(0.5, -0.5, 0.5).endVertex();
//
//        bufferbuilder.pos(-0.5, -0.5, -0.5).endVertex();
//        bufferbuilder.pos(-0.5, -0.5, 0.5).endVertex();
//
//        bufferbuilder.pos(-0.5, 0.5, -0.5).endVertex();
//        bufferbuilder.pos(-0.5, 0.5, 0.5).endVertex();

        Tessellator.getInstance().draw();
    }

    @SideOnly(Side.CLIENT)
    public static void renderCell(AxisAlignedBB bb)
    {
        Minecraft.getMinecraft().mcProfiler.startSection("magic_maid");
        GlStateManager.pushMatrix();
//        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableBlend();
//        GlStateManager.enableDepth();
//        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        double renderPosX = TileEntityRendererDispatcher.staticPlayerX;
        double renderPosY = TileEntityRendererDispatcher.staticPlayerY;
        double renderPosZ = TileEntityRendererDispatcher.staticPlayerZ;

        GlStateManager.translate(-renderPosX + 0.5, -renderPosY + 0.5, -renderPosZ + 0.5);
//        GlStateManager.translate((bb.minX + bb.maxX) / 2.0, (bb.minY + bb.maxY) / 2.0, (bb.minZ + bb.maxZ) / 2.0);

//        GlStateManager.enableRescaleNormal();
//        GlStateManager.scale(bb.maxX -  bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ);
//        GlStateManager.color(0.3f, 0.0f, 0.0f,0.2f);
        GlStateManager.color(1, 1, 1, 0.5f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(WARNING);
        Iterable<BlockPos> iterable = BlockPos.getAllInBox((int)bb.minX, (int)bb.minY, (int)bb.minZ, (int)bb.maxX, (int)bb.maxY, (int)bb.maxZ);
        Iterator<BlockPos> it = iterable.iterator();
        while (it.hasNext()){
            BlockPos pos = it.next();
            GlStateManager.pushMatrix();
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            GlStateManager.scale(0.125, 0.125, 0.125);

            BOX.render(null, 0, 0, 0, 0, 0, 1);

            GlStateManager.popMatrix();
        }

        RenderHelper.disableStandardItemLighting();
//        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
//        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @SideOnly(Side.CLIENT)
    private static void renderWarningArea(AxisAlignedBB bb)
    {
        Minecraft.getMinecraft().mcProfiler.startSection("magic_maid");
        GlStateManager.pushMatrix();
//        GlStateManager.loadIdentity();
//        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
//        GlStateManager.enableColorMaterial();
        GlStateManager.enableBlend();
//        GlStateManager.disableDepth();
        GlStateManager.enableDepth();

        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        double renderPosX = TileEntityRendererDispatcher.staticPlayerX;
        double renderPosY = TileEntityRendererDispatcher.staticPlayerY;
        double renderPosZ = TileEntityRendererDispatcher.staticPlayerZ;

        GlStateManager.translate(-renderPosX + 0.5, -renderPosY + 0.5, -renderPosZ + 0.5);
        GlStateManager.translate((bb.minX + bb.maxX) / 2.0, (bb.minY + bb.maxY) / 2.0, (bb.minZ + bb.maxZ) / 2.0);
//        GlStateManager.scale(0.125, 0.125, 0.125);
        GlStateManager.scale(bb.maxX -  bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ);
        GlStateManager.color(0.6f, 0.0f, 0.0f,0.2f);
//        Minecraft.getMinecraft().getTextureManager().bindTexture(WARNING);
//        GlStateManager.color(1, 1, 1, 0.5f);

//        BOX.render(null, 0, 0, 0, 0, 0, 1);
        GlStateManager.enableRescaleNormal();

        renderBox((int)(bb.maxX -  bb.minX), (int)(bb.maxY - bb.minY), (int)(bb.maxZ - bb.minZ));

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
//        GlStateManager.disableColorMaterial();
//        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    static int tick = 0;
    @SideOnly(Side.CLIENT)
    public static void renderBoxList()
    {
        tick++;
        try {
            for (AxisAlignedBB bb : RENDER_BOX_LIST.values())
            renderWarningArea(bb);
//                renderCell(bb);
        } catch (ConcurrentModificationException exception)
        {
            ; //线程间会修改这个LIST
        }
        if (tick > 400) {
            synchronized(RENDER_BOX_LIST) { //This may be access multiple times, from multiple threads, lets be safe.
                RENDER_BOX_LIST.clear();
            }
            tick = 0;
        }
    }

    public static int allocateArea()
    {
        if (areaID >= Integer.MAX_VALUE)
            return -1;
        return areaID++;
    }

    public static void addRenderBox(int i, AxisAlignedBB bb)
    {
        RENDER_BOX_LIST.put(i, bb);
    }

    public static void removeRenderBox(int i)
    {
        if (RENDER_BOX_LIST.containsKey(i))
            RENDER_BOX_LIST.remove(i);
    }
}
