package com.xwm.magicmaid.entity.model.mob;// Made with Blockbench 3.9.3
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import com.xwm.magicmaid.entity.mob.basic.EntityNun;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;

public class ModelNun extends ModelPlayer {

	public ModelNun() {
		super(0.0F, true);
	}

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
       super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        if (entityIn instanceof EntityNun)
		{
			EntityNun nun = (EntityNun) entityIn;
			if (nun.isArmsRaised()) {
                this.bipedRightArm.rotateAngleX = 4.35F;
                this.bipedRightArmwear.rotateAngleX = 4.35F;
            }
		}

    }
}