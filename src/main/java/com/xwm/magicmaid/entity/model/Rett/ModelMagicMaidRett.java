package com.xwm.magicmaid.entity.model.Rett;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRett;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Mod;

public class ModelMagicMaidRett extends ModelMagicMaidRettBone
{
    private ModelMagicMaidRettStandard standard = new ModelMagicMaidRettStandard();

    /** 拿着灭魔大剑时候的样子 **/
    private ModelMagicMaidRettDemonKillerStand demonKillerBone = new ModelMagicMaidRettDemonKillerStand();
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
        this.dress = model.dress;
        this.handle = model.handle; //剑柄
    }

    private void copyModelXYZ(ModelMagicMaidRettBone model)
    {
        copyModelRendererXYZ(this.hairMain, model.hairMain);
        copyModelRendererXYZ(this.body, model.body);
        copyModelRendererXYZ(this.leftLeg, model.leftLeg);
        copyModelRendererXYZ(this.rightLeg, model.rightLeg);
        copyModelRendererXYZ(this.dress, model.dress);

        copyModelRendererXYZ(this.handle, model.handle); // 剑柄改变了
    }

    private void selectModelByEntityState(EnumRettState state)
    {
        switch (state){
            case STANDARD: selectModel(standard); break;
            case DEMON_KILLER_STANDARD: selectModel(demonKillerBone); break;
        }
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityMagicMaidRett maid = (EntityMagicMaidRett) entity;
        int state = maid.getState();
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
            case DEMON_KILLER_STANDARD:
                performDemonKillerAnimation(rett);
                this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        }

        this.hairMain.rotateAngleY = netHeadYaw * 0.017453292F;
        this.hairMain.rotateAngleX = headPitch * 0.017453292F;
    }

    private void performDemonKillerAnimation(EntityMagicMaidRett rett)
    {
        int performTick = rett.getPerformTick();
        if (performTick <= 5){
            setRotationByPerformTick(demonKillerStand, demonKillerAttack1, performTick);
        }
        else if (performTick <= 10){
            setRotationByPerformTick(demonKillerAttack1, demonKillerAttack2, performTick - 5);
        }
        else if (performTick <= 20){
            setRotationByPerformTick(demonKillerAttack2, demonKillerAttack3, performTick - 10);
        }
        else if (performTick <= 30){
            setRotationByPerformTick(demonKillerAttack3, demonKillerAttack4, performTick - 20);
        }
        else if (performTick == 31){
            copyModelXYZ(demonKillerStand);
        }
    }

    private void setRotationByPerformTick(ModelMagicMaidRettBone oldModel, ModelMagicMaidRettBone newModel, int tick)
    {
        setRotationBetween(this.hairMain, oldModel.hairMain, newModel.hairMain, tick);
        setRotationBetween(this.body, oldModel.body, newModel.body, tick);
        setRotationBetween(this.dress, oldModel.dress, newModel.dress, tick);
    }

    private void setRotationBetween(ModelRenderer o, ModelRenderer a, ModelRenderer b, int tick){
       dfs(o, a, b, tick);
    }

    private void dfs(ModelRenderer o, ModelRenderer a, ModelRenderer b, int tick)
    {
        o.rotateAngleX = a.rotateAngleX + ((b.rotateAngleX - a.rotateAngleX) / (10.0f)) * tick;
        o.rotateAngleY = a.rotateAngleY + ((b.rotateAngleY - a.rotateAngleY) / (10.0f)) * tick;
        o.rotateAngleZ = a.rotateAngleZ + ((b.rotateAngleZ - a.rotateAngleZ) / (10.0f)) * tick;
        o.offsetX = a.offsetX + ((b.offsetX - a.offsetX) / 10.0f) * tick;
        o.offsetY = a.offsetY + ((b.offsetY - a.offsetY) / 10.0f) * tick;
        o.offsetZ = a.offsetZ + ((b.offsetZ - a.offsetZ) / 10.0f) * tick;

        for (int i = 0; o.childModels != null && i < o.childModels.size(); i++)
        {
            dfs(o.childModels.get(i), a.childModels.get(i), b.childModels.get(i), tick);
        }
    }

    private void copyModelRendererXYZ(ModelRenderer a, ModelRenderer b){
        a.rotateAngleX = b.rotateAngleX;
        a.rotateAngleY = b.rotateAngleY;
        a.rotateAngleZ = b.rotateAngleZ;
        a.offsetX = b.offsetX;
        a.offsetY = b.offsetY;
        a.offsetZ = b.offsetZ;
    }

}
