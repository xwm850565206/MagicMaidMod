package com.xwm.magicmaid.entity.model.weapon;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * PandorasBox - xwm
 * Created using Tabula 7.1.0
 */
public class ModelPandorasBox extends ModelBase {

    private static float FLOAT_HEIGHT = 0.25f;
    private boolean flag = false; //控制盒子开关动画

    public ModelRenderer core;
    public ModelRenderer frontside;
    public ModelRenderer backside;
    public ModelRenderer leftside;
    public ModelRenderer rightside;
    public ModelRenderer topside;
    public ModelRenderer buttonside;
    public ModelRenderer stickfb;
    public ModelRenderer sticklr;
    public ModelRenderer sticktb;

    public ModelPandorasBox() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.rightside = new ModelRenderer(this, 60, 0);
        this.rightside.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.rightside.addBox(-8.0F, -8.0F, -0.5F, 16, 16, 1, 0.0F);
        this.setRotateAngle(rightside, 0.0F, 1.5707963267948966F, 0.0F);
        this.leftside = new ModelRenderer(this, 60, 30);
        this.leftside.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.leftside.addBox(-8.0F, -8.0F, -0.5F, 16, 16, 1, 0.0F);
        this.setRotateAngle(leftside, 0.0F, 1.5707963267948966F, 0.0F);
        this.frontside = new ModelRenderer(this, 60, 70);
        this.frontside.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.frontside.addBox(-8.0F, -8.0F, -0.5F, 16, 16, 1, 0.0F);
        this.sticklr = new ModelRenderer(this, 60, 100);
        this.sticklr.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.sticklr.addBox(-1.0F, -1.0F, -8.0F, 2, 2, 16, 0.0F);
        this.setRotateAngle(sticklr, 0.0F, 1.5707963267948966F, 0.0F);
        this.buttonside = new ModelRenderer(this, 0, 30);
        this.buttonside.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.buttonside.addBox(-8.0F, -8.0F, -0.5F, 16, 16, 1, 0.0F);
        this.setRotateAngle(buttonside, 1.5707963267948966F, 0.0F, 0.0F);
        this.backside = new ModelRenderer(this, 60, 50);
        this.backside.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.backside.addBox(-8.0F, -8.0F, -0.5F, 16, 16, 1, 0.0F);
        this.sticktb = new ModelRenderer(this, 90, 80);
        this.sticktb.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.sticktb.addBox(-1.0F, -1.0F, -8.0F, 2, 2, 16, 0.0F);
        this.setRotateAngle(sticktb, 1.5707963267948966F, 0.0F, 0.0F);
        this.topside = new ModelRenderer(this, 0, 0);
        this.topside.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.topside.addBox(-8.0F, -8.0F, -0.5F, 16, 16, 1, 0.0F);
        this.setRotateAngle(topside, 1.5707963267948966F, 0.0F, 0.0F);
        this.stickfb = new ModelRenderer(this, 0, 80);
        this.stickfb.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.stickfb.addBox(-1.0F, -1.0F, -8.0F, 2, 2, 16, 0.0F);
        this.core = new ModelRenderer(this, 0, 110);
        this.core.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.core.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.core.addChild(this.rightside);
        this.core.addChild(this.leftside);
        this.core.addChild(this.frontside);
        this.core.addChild(this.sticklr);
        this.core.addChild(this.buttonside);
        this.core.addChild(this.backside);
        this.core.addChild(this.sticktb);
        this.core.addChild(this.topside);
        this.core.addChild(this.stickfb);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.core.offsetX, this.core.offsetY, this.core.offsetZ);
        GlStateManager.translate(this.core.rotationPointX * f5, this.core.rotationPointY * f5, this.core.rotationPointZ * f5);
        GlStateManager.scale(0.25D, 0.25D, 0.25D);
        GlStateManager.translate(-this.core.offsetX, -this.core.offsetY, -this.core.offsetZ);
        GlStateManager.translate(-this.core.rotationPointX * f5, -this.core.rotationPointY * f5, -this.core.rotationPointZ * f5);
        this.core.render(f5);
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

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        EntityMaidWeaponPandorasBox pandorasBox = (EntityMaidWeaponPandorasBox) entitylivingbaseIn;
        if (pandorasBox.isOpen() && !flag)
        {
            this.frontside.offsetZ -= 1;
            this.backside.offsetZ += 1;
            this.leftside.offsetX -= 1;
            this.rightside.offsetX += 1;
            this.topside.offsetY -= 1;
            this.buttonside.offsetY += 1;
            this.flag = true;
        }
        else if (!pandorasBox.isOpen() && flag){
            this.frontside.offsetZ += 1;
            this.backside.offsetZ -= 1;
            this.leftside.offsetX += 1;
            this.rightside.offsetX -= 1;
            this.topside.offsetY += 1;
            this.buttonside.offsetY -= 1;
            this.flag = false;
        }
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        EntityMaidWeaponPandorasBox pandorasBox = (EntityMaidWeaponPandorasBox) entityIn;
        this.core.rotateAngleY = 4 * MathHelper.cos((float) Math.toRadians(pandorasBox.tick * 5));

        float f = (pandorasBox.tick / 9.0f);
        while (f > 10) f -= 10;
        this.core.offsetY = FLOAT_HEIGHT / 5.0f * MathHelper.abs(f - 5.0f);
    }
}
