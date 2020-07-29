package com.xwm.magicmaid.entity.model.weapon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * Repentance - xwm
 * Created using Tabula 7.1.0
 */
public class ModelRepentance extends ModelBase {
    public ModelRenderer center;
    public ModelRenderer mainStrip1;
    public ModelRenderer mainStrip2;
    public ModelRenderer mainStrip3;
    public ModelRenderer mainStrip4;
    public ModelRenderer row1;
    public ModelRenderer row2;
    public ModelRenderer row3;
    public ModelRenderer row4;
    public ModelRenderer row5;
    public ModelRenderer row6;
    public ModelRenderer row7;
    public ModelRenderer row8;

    public ModelRepentance() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.row2 = new ModelRenderer(this, 60, 10);
        this.row2.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.row2.addBox(-0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(row2, 0.0F, 0.0F, 1.5707963267948966F);
        this.center = new ModelRenderer(this, 20, 10);
        this.center.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.center.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.row7 = new ModelRenderer(this, 60, 10);
        this.row7.setRotationPoint(-4.2F, 0.0F, 0.0F);
        this.row7.addBox(-0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F);
        this.row6 = new ModelRenderer(this, 60, 10);
        this.row6.setRotationPoint(-3.0F, 2.7F, 0.0F);
        this.row6.addBox(-0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(row6, 0.0F, 0.0F, -0.7853981633974483F);
        this.row4 = new ModelRenderer(this, 60, 10);
        this.row4.setRotationPoint(3.0F, 2.7F, 0.0F);
        this.row4.addBox(-0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(row4, 0.0F, 0.0F, 0.7853981633974483F);
        this.row1 = new ModelRenderer(this, 60, 10);
        this.row1.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.row1.addBox(-0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(row1, 0.0F, 0.0F, 1.5707963267948966F);
        this.row8 = new ModelRenderer(this, 60, 10);
        this.row8.setRotationPoint(4.2F, 0.0F, 0.0F);
        this.row8.addBox(-0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F);
        this.row5 = new ModelRenderer(this, 60, 10);
        this.row5.setRotationPoint(3.0F, -2.7F, 0.0F);
        this.row5.addBox(-0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(row5, 0.0F, 0.0F, -0.7853981633974483F);
        this.mainStrip3 = new ModelRenderer(this, 40, 10);
        this.mainStrip3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mainStrip3.addBox(-0.5F, -7.0F, -0.5F, 1, 14, 1, 0.0F);
        this.setRotateAngle(mainStrip3, 0.0F, 0.0F, 0.7853981633974483F);
        this.mainStrip2 = new ModelRenderer(this, 40, 10);
        this.mainStrip2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mainStrip2.addBox(-0.5F, -8.0F, -0.5F, 1, 16, 1, 0.0F);
        this.setRotateAngle(mainStrip2, 0.0F, 0.0F, 1.5707963267948966F);
        this.mainStrip4 = new ModelRenderer(this, 40, 10);
        this.mainStrip4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mainStrip4.addBox(-0.5F, -7.0F, -0.5F, 1, 14, 1, 0.0F);
        this.setRotateAngle(mainStrip4, 0.0F, 0.0F, -0.7853981633974483F);
        this.mainStrip1 = new ModelRenderer(this, 30, 40);
        this.mainStrip1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mainStrip1.addBox(-0.5F, -8.0F, -0.5F, 1, 20, 1, 0.0F);
        this.row3 = new ModelRenderer(this, 60, 10);
        this.row3.setRotationPoint(-3.0F, -2.7F, 0.0F);
        this.row3.addBox(-0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(row3, 0.0F, 0.0F, 0.7853981633974483F);
        this.center.addChild(this.row2);
        this.center.addChild(this.row7);
        this.center.addChild(this.row6);
        this.center.addChild(this.row4);
        this.center.addChild(this.row1);
        this.center.addChild(this.row8);
        this.center.addChild(this.row5);
        this.center.addChild(this.mainStrip3);
        this.center.addChild(this.mainStrip2);
        this.center.addChild(this.mainStrip4);
        this.center.addChild(this.mainStrip1);
        this.center.addChild(this.row3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.center.offsetX, this.center.offsetY, this.center.offsetZ);
        GlStateManager.translate(this.center.rotationPointX * f5, this.center.rotationPointY * f5, this.center.rotationPointZ * f5);
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.translate(-this.center.offsetX, -this.center.offsetY, -this.center.offsetZ);
        GlStateManager.translate(-this.center.rotationPointX * f5, -this.center.rotationPointY * f5, -this.center.rotationPointZ * f5);
        this.center.render(f5);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
