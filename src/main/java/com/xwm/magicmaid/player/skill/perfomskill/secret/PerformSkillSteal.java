package com.xwm.magicmaid.player.skill.perfomskill.secret;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class PerformSkillSteal extends PerformSkillSecretBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getPerformEnergy() {
        return 500;
    }

    @Override
    public int getColdTime() {
        return 200;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {

        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;
        if (worldIn.isRemote) return;
        List<EntityLiving> entityLivings = worldIn.getEntitiesWithinAABB(EntityLiving.class, playerIn.getEntityBoundingBox().grow(5*level, 2, 5*level));
        for (EntityLiving entityLiving : entityLivings) {
            try {
                if (entityLiving == playerIn)
                    continue;
                if (!entityLiving.isNonBoss())
                    continue;
                if (entityLiving instanceof EntityMagicMaid)
                    continue;
                if (entityLiving instanceof IInventory) {
                    InventoryHelper.dropInventoryItems(worldIn, new BlockPos(entityLiving), (IInventory) entityLiving);
                }
            } catch (Exception e) {
                ;
            }
        }
        AxisAlignedBB bb = playerIn.getEntityBoundingBox().grow(5*level, 2, 5*level);
        for (BlockPos pos : BlockPos.getAllInBox((int)Math.floor(bb.minX), (int)Math.floor(bb.minY), (int)Math.floor(bb.minZ),
                (int)Math.ceil(bb.maxX),(int)Math.ceil(bb.maxY), (int)Math.ceil(bb.maxZ))) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileEntity);
            }
        }
        curColdTime = getColdTime();
    }

    @Override
    public int getRequirePoint() {
        return getLevel() < getMaxLevel() ? 5000 : -1;
    }

    @Override
    public String getName() {
        return super.getName() + ".steal";
    }

    @Override
    public String getDescription() {
        return "偷窃";
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

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 86, 141, 46, 46);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDetailDescription() {
        return "将附近可能携带物品的生物或者储存物里的东西掉落出来\n并不是对所有生物都有效";
    }

}
