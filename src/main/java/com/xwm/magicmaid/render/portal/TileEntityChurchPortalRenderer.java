package com.xwm.magicmaid.render.portal;


import com.xwm.magicmaid.object.tileentity.TileEntityChurchPortal;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class TileEntityChurchPortalRenderer extends TileEntitySpecialRenderer<TileEntityChurchPortal>
{
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("minecraft:textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation(Reference.MODID + ":textures/effect/effect_box.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

    public void render(TileEntityChurchPortal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        renderBackground(te, x, y, z);
        renderFront(te, x, y, z);
    }

    protected void renderBackground(TileEntityChurchPortal te, double x, double y, double z)
    {
        GlStateManager.disableLighting();
        GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, MODELVIEW);
        GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, PROJECTION);
        float f = this.getOffset();
        double d0 = x * x + y * y + z * z;
        GlStateManager.pushMatrix();
        this.bindTexture(END_SKY_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
        GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
        GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
        GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
        GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
        GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
        GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.5F, 0.5F, 0.0F);
        GlStateManager.scale(0.5F, 0.5F, 1.0F);
        float f2 = 1;
        GlStateManager.translate(17.0F / f2, (2.0F + f2 / 1.5F) * ((float)Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
        GlStateManager.rotate((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);
        GlStateManager.multMatrix(PROJECTION);
        GlStateManager.multMatrix(MODELVIEW);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;

        if (te.shouldRenderFace(EnumFacing.SOUTH))
        {
            bufferbuilder.pos(x, y, z + 0.5D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 1.0D, y, z + 0.5D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 1.0D, y + 1.0D, z + 0.5D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x, y + 1.0D, z + 0.5D).color(f3, f4, f5, 1.0F).endVertex();
        }

        if (te.shouldRenderFace(EnumFacing.NORTH))
        {
            bufferbuilder.pos(x, y + 1.0D, z + 0.5D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 1.0D, y + 1.0D, z + 0.5D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 1.0D, y, z + 0.5D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x, y, z + 0.5D).color(f3, f4, f5, 1.0F).endVertex();
        }

        if (te.shouldRenderFace(EnumFacing.EAST))
        {
            bufferbuilder.pos(x + 0.5D, y + 1.0D, z).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 0.5D, y + 1.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 0.5D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 0.5D, y, z).color(f3, f4, f5, 1.0F).endVertex();
        }

        if (te.shouldRenderFace(EnumFacing.WEST))
        {
            bufferbuilder.pos(x + 0.5D, y, z).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 0.5D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 0.5D, y + 1.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
            bufferbuilder.pos(x + 0.5D, y + 1.0D, z).color(f3, f4, f5, 1.0F).endVertex();
        }


        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);


        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.enableLighting();
    }

    protected void renderFront(TileEntityChurchPortal te, double x, double y, double z)
    {
        GlStateManager.disableLighting();
        GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, MODELVIEW);
        GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();

//        GlStateManager.enableBlend();
//        GlStateManager.disableDepth();
//        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        GlStateManager.loadIdentity();
        GlStateManager.translate(0.5F, 0.5F, 0.0F);
        GlStateManager.scale(0.5F, 0.5F, 1.0F);

        float f2 = 1;
        float f3 = 1.0f;
        float f4 = 1.0f;
        float f5 = 1.0f;

        GlStateManager.translate(17.0F / f2, (2.0F + f2 / 1.5F) * ((float)Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
        GlStateManager.rotate((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);

        GlStateManager.multMatrix(PROJECTION);
        GlStateManager.multMatrix(MODELVIEW);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.color(f3, f4, f5);
        GlStateManager.glLineWidth(4);
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        if (te.shouldRenderFace(EnumFacing.SOUTH))
        {
            bufferbuilder.pos(x + RANDOM.nextDouble(), y + RANDOM.nextDouble(), z + 0.5D).endVertex();
            bufferbuilder.pos(x  + RANDOM.nextDouble(), y + RANDOM.nextDouble(), z + 0.5D).endVertex();
        }

        if (te.shouldRenderFace(EnumFacing.NORTH))
        {
            bufferbuilder.pos(x + RANDOM.nextDouble(), y + RANDOM.nextDouble(), z + 0.5D).endVertex();
            bufferbuilder.pos(x + RANDOM.nextDouble(), y + RANDOM.nextDouble(), z + 0.5D).endVertex();
        }

        if (te.shouldRenderFace(EnumFacing.EAST))
        {
            bufferbuilder.pos(x + 0.5D, y + RANDOM.nextDouble(), z + RANDOM.nextDouble()).endVertex();
            bufferbuilder.pos(x + 0.5D, y + RANDOM.nextDouble(), z + + RANDOM.nextDouble()).endVertex();
        }

        if (te.shouldRenderFace(EnumFacing.WEST))
        {
            bufferbuilder.pos(x + 0.5D, y + RANDOM.nextDouble(), z + RANDOM.nextDouble()).endVertex();
            bufferbuilder.pos(x + 0.5D, y + RANDOM.nextDouble(), z + RANDOM.nextDouble()).endVertex();
        }


        tessellator.draw();

        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableLighting();
//        GlStateManager.enableDepth();
    }

    protected float getOffset()
    {
        return 0.75F;
    }

    private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
    {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }
}
