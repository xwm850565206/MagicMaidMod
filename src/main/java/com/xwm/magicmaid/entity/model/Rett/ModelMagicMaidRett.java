package com.xwm.magicmaid.entity.model.Rett;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRett;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelMagicMaidRett extends ModelMagicMaidRettBone
{
    private ModelMagicMaidRettStandard standard = new ModelMagicMaidRettStandard();
    private ModelMagicMaidRettDemonKillerStand demonKillerStand = new ModelMagicMaidRettDemonKillerStand();
    private ModelMagicMaidRettDemonKillerAttack1 demonKillerAttack1 = new ModelMagicMaidRettDemonKillerAttack1();
    private ModelMagicMaidRettDemonKillerAttack2 demonKillerAttack2 = new ModelMagicMaidRettDemonKillerAttack2();
    private ModelMagicMaidRettDemonKillerAttack3 demonKillerAttack3 = new ModelMagicMaidRettDemonKillerAttack3();
    private ModelMagicMaidRettDemonKillerAttack4 demonKillerAttack4 = new ModelMagicMaidRettDemonKillerAttack4();

    private void selectModel(ModelMagicMaidRettBone model)
    {
        this.hairMain = model.hairMain;
        this.body = model.body;
        this.leftLeg = model.leftLeg;
        this.rightLeg = model.rightLeg;
        this.leftSleeve = model.leftSleeve;
        this.rightSleeve = model.rightSleeve;
        this.dress = model.dress;
    }

    private void selectModelByEntityState(EnumRettState state)
    {
        switch (state){
            case STANDARD: selectModel(standard); break;
            case DEMON_KILLER_STANDARD: selectModel(demonKillerStand); break;
            case DEMON_KILLER_ATTACK1: selectModel(demonKillerAttack1); break;
            case DEMON_KILLER_ATTACK2: selectModel(demonKillerAttack2); break;
            case DEMON_KILLER_ATTACK3: selectModel(demonKillerAttack3); break;
            case DEMON_KILLER_ATTACK4: selectModel(demonKillerAttack4); break;
        }
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityMagicMaidRett maid = (EntityMagicMaidRett) entity;
        int state = maid.getState();
//        maid.debug();
        selectModelByEntityState(EnumRettState.valueOf(state));

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
        EntityMagicMaidRett rett = (EntityMagicMaidRett) entityIn;
        EnumRettState state = EnumRettState.valueOf(rett.getState());
        selectModelByEntityState(state);

        switch (state){
            case STANDARD:
                this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
                this.leftSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
                this.rightSleeve.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                break;
            default:
                this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        }

        this.hairMain.rotateAngleY = netHeadYaw * 0.017453292F;
        this.hairMain.rotateAngleX = headPitch * 0.017453292F;
    }
}
