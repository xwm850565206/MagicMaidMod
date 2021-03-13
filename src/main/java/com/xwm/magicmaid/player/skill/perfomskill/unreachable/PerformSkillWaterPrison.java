package com.xwm.magicmaid.player.skill.perfomskill.unreachable;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Random;

public class PerformSkillWaterPrison extends PerformSkillUnreachableBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");
    private Random random = new Random();

    public PerformSkillWaterPrison() {
        this.level = 0; // 从0开始 0没有技能，需要学习
    }

    @Override
    public int getPerformEnergy() {
        return 50;
    }

    @Override
    public int getColdTime() {
        return 200;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

        if (level == 0) {
            playerIn.sendMessage(new TextComponentString("无法释放技能，请先升级该技能"));
            return;
        }
        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;


        BlockPos centerpos = new BlockPos(posIn.getX(), posIn.getY() + playerIn.height + 6, posIn.getZ());
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                BlockPos temppos = new BlockPos(centerpos.getX() + x, centerpos.getY() + 2, centerpos.getZ() + z);
                worldIn.setBlockState(temppos, Blocks.STONE.getDefaultState());
                temppos = new BlockPos(centerpos.getX() + x, centerpos.getY() - 2, centerpos.getZ() + z);
                worldIn.setBlockState(temppos, Blocks.STONE.getDefaultState());
            }

        for (int x = -2; x <= 2; x++)
            for (int y = -2; y <= 2; y++) {
                BlockPos temppos = new BlockPos(centerpos.getX() + x, centerpos.getY() + y, centerpos.getZ() + 2);
                worldIn.setBlockState(temppos, Blocks.STONE.getDefaultState());
                temppos = new BlockPos(centerpos.getX() + x, centerpos.getY() + y, centerpos.getZ() - 2);
                worldIn.setBlockState(temppos, Blocks.STONE.getDefaultState());
            }

        for (int y = -2; y <= 2; y++)
            for (int z = -2; z <= 2; z++) {
                BlockPos temppos = new BlockPos(centerpos.getX() + 2, centerpos.getY() + y, centerpos.getZ() + z);
                worldIn.setBlockState(temppos, Blocks.STONE.getDefaultState());
                temppos = new BlockPos(centerpos.getX() - 2, centerpos.getY() + y, centerpos.getZ() + z);
                worldIn.setBlockState(temppos, Blocks.STONE.getDefaultState());
            }

        int offset = 1;
        for (int x = centerpos.getX()-offset; x <= centerpos.getX()+offset; x++)
            for (int y = centerpos.getY()-offset; y <= centerpos.getY()+offset; y++)
                for (int z = centerpos.getZ()-offset; z <= centerpos.getZ()+offset; z++)
                {
                    BlockPos temppos = new BlockPos(x, y , z);
                    worldIn.setBlockState(temppos, Blocks.WATER.getDefaultState());
                }

        List<EntityLiving> entityLivings = worldIn.getEntitiesWithinAABB(EntityLiving.class, playerIn.getEntityBoundingBox().grow(5*level, 3 + level, 5*level));
        for (EntityLiving entityLiving : entityLivings) {
            try {
                if (entityLiving == playerIn)
                    continue;

                if (worldIn.isRemote)
                {
                    for (int i = 0; i < 10; i++)
                        worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, entityLiving.posX + random.nextDouble(), entityLiving.posY + entityLiving.height / 2.0 + random.nextDouble(), entityLiving.posZ + random.nextDouble(), 0.1*(random.nextDouble()-0.5), 0.1*random.nextDouble(), 0.1*(random.nextDouble()-0.5));
                }
                entityLiving.setPosition(centerpos.getX(), centerpos.getY(), centerpos.getZ());

                if (level > 1) {
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 400, level));
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.POISON, 400, level));
                }

            } catch (Exception e) {
                ;
            }
        }

        curColdTime = getColdTime();
    }

    @Override
    public int getRequirePoint() {
        int[] points = new int[] {5000, 2000, 2000, 8000};
        return getLevel() < getMaxLevel() ? points[level] : -1;
    }

    @Override
    public String getName() {
        return super.getName() + ".water_prison";
    }

    @Override
    public String getDescription() {
        return "一键建筑赠送技能:水牢";
    }

    @Override
    public void drawIcon(int x, int y, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 182, 142, 46, 46);
        GlStateManager.popMatrix();

    }

    @Override
    public String getDetailDescription() {
        return "在上方生成一个水牢，而后将附近的生物打入水牢\n造成持续性的中毒伤害\n给牢里的人提供夜视\n升级可以增大关押范围";
    }
}
