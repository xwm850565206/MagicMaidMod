package com.xwm.magicmaid.player.skill.perfomskill;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class PerformSkillFlash extends PerformSkillBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");
    private Random random = new Random();

    @Override
    public int getPerformEnergy() {
        return 50;
    }

    @Override
    public int getColdTime() {
        return 60;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

        if (curColdTime > 0) return;

        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;

        if (worldIn.isRemote)
        {
            for (int i = 0; i < 5; i++)
                worldIn.spawnParticle(EnumParticleTypes.DRAGON_BREATH, playerIn.posX + random.nextDouble(), playerIn.posY + playerIn.height / 2.0 + random.nextDouble(), playerIn.posZ + random.nextDouble(), 0.1*(random.nextDouble()-0.5), 0.1*random.nextDouble(), 0.1*(random.nextDouble()-0.5));
        }

        int offset = 5 * (getLevel() + 1); //todo
        BlockPos pos = posIn.offset(playerIn.getHorizontalFacing(), offset);
        playerIn.setPosition(pos.getX(), pos.getY(), pos.getZ());

        if (worldIn.isRemote)
        {
            for (int i = 0; i < 5; i++)
                worldIn.spawnParticle(EnumParticleTypes.DRAGON_BREATH, playerIn.posX + random.nextDouble(), playerIn.posY + playerIn.height / 2.0 + random.nextDouble(), playerIn.posZ + random.nextDouble(), 0.1*(random.nextDouble()-0.5), 0.1*random.nextDouble(), 0.1*(random.nextDouble()-0.5));
        }

        curColdTime = getColdTime();
    }

    @Override
    public int getRequirePoint() {
        return 20 + 20 * getLevel();
    }

    @Override
    public String getName() {
        return super.getName() + ".flash";
    }

    @Override
    public String getDescription() {
        return "闪现";
    }

    @Override
    public void drawIcon(int x, int y) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 24.0 / 40.0;
        double scaley = 29.0 / 40.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 40, 40);
        GlStateManager.popMatrix();
    }
}
