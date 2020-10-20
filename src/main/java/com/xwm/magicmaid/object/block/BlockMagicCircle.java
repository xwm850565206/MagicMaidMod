package com.xwm.magicmaid.object.block;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.object.tileentity.TileEntityMagicCircle;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockMagicCircle extends BlockBase implements ITileEntityProvider
{
    public static final PropertyBool OPEN = PropertyBool.create("open");

    protected AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.2, 1);

    public BlockMagicCircle(String name) {
        super(name, Material.ROCK);

        setHardness(100.0f);
        setResistance(2000.0f);
        setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(OPEN, Boolean.FALSE));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (hand != EnumHand.MAIN_HAND)
            return false;

        playerIn.openGui(Main.instance, Reference.GUI_MAGIC_CIRCLE, worldIn, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(OPEN, meta == 1);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return (Boolean) state.getValue(OPEN) ? 1 : 0;
    }


    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, OPEN);
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



    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     *
     * @param worldIn
     * @param meta
     */
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMagicCircle();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(BlockInit.magicCircle);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(BlockInit.magicCircle);
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Deprecated //Use IBlockState.getBlockFaceShape
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        if (side == EnumFacing.UP)
            return false;
        return true;
    }

    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, BlockInit.magicCircle.getDefaultState().withProperty(BlockMagicCircle.OPEN, active));
        if(tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
}
