package com.xwm.magicmaid.entity.model.Selina;


import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMartha;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelina;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelMagicMaidSeline extends ModelMagicMaidSelineBone
{
    private static ModelMagicMaidSelineStandard standard = new ModelMagicMaidSelineStandard();
    private static ModelMagicMaidSelineServe serve = new ModelMagicMaidSelineServe();
    private static ModelMagicMaidSelineSitting sitting = new ModelMagicMaidSelineSitting();
    private static ModelMagicMaidSelineChant chant = new ModelMagicMaidSelineChant();
    private static ModelMagicMaidSelineStimulate stimulate = new ModelMagicMaidSelineStimulate();

    private void selectModel(ModelMagicMaidSelineBone model)
    {
        this.hairMain = model.hairMain;
        this.body = model.body;
        this.leftLeg = model.leftLeg;
        this.rightLeg = model.rightLeg;
        this.leftSleeve = model.leftSleeve;
        this.rightSleeve = model.rightSleeve;
        this.dress = model.dress;
    }

    private void selectModelByEntityState(EnumSelineState state)
    {
        switch (state){
            case STANDARD: selectModel(standard); break;
            case SITTING: selectModel(sitting); break;
            case SERVE: selectModel(serve); break;
            case CHANT: selectModel(chant); break;
            case STIMULATE: selectModel(stimulate); break;
        }
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityMagicMaidSelina maid = (EntityMagicMaidSelina) entity;
        EnumSelineState state = EnumSelineState.valueOf(maid.getState());
        selectModelByEntityState(state);

        this.hairMain.render(f5);
        this.leftLeg.render(f5);
        this.dress.render(f5);
        this.rightLeg.render(f5);
        this.body.render(f5);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        EntityMagicMaidSelina selina = (EntityMagicMaidSelina) entityIn;
        EnumSelineState state = EnumSelineState.valueOf(selina.getState());
        selectModelByEntityState(state);

        switch (state) {
            case STANDARD:
                this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
                this.leftSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
                this.rightSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                break;
            case SITTING:
                this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
                break;
            case SERVE:
                this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
                this.leftSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.6F * limbSwingAmount;
                this.rightSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;
                break;
            case CHANT:
                break;
            case STIMULATE:
                break;
        }
        this.hairMain.rotateAngleY = netHeadYaw * 0.017453292F;
        this.hairMain.rotateAngleX = headPitch * 0.017453292F;
    }
}
