package com.xwm.magicmaid.entity.model.strawberry;

import com.xwm.magicmaid.entity.maid.EntityMagicMaidStrawberry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelMagicMaidStrawberry extends ModelBase
{
    private ModelMagicMaidStrawberryStandard standard = new ModelMagicMaidStrawberryStandard();
    private ModelMagicMaidStrawberryWalk walk = new ModelMagicMaidStrawberryWalk();
    private ModelMagicMaidStrawberryNormalStand normalStand = new ModelMagicMaidStrawberryNormalStand();
    private ModelMagicMaidStrawberryAttack attack = new ModelMagicMaidStrawberryAttack();

    private void selectModel(ModelBase modelBase)
    {

    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityMagicMaidStrawberry maid = (EntityMagicMaidStrawberry) entity;

        int state = maid.getState();

        switch (state){
            case 0: normalStand.render(entity, f, f1, f2, f3, f4, f5);break;
            case 1: walk.render(entity, f, f1, f2, f3, f4, f5);break;
            case 2: standard.render(entity, f, f1, f2, f3, f4, f5);break;
            case 3: attack.render(entity, f, f1, f2, f3, f4, f5);break;
        }
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
    }
}
