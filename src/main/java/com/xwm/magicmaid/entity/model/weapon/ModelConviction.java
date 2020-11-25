package com.xwm.magicmaid.entity.model.weapon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

/**
 * conviction - xwm
 * Created using Tabula 7.1.0
 */
public class ModelConviction extends ModelBase {
	public ModelRenderer center;
	public ModelRenderer slope1;
	public ModelRenderer slope2;
	public ModelRenderer slope3;
	public ModelRenderer slope4;
	public ModelRenderer slope5;
	public ModelRenderer slope6;
	public ModelRenderer slope7;
	public ModelRenderer slope8;
	public ModelRenderer stick1;
	public ModelRenderer stick2;
	public ModelRenderer stick3;
	public ModelRenderer stick4;
	public ModelRenderer stick5;
	public ModelRenderer stick6;
	public ModelRenderer stick7;
	public ModelRenderer stick8;
	public ModelRenderer angle1;
	public ModelRenderer angle2;
	public ModelRenderer angle3;
	public ModelRenderer angle4;
	public ModelRenderer tip1;
	public ModelRenderer tip2;
	public ModelRenderer tip3;
	public ModelRenderer tip4;
	public ModelRenderer core;

	public ModelConviction() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.stick1 = new ModelRenderer(this, 40, 0);
		this.stick1.setRotationPoint(0.0F, -4.0F, -2.5F);
		this.stick1.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.stick3 = new ModelRenderer(this, 40, 5);
		this.stick3.setRotationPoint(0.0F, 4.0F, 2.5F);
		this.stick3.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.slope2 = new ModelRenderer(this, 20, 0);
		this.slope2.mirror = true;
		this.slope2.setRotationPoint(1.5F, -2.5F, 1.5F);
		this.slope2.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(slope2, 1.0471975511965976F, 0.0F, 0.5235987755982988F);
		this.stick7 = new ModelRenderer(this, 40, 5);
		this.stick7.setRotationPoint(-2.5F, 4.0F, 0.0F);
		this.stick7.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.setRotateAngle(stick7, 0.0F, 1.5707963267948966F, 0.0F);
		this.tip4 = new ModelRenderer(this, 0, 20);
		this.tip4.setRotationPoint(3.5F, -5.0F, 4.0F);
		this.tip4.addBox(0.0F, -0.5F, -0.5F, 6, 1, 1, 0.0F);
		this.setRotateAngle(tip4, 0.0F, 0.5235987755982988F, -2.0943951023931953F);
		this.tip2 = new ModelRenderer(this, 0, 20);
		this.tip2.setRotationPoint(3.5F, -5.0F, -4.0F);
		this.tip2.addBox(0.0F, -0.5F, -0.5F, 6, 1, 1, 0.0F);
		this.setRotateAngle(tip2, 0.0F, -0.5235987755982988F, -2.0943951023931953F);
		this.core = new ModelRenderer(this, 50, 20);
		this.core.setRotationPoint(0.0F, -11.0F, 0.0F);
		this.core.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
		this.setRotateAngle(core, -0.7853981633974483F, 0.0F, -0.7853981633974483F);
		this.slope3 = new ModelRenderer(this, 20, 0);
		this.slope3.mirror = true;
		this.slope3.setRotationPoint(-1.5F, -2.5F, -1.5F);
		this.slope3.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(slope3, 2.0943951023931953F, 0.0F, -0.5235987755982988F);
		this.stick8 = new ModelRenderer(this, 40, 5);
		this.stick8.setRotationPoint(2.5F, 4.0F, 0.0F);
		this.stick8.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.setRotateAngle(stick8, 0.0F, 1.5707963267948966F, 0.0F);
		this.slope7 = new ModelRenderer(this, 20, 0);
		this.slope7.setRotationPoint(-1.5F, 2.5F, -1.5F);
		this.slope7.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(slope7, -1.0471975511965976F, -3.141592653589793F, 0.5235987755982988F);
		this.slope5 = new ModelRenderer(this, 20, 0);
		this.slope5.mirror = true;
		this.slope5.setRotationPoint(-1.5F, 2.5F, 1.5F);
		this.slope5.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(slope5, -2.0943951023931953F, -3.141592653589793F, 0.5235987755982988F);
		this.tip3 = new ModelRenderer(this, 0, 20);
		this.tip3.setRotationPoint(-3.5F, -5.0F, 4.0F);
		this.tip3.addBox(0.0F, -0.5F, -0.5F, 6, 1, 1, 0.0F);
		this.setRotateAngle(tip3, 0.0F, 0.5235987755982988F, -1.0471975511965976F);
		this.angle2 = new ModelRenderer(this, 40, 20);
		this.angle2.setRotationPoint(2.5F, -4.0F, -2.5F);
		this.angle2.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
		this.setRotateAngle(angle2, 0.0F, 0.6283185307179586F, -1.0471975511965976F);
		this.angle1 = new ModelRenderer(this, 40, 20);
		this.angle1.setRotationPoint(-2.5F, -4.0F, -2.5F);
		this.angle1.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
		this.setRotateAngle(angle1, 0.0F, 0.6283185307179586F, -2.0943951023931953F);
		this.stick2 = new ModelRenderer(this, 40, 0);
		this.stick2.setRotationPoint(0.0F, -4.0F, 2.5F);
		this.stick2.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.stick4 = new ModelRenderer(this, 40, 5);
		this.stick4.setRotationPoint(0.0F, 4.1F, -2.5F);
		this.stick4.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.stick6 = new ModelRenderer(this, 40, 0);
		this.stick6.setRotationPoint(2.5F, -4.0F, 0.0F);
		this.stick6.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.setRotateAngle(stick6, 0.0F, 1.5707963267948966F, 0.0F);
		this.slope6 = new ModelRenderer(this, 20, 0);
		this.slope6.setRotationPoint(1.5F, 2.5F, 1.5F);
		this.slope6.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(slope6, -2.0943951023931953F, -3.141592653589793F, -0.5235987755982988F);
		this.center = new ModelRenderer(this, 0, 0);
		this.center.setRotationPoint(0.0F, 21.0F, 0.0F);
		this.center.addBox(-2.0F, -3.0F, -2.0F, 4, 6, 4, 0.0F);
		this.slope4 = new ModelRenderer(this, 20, 0);
		this.slope4.setRotationPoint(1.5F, -2.5F, -1.5F);
		this.slope4.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(slope4, 2.0943951023931953F, 0.0F, 0.5235987755982988F);
		this.stick5 = new ModelRenderer(this, 40, 0);
		this.stick5.setRotationPoint(-2.5F, -4.0F, 0.0F);
		this.stick5.addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
		this.setRotateAngle(stick5, 0.0F, 1.5707963267948966F, 0.0F);
		this.slope8 = new ModelRenderer(this, 20, 0);
		this.slope8.setRotationPoint(1.5F, 2.5F, -1.5F);
		this.slope8.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(slope8, -1.0471975511965976F, -3.141592653589793F, -0.5235987755982988F);
		this.angle4 = new ModelRenderer(this, 40, 20);
		this.angle4.setRotationPoint(2.5F, -4.0F, 2.5F);
		this.angle4.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
		this.setRotateAngle(angle4, 0.0F, -0.6283185307179586F, -1.0471975511965976F);
		this.angle3 = new ModelRenderer(this, 40, 20);
		this.angle3.setRotationPoint(-2.5F, -4.0F, 2.5F);
		this.angle3.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
		this.setRotateAngle(angle3, 0.0F, -0.6283185307179586F, -2.0943951023931953F);
		this.slope1 = new ModelRenderer(this, 20, 0);
		this.slope1.setRotationPoint(-1.5F, -2.5F, 1.5F);
		this.slope1.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(slope1, 1.0471975511965976F, 0.0F, -0.5235987755982988F);
		this.tip1 = new ModelRenderer(this, 0, 20);
		this.tip1.setRotationPoint(-3.5F, -5.0F, -4.0F);
		this.tip1.addBox(0.0F, -0.5F, -0.5F, 6, 1, 1, 0.0F);
		this.setRotateAngle(tip1, 0.0F, -0.5235987755982988F, -1.0471975511965976F);
		this.center.addChild(this.stick1);
		this.center.addChild(this.stick3);
		this.center.addChild(this.slope2);
		this.center.addChild(this.stick7);
		this.center.addChild(this.tip4);
		this.center.addChild(this.tip2);
		this.center.addChild(this.core);
		this.center.addChild(this.slope3);
		this.center.addChild(this.stick8);
		this.center.addChild(this.slope7);
		this.center.addChild(this.slope5);
		this.center.addChild(this.tip3);
		this.center.addChild(this.angle2);
		this.center.addChild(this.angle1);
		this.center.addChild(this.stick2);
		this.center.addChild(this.stick4);
		this.center.addChild(this.stick6);
		this.center.addChild(this.slope6);
		this.center.addChild(this.slope4);
		this.center.addChild(this.stick5);
		this.center.addChild(this.slope8);
		this.center.addChild(this.angle4);
		this.center.addChild(this.angle3);
		this.center.addChild(this.slope1);
		this.center.addChild(this.tip1);
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
