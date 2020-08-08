package com.xwm.magicmaid.entity.model.martha;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMartha;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelMagicMaidMartha extends ModelMagicMaidMarthaBone
{
    private static ModelMagicMaidMarthaStandard standard = new ModelMagicMaidMarthaStandard();
    private static ModelMagicMaidMarthaWalk walk = new ModelMagicMaidMarthaWalk();
    private static ModelMagicMaidMarthaNormalStand normalStand = new ModelMagicMaidMarthaNormalStand();
    private static ModelMagicMaidMarthaAttack attack = new ModelMagicMaidMarthaAttack();

    private void selectModel(ModelMagicMaidMarthaBone model)
    {
        this.hairMain = model.hairMain;
        this.body = model.body;
        this.leftLeg = model.leftLeg;
        this.rightLeg = model.rightLeg;
        this.leftSleeve = model.leftSleeve;
        this.rightSleeve = model.rightSleeve;
        this.dress = model.dress;
    }

    private void selectModelByEntityState(int state)
    {
        switch (state){
            case 0: selectModel(standard);break;
            case 1: selectModel(walk);break;
            case 2: selectModel(normalStand);break;
            case 3: selectModel(attack);break;
        }
    }
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityMagicMaidMartha maid = (EntityMagicMaidMartha) entity;
        int state = maid.getState();
        selectModelByEntityState(state);

        this.hairMain.render(f5);
        this.leftLeg.render(f5);
        this.dress.render(f5);
        this.rightLeg.render(f5);
        this.body.render(f5);
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        int state = ((EntityMagicMaid)entityIn).getState();
        selectModelByEntityState(state);
        if (state == 0) {
            this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leftSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.rightSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
        else if (state == 1){
            this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leftSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.6F * limbSwingAmount;
            this.rightSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;
        }
        else if (state == 2){
            this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        }

        this.hairMain.rotateAngleY = netHeadYaw * 0.017453292F;
        this.hairMain.rotateAngleX = headPitch * 0.017453292F;
    }
}
