package com.xwm.magicmaid.object.block;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMarthaBoss;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRettBoss;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelinaBoss;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockMemoryClock extends BlockBase
{
    Random random = new Random();

    public BlockMemoryClock(String name) {
        super(name, Material.IRON);

        setHardness(100.0f);
        setResistance(2000.0f);
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

//    @SideOnly(Side.CLIENT)
//    public BlockRenderLayer getBlockLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
            return false;

        if (hand != EnumHand.MAIN_HAND)
            return false;

        EntityMagicMaid bossMaid;
        int f = random.nextInt(3);
        switch (f){
            case 0:
                bossMaid = new EntityMagicMaidMarthaBoss(worldIn);break;
            case 1:
                bossMaid = new EntityMagicMaidRettBoss(worldIn);break;
            default:
                bossMaid = new EntityMagicMaidSelinaBoss(worldIn);break;
        }
        bossMaid.setPosition(pos.getX(), pos.getY()+2, pos.getZ()); //todo
        worldIn.spawnEntity(bossMaid);
        return true;
    }
}
