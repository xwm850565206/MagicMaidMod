package com.xwm.magicmaid.entity.model.Rett;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelMagicMaidRettStandard - xwm
 * Created using Tabula 7.1.0
 */
public class ModelMagicMaidRettDemonKillerAttack1 extends ModelMagicMaidRettBone {

    public ModelMagicMaidRettDemonKillerAttack1() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.leftLeg = new ModelRenderer(this, 0, 100);
        this.leftLeg.mirror = true;
        this.leftLeg.setRotationPoint(1.0F, 16.0F, 0.0F);
        this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.partTop = new ModelRenderer(this, 110, 50);
        this.partTop.setRotationPoint(0.0F, -2.0F, -0.5F);
        this.partTop.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.partLeft_1 = new ModelRenderer(this, 110, 50);
        this.partLeft_1.setRotationPoint(1.0F, 0.0F, -0.5F);
        this.partLeft_1.addBox(0.0F, -1.0F, 0.0F, 1, 2, 1, 0.0F);
        this.hairMain = new ModelRenderer(this, 100, 0);
        this.hairMain.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.hairMain.addBox(-3.0F, -6.0F, -3.0F, 6, 9, 6, 0.5F);
        this.partRight_1 = new ModelRenderer(this, 110, 50);
        this.partRight_1.setRotationPoint(-2.0F, 0.0F, -0.5F);
        this.partRight_1.addBox(0.0F, -1.0F, 0.0F, 1, 2, 1, 0.0F);
        this.earRight2 = new ModelRenderer(this, 110, 30);
        this.earRight2.mirror = true;
        this.earRight2.setRotationPoint(-2.2F, 1.0F, 0.0F);
        this.earRight2.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.connect1 = new ModelRenderer(this, 85, 100);
        this.connect1.setRotationPoint(0.0F, -9.0F, 1.0F);
        this.connect1.addBox(-2.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(connect1, -0.017453292519943295F, 0.0F, 0.0F);
        this.cubeRight = new ModelRenderer(this, 110, 110);
        this.cubeRight.setRotationPoint(3.0F, 0.5F, 0.0F);
        this.cubeRight.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.sword5 = new ModelRenderer(this, 25, 15);
        this.sword5.setRotationPoint(-0.5F, 7.0F, 0.0F);
        this.sword5.addBox(-0.5F, -1.0F, -0.5F, 1, 16, 1, 0.0F);
        this.partTop_1 = new ModelRenderer(this, 110, 50);
        this.partTop_1.setRotationPoint(0.0F, -2.0F, -0.5F);
        this.partTop_1.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.earLeft2 = new ModelRenderer(this, 110, 30);
        this.earLeft2.setRotationPoint(-0.2F, 1.0F, 0.0F);
        this.earLeft2.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.sword3 = new ModelRenderer(this, 13, 17);
        this.sword3.setRotationPoint(0.5F, 7.0F, 0.0F);
        this.sword3.addBox(-0.5F, -1.0F, -0.5F, 1, 14, 1, 0.0F);
        this.earLeft = new ModelRenderer(this, 110, 20);
        this.earLeft.setRotationPoint(3.0F, -4.0F, 0.0F);
        this.earLeft.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(earLeft, 0.0F, 0.0F, 0.013439035240356337F);
        this.connect2 = new ModelRenderer(this, 110, 75);
        this.connect2.setRotationPoint(3.0F, 0.7F, 0.0F);
        this.connect2.addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(connect2, 0.0F, 0.0F, 0.5462880558742251F);
        this.partBottom_1 = new ModelRenderer(this, 110, 50);
        this.partBottom_1.setRotationPoint(0.0F, 1.0F, -0.5F);
        this.partBottom_1.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.cubeLeft = new ModelRenderer(this, 110, 110);
        this.cubeLeft.setRotationPoint(-3.0F, 0.5F, 0.0F);
        this.cubeLeft.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.leftArm = new ModelRenderer(this, 35, 48);
        this.leftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        this.sword1 = new ModelRenderer(this, 0, 19);
        this.sword1.setRotationPoint(1.5F, 7.0F, 0.0F);
        this.sword1.addBox(-0.5F, -1.0F, -0.5F, 1, 12, 1, 0.0F);
        this.rightLeg = new ModelRenderer(this, 0, 100);
        this.rightLeg.setRotationPoint(-1.0F, 16.0F, 0.0F);
        this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.dress = new ModelRenderer(this, 36, 100);
        this.dress.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.dress.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8, 0.0F);
        this.body = new ModelRenderer(this, 16, 100);
        this.body.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.body.addBox(-3.0F, 0.0F, -2.0F, 6, 6, 4, 0.0F);
        this.setRotateAngle(body, 0.0F, 0.5235987755982988F, 0.011693705988362007F);
        this.rightSleeve = new ModelRenderer(this, 48, 48);
        this.rightSleeve.mirror = true;
        this.rightSleeve.setRotationPoint(-4.0F, 0.5F, 0.0F);
        this.rightSleeve.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(rightSleeve, -1.0471975511965976F, 0.4553564018453205F, 0.5462880558742251F);
        this.rightArm = new ModelRenderer(this, 35, 48);
        this.rightArm.mirror = true;
        this.rightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        this.sword6 = new ModelRenderer(this, 28, 6);
        this.sword6.setRotationPoint(-1.0F, 7.0F, 0.0F);
        this.sword6.addBox(-0.5F, -1.0F, -0.5F, 1, 3, 1, 0.0F);
        this.handle = new ModelRenderer(this, 0, 0);
        this.handle.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.handle.addBox(-0.5F, -1.0F, -0.5F, 1, 6, 1, 0.0F);
        this.setRotateAngle(handle, 0.0F, -2.367539130330308F, 0.0F);
        this.head = new ModelRenderer(this, 70, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
        this.handlepart = new ModelRenderer(this, 10, 0);
        this.handlepart.setRotationPoint(0.0F, 6.5F, 0.0F);
        this.handlepart.addBox(-3.0F, -1.5F, -0.5F, 6, 1, 1, 0.0F);
        this.partRight = new ModelRenderer(this, 110, 50);
        this.partRight.setRotationPoint(-2.0F, 0.0F, -0.5F);
        this.partRight.addBox(0.0F, -1.0F, 0.0F, 1, 2, 1, 0.0F);
        this.hairpin = new ModelRenderer(this, 110, 100);
        this.hairpin.setRotationPoint(0.0F, -7.5F, -1.0F);
        this.hairpin.addBox(-2.5F, -1.0F, -0.5F, 5, 2, 1, 0.0F);
        this.hairLeftBraid = new ModelRenderer(this, 110, 40);
        this.hairLeftBraid.setRotationPoint(4.0F, 2.5F, 0.0F);
        this.hairLeftBraid.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(hairLeftBraid, -0.8726646259971648F, -1.5707963267948966F, 0.6981317007977318F);
        this.hairRightBraid = new ModelRenderer(this, 110, 40);
        this.hairRightBraid.setRotationPoint(-4.0F, 2.5F, 0.0F);
        this.hairRightBraid.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(hairRightBraid, 0.6981317007977318F, 1.5707963267948966F, 0.8726646259971648F);
        this.connect3 = new ModelRenderer(this, 110, 75);
        this.connect3.mirror = true;
        this.connect3.setRotationPoint(-3.0F, 0.7F, 0.0F);
        this.connect3.addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(connect3, 0.0F, 0.0F, -0.5235987755982988F);
        this.partBottom = new ModelRenderer(this, 110, 50);
        this.partBottom.setRotationPoint(0.0F, 1.0F, -0.5F);
        this.partBottom.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.partLeft = new ModelRenderer(this, 110, 50);
        this.partLeft.setRotationPoint(1.0F, 0.0F, -0.5F);
        this.partLeft.addBox(0.0F, -1.0F, 0.0F, 1, 2, 1, 0.0F);
        this.leftSleeve = new ModelRenderer(this, 48, 48);
        this.leftSleeve.setRotationPoint(4.0F, 0.5F, 0.0F);
        this.leftSleeve.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(leftSleeve, -0.045553093477052F, 0.18203784098300857F, -0.36425021489121656F);
        this.sword2 = new ModelRenderer(this, 6, 18);
        this.sword2.setRotationPoint(1.0F, 7.0F, 0.0F);
        this.sword2.addBox(-0.5F, -1.0F, -0.5F, 1, 13, 1, 0.0F);
        this.sword4 = new ModelRenderer(this, 19, 16);
        this.sword4.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.sword4.addBox(-0.5F, -1.0F, -0.5F, 1, 15, 1, 0.0F);
        this.earRight = new ModelRenderer(this, 110, 20);
        this.earRight.mirror = true;
        this.earRight.setRotationPoint(-3.0F, -4.0F, 0.0F);
        this.earRight.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(earRight, 0.0F, 0.0F, 0.013439035240356337F);
        this.hairLeftBraid.addChild(this.partTop);
        this.hairRightBraid.addChild(this.partLeft_1);
        this.hairRightBraid.addChild(this.partRight_1);
        this.earRight.addChild(this.earRight2);
        this.hairMain.addChild(this.connect1);
        this.hairpin.addChild(this.cubeRight);
        this.handle.addChild(this.sword5);
        this.hairRightBraid.addChild(this.partTop_1);
        this.earLeft.addChild(this.earLeft2);
        this.handle.addChild(this.sword3);
        this.head.addChild(this.earLeft);
        this.connect1.addChild(this.connect2);
        this.hairRightBraid.addChild(this.partBottom_1);
        this.hairpin.addChild(this.cubeLeft);
        this.leftSleeve.addChild(this.leftArm);
        this.handle.addChild(this.sword1);
        this.body.addChild(this.rightSleeve);
        this.rightSleeve.addChild(this.rightArm);
        this.handle.addChild(this.sword6);
        this.rightArm.addChild(this.handle);
        this.hairMain.addChild(this.head);
        this.handle.addChild(this.handlepart);
        this.hairLeftBraid.addChild(this.partRight);
        this.hairMain.addChild(this.hairpin);
        this.connect1.addChild(this.hairLeftBraid);
        this.connect1.addChild(this.hairRightBraid);
        this.connect1.addChild(this.connect3);
        this.hairLeftBraid.addChild(this.partBottom);
        this.hairLeftBraid.addChild(this.partLeft);
        this.body.addChild(this.leftSleeve);
        this.handle.addChild(this.sword2);
        this.handle.addChild(this.sword4);
        this.head.addChild(this.earRight);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.leftLeg.render(f5);
        this.hairMain.render(f5);
        this.rightLeg.render(f5);
        this.dress.render(f5);
        this.body.render(f5);
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
