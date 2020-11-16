package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.model.effect.ModelEffectBox;
import com.xwm.magicmaid.util.Reference;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class MagicRenderRegistry
{
    private static ResourceLocation WARNING = new ResourceLocation(Reference.MODID + ":textures/effect/effect_box.png");
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

    private static void renderBox() {
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

        GlStateManager.color(1f, 0, 0, 0.4f);
        GL11.glLineWidth(4);
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        // FRONT
        bufferbuilder.pos(-0.5, -0.5, -0.5).endVertex();
        bufferbuilder.pos(-0.5, 0.5, -0.5).endVertex();

        bufferbuilder.pos(-0.5, 0.5, -0.5).endVertex();
        bufferbuilder.pos(0.5, 0.5, -0.5).endVertex();

        bufferbuilder.pos(0.5, 0.5, -0.5).endVertex();
        bufferbuilder.pos(0.5, -0.5, -0.5).endVertex();

        bufferbuilder.pos(0.5, -0.5, -0.5).endVertex();
        bufferbuilder.pos(-0.5, -0.5, -0.5).endVertex();

        // BACK
        bufferbuilder.pos(-0.5, -0.5, 0.5).endVertex();
        bufferbuilder.pos(-0.5, 0.5, 0.5).endVertex();

        bufferbuilder.pos(-0.5, 0.5, 0.5).endVertex();
        bufferbuilder.pos(0.5, 0.5, 0.5).endVertex();

        bufferbuilder.pos(0.5, 0.5, 0.5).endVertex();
        bufferbuilder.pos(0.5, -0.5, 0.5).endVertex();

        bufferbuilder.pos(0.5, -0.5, 0.5).endVertex();
        bufferbuilder.pos(-0.5, -0.5, 0.5).endVertex();

        // betweens.
        bufferbuilder.pos(0.5, 0.5, -0.5).endVertex();
        bufferbuilder.pos(0.5, 0.5, 0.5).endVertex();

        bufferbuilder.pos(0.5, -0.5, -0.5).endVertex();
        bufferbuilder.pos(0.5, -0.5, 0.5).endVertex();

        bufferbuilder.pos(-0.5, -0.5, -0.5).endVertex();
        bufferbuilder.pos(-0.5, -0.5, 0.5).endVertex();

        bufferbuilder.pos(-0.5, 0.5, -0.5).endVertex();
        bufferbuilder.pos(-0.5, 0.5, 0.5).endVertex();

        Tessellator.getInstance().draw();
    }

    public static void renderCell(AxisAlignedBB bb)
    {
        Minecraft.getMinecraft().mcProfiler.startSection("magic_maid");
        GlStateManager.pushMatrix();
//        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        double renderPosX = TileEntityRendererDispatcher.staticPlayerX;
        double renderPosY = TileEntityRendererDispatcher.staticPlayerY;
        double renderPosZ = TileEntityRendererDispatcher.staticPlayerZ;

        GlStateManager.translate(-renderPosX + 0.5, -renderPosY + 0.5, -renderPosZ + 0.5);
        GlStateManager.translate((bb.minX + bb.maxX) / 2.0, (bb.minY + bb.maxY) / 2.0, (bb.minZ + bb.maxZ) / 2.0);

        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(bb.maxX -  bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ);
        GlStateManager.color(0.3f, 0.0f, 0.0f,0.2f);
        Iterable<BlockPos> iterable = BlockPos.getAllInBox((int)bb.minX, (int)bb.minY, (int)bb.minZ, (int)bb.maxX, (int)bb.maxY, (int)bb.maxZ);
        Iterator<BlockPos> it = iterable.iterator();
        while (it.hasNext()){
            BlockPos pos = it.next();
            GlStateManager.pushMatrix();
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            renderBox();
            GlStateManager.popMatrix();
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    private static void renderWarningArea(AxisAlignedBB bb)
    {
        Minecraft.getMinecraft().mcProfiler.startSection("magic_maid");
        GlStateManager.pushMatrix();
//        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        double renderPosX = TileEntityRendererDispatcher.staticPlayerX;
        double renderPosY = TileEntityRendererDispatcher.staticPlayerY;
        double renderPosZ = TileEntityRendererDispatcher.staticPlayerZ;

        GlStateManager.translate(-renderPosX + 0.5, -renderPosY + 0.5, -renderPosZ + 0.5);
        GlStateManager.translate((bb.minX + bb.maxX) / 2.0, (bb.minY + bb.maxY) / 2.0, (bb.minZ + bb.maxZ) / 2.0);

        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(bb.maxX -  bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ);
        GlStateManager.color(0.6f, 0.0f, 0.0f,0.2f);

        renderBox();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    public static void renderBoxList()
    {
        for (AxisAlignedBB bb : RENDER_BOX_LIST.values())
            renderWarningArea(bb);
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
