package com.xwm.magicmaid.entity.model.weapon;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * ModelWhisper - xwm
 * Created using Tabula 7.1.0
 */
public class ModelWhisper extends ModelBase {

    private static final float FLOAT_HEIGHT = 0.2f;
    private boolean flag = false;

    public ModelRenderer handle;
    public ModelRenderer powermain;
    public ModelRenderer head1;
    public ModelRenderer head2;
    public ModelRenderer head3;
    public ModelRenderer head4;
    public ModelRenderer powerpart1;
    public ModelRenderer powerpart2;
    public ModelRenderer handlepart1;
    public ModelRenderer handlepart2;
    public ModelRenderer headpart1;
    public ModelRenderer headpart1_1;
    public ModelRenderer headpart1_2;
    public ModelRenderer headpart1_3;


    public ModelWhisper() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.handle = new ModelRenderer(this, 56, 0);
        this.handle.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.handle.addBox(-0.5F, -9.0F, -0.5F, 1, 22, 1, 0.0F);
        this.powermain = new ModelRenderer(this, 0, 57);
        this.powermain.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.powermain.addBox(-1.0F, -2.0F, -1.0F, 2, 4, 2, 0.0F);
        this.headpart1_3 = new ModelRenderer(this, 8, 40);
        this.headpart1_3.mirror = true;
        this.headpart1_3.setRotationPoint(-0.5F, -2.0F, -2.0F);
        this.headpart1_3.addBox(0.0F, -2.5F, 0.0F, 1, 5, 1, 0.0F);
        this.handlepart2 = new ModelRenderer(this, 20, 20);
        this.handlepart2.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.handlepart2.addBox(-1.5F, -1.0F, -1.5F, 3, 2, 3, 0.0F);
        this.headpart1 = new ModelRenderer(this, 0, 40);
        this.headpart1.setRotationPoint(-0.5F, -2.0F, -2.0F);
        this.headpart1.addBox(0.0F, -2.5F, 0.0F, 1, 5, 1, 0.0F);
        this.powerpart1 = new ModelRenderer(this, 0, 20);
        this.powerpart1.setRotationPoint(-2.5F, -13.0F, 0.0F);
        this.powerpart1.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F);
        this.headpart1_2 = new ModelRenderer(this, 8, 40);
        this.headpart1_2.setRotationPoint(-0.5F, -2.0F, -2.0F);
        this.headpart1_2.addBox(0.0F, -2.5F, 0.0F, 1, 5, 1, 0.0F);
        this.head3 = new ModelRenderer(this, 19, 57);
        this.head3.setRotationPoint(-3.0F, -8.5F, 0.0F);
        this.head3.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(head3, 0.0F, 1.5707963267948966F, 0.0F);
        this.head4 = new ModelRenderer(this, 19, 57);
        this.head4.mirror = true;
        this.head4.setRotationPoint(3.0F, -8.5F, 0.0F);
        this.head4.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(head4, 0.0F, -1.5707963267948966F, 0.0F);
        this.head2 = new ModelRenderer(this, 9, 57);
        this.head2.mirror = true;
        this.head2.setRotationPoint(0.0F, -8.5F, 3.0F);
        this.head2.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(head2, 0.0F, -3.141592653589793F, 0.0F);
        this.head1 = new ModelRenderer(this, 9, 57);
        this.head1.setRotationPoint(0.0F, -8.5F, -3.0F);
        this.head1.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 3, 0.0F);
        this.powerpart2 = new ModelRenderer(this, 0, 20);
        this.powerpart2.mirror = true;
        this.powerpart2.setRotationPoint(2.5F, -13.0F, 0.0F);
        this.powerpart2.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F);
        this.handlepart1 = new ModelRenderer(this, 20, 0);
        this.handlepart1.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.handlepart1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.headpart1_1 = new ModelRenderer(this, 0, 40);
        this.headpart1_1.mirror = true;
        this.headpart1_1.setRotationPoint(-0.5F, -2.0F, -2.0F);
        this.headpart1_1.addBox(0.0F, -2.5F, 0.0F, 1, 5, 1, 0.0F);
        this.handle.addChild(this.powermain);
        this.head4.addChild(this.headpart1_3);
        this.handle.addChild(this.handlepart2);
        this.head1.addChild(this.headpart1);
        this.handle.addChild(this.powerpart1);
        this.head3.addChild(this.headpart1_2);
        this.handle.addChild(this.head3);
        this.handle.addChild(this.head4);
        this.handle.addChild(this.head2);
        this.handle.addChild(this.head1);
        this.handle.addChild(this.powerpart2);
        this.handle.addChild(this.handlepart1);
        this.head2.addChild(this.headpart1_1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.handle.offsetX, this.handle.offsetY, this.handle.offsetZ);
        GlStateManager.translate(this.handle.rotationPointX * f5, this.handle.rotationPointY * f5, this.handle.rotationPointZ * f5);
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.translate(-this.handle.offsetX, -this.handle.offsetY, -this.handle.offsetZ);
        GlStateManager.translate(-this.handle.rotationPointX * f5, -this.handle.rotationPointY * f5, -this.handle.rotationPointZ * f5);
        this.handle.render(f5);
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
//        EntityMaidWeaponWhisper whisper = (EntityMaidWeaponWhisper) entitylivingbaseIn;
//        if (whisper.isAttack())
//        {
//            if (!flag) {
//                this.handle.rotateAngleZ = 90;
//                this.flag = true;
//            }
//            if (whisper.maid != null){
//                EnumFacing facing = whisper.maid.getHorizontalFacing();
//                this.handle.rotateAngleX = facing.getHorizontalAngle();
//            }
//        }
//        else if (!whisper.isAttack() && flag){
//            this.handle.rotateAngleZ = 0;
//            this.handle.rotateAngleX = 0;
//            this.flag = false;
//        }
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        EntityMaidWeaponWhisper whisper = (EntityMaidWeaponWhisper) entityIn;
        this.powerpart1.rotateAngleY = 2 * MathHelper.cos((float) Math.toRadians(whisper.tick * 5));
        this.powerpart2.rotateAngleY = 2 * MathHelper.cos((float) Math.toRadians(whisper.tick * 5));
        if (whisper.isAttack()){
            this.handle.rotateAngleY = 8 * MathHelper.cos((float) Math.toRadians(whisper.tick * 5));
        }
        float f = (whisper.tick / 9.0f);
        while (f > 10) f -= 10;
        this.powermain.offsetY = FLOAT_HEIGHT / 5.0f * MathHelper.abs(f - 5.0f);
    }
}
