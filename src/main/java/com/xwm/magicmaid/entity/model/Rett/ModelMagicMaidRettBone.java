package com.xwm.magicmaid.entity.model.Rett;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import java.util.ArrayList;
import java.util.List;

public class ModelMagicMaidRettBone extends ModelBase
{
    public ModelRenderer hairMain;
    public ModelRenderer rightLeg;
    public ModelRenderer body;
    public ModelRenderer leftLeg;
    public ModelRenderer dress;
    public ModelRenderer head;
    public ModelRenderer hairpin;
    public ModelRenderer connect1;
    public ModelRenderer earLeft;
    public ModelRenderer earRight;
    public ModelRenderer earLeft2;
    public ModelRenderer earRight2;
    public ModelRenderer cubeLeft;
    public ModelRenderer cubeRight;
    public ModelRenderer hairLeftBraid;
    public ModelRenderer hairRightBraid;
    public ModelRenderer connect2;
    public ModelRenderer connect3;
    public ModelRenderer partTop;
    public ModelRenderer partBottom;
    public ModelRenderer partLeft;
    public ModelRenderer partRight;
    public ModelRenderer partTop_1;
    public ModelRenderer partBottom_1;
    public ModelRenderer partLeft_1;
    public ModelRenderer partRight_1;
    public ModelRenderer leftSleeve;
    public ModelRenderer rightSleeve;
    public ModelRenderer leftArm;
    public ModelRenderer rightArm;

    /**灭魔大剑模型部分**/
    public ModelRenderer handle;
    public ModelRenderer handlepart;
    public ModelRenderer sword1;
    public ModelRenderer sword2;
    public ModelRenderer sword3;
    public ModelRenderer sword4;
    public ModelRenderer sword5;
    public ModelRenderer sword6;

//    public List<ModelRenderer> rendererList = new ArrayList<ModelRenderer>();

    public ModelMagicMaidRettBone(){

//        rendererList.add(hairMain);
//        rendererList.add(rightLeg);
//        rendererList.add(body);
//        rendererList.add(leftLeg);
//        rendererList.add(dress);
//        rendererList.add(head);
//        rendererList.add(hairpin);
//        rendererList.add(connect1);
//        rendererList.add(earLeft);
//        rendererList.add(earRight);
//        rendererList.add(earLeft2);
//        rendererList.add(earRight2);
//        rendererList.add(cubeLeft);
//        rendererList.add(cubeRight);
//        rendererList.add(hairLeftBraid);
//        rendererList.add(hairRightBraid);
//        rendererList.add(connect2);
//        rendererList.add(connect3);
//        rendererList.add(partTop);
//        rendererList.add(partBottom);
//        rendererList.add(partLeft);
//        rendererList.add(partRight);
//        rendererList.add(partTop_1);
//        rendererList.add(partBottom_1);
//        rendererList.add(partLeft_1);
//        rendererList.add(partRight_1);
//        rendererList.add(leftSleeve);
//        rendererList.add(rightSleeve);
//        rendererList.add(leftArm);
//        rendererList.add(rightArm);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
