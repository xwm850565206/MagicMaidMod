package com.xwm.magicmaid.object.block;

import com.google.common.base.Predicates;
import com.xwm.magicmaid.init.BlockInit;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChurchPortalFrame extends BlockBase
{
    public static final PropertyBool GOSPELS = PropertyBool.create("gospels");
    private static BlockPattern portalShape;

    public BlockChurchPortalFrame(String name) {
        super(name, Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(GOSPELS, Boolean.valueOf(false)));
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }


    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(GOSPELS, Boolean.valueOf(false));
    }

    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return (Boolean) blockState.getValue(GOSPELS) ? 15 : 0;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(GOSPELS, (meta & 4) != 0);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        if ((Boolean) state.getValue(GOSPELS))
        {
            i |= 4;
        }

        return i;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(GOSPELS) ? 30 : 0;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {GOSPELS});
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public static BlockPattern getOrCreatePortalShape()
    {
        if (portalShape == null)
        {
            portalShape = FactoryBlockPattern.start().aisle("?##?", "x??x", "x??x", "x??x", "?##?")
                    .where('?', BlockWorldState.hasState(BlockStateMatcher.ANY))
                    .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.COBBLESTONE)))
                    .where('x', BlockWorldState.hasState(BlockStateMatcher.forBlock(BlockInit.CHURCH_PORTAL_FRAME).where(GOSPELS, Predicates.equalTo(Boolean.TRUE))))
                    .build();
        }

        return portalShape;
    }
}
