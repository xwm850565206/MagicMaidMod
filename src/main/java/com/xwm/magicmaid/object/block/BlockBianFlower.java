package com.xwm.magicmaid.object.block;

import com.xwm.magicmaid.init.PotionInit;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBianFlower extends BlockBase
{
    protected AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.2, 0, 0.2, 0.4, 0.4, 0.4);

    public BlockBianFlower(String name) {
        super(name, Material.PLANTS);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        EntityPlayer player = world.getNearestAttackablePlayer(pos, 10, 4);
        player.addPotionEffect(new PotionEffect(PotionInit.BIAN_FLOWER_EFFECT, 1000, 2));
        super.breakBlock(world, pos, state);
    }

}
