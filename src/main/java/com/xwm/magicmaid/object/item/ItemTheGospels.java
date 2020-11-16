package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.object.block.BlockChurchPortalFrame;
import com.xwm.magicmaid.world.dimension.ChurchTeleporter;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class ItemTheGospels extends ItemBase
{
    public ItemTheGospels(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "歌颂神的福音书，为什么会在僵尸身上？");
        tooltip.add(TextFormatting.YELLOW + "右键使用将穿梭到教堂废墟");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
//        if (worldIn.isRemote)
//            return super.onItemRightClick(worldIn, playerIn, handIn);
//        else if (worldIn.provider.getDimension() != DimensionInit.DIMENSION_CHURCH){
//            playerIn.changeDimension(DimensionInit.DIMENSION_CHURCH, new ChurchTeleporter((WorldServer) worldIn, DimensionInit.DIMENSION_CHURCH, playerIn.posX, playerIn.posY, playerIn.posZ));
//            return super.onItemRightClick(worldIn, playerIn, handIn);
//        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }


    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        ItemStack itemstack = player.getHeldItem(hand);

        if (player.canPlayerEdit(pos.offset(facing), facing, itemstack) && iblockstate.getBlock() == BlockInit.CHURCH_PORTAL_FRAME && !iblockstate.getValue(BlockChurchPortalFrame.GOSPELS))
        {
            if (worldIn.isRemote)
            {
                return EnumActionResult.SUCCESS;
            }
            else
            {
                worldIn.setBlockState(pos, iblockstate.withProperty(BlockChurchPortalFrame.GOSPELS, Boolean.TRUE), 2);
                worldIn.updateComparatorOutputLevel(pos, BlockInit.CHURCH_PORTAL_FRAME);
                itemstack.shrink(1);

                //todo  特效修改
                for (int i = 0; i < 16; ++i)
                {
                    double d0 = (double)((float)pos.getX() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
                    double d1 = (double)((float)pos.getY() + 0.8125F);
                    double d2 = (double)((float)pos.getZ() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
                    double d3 = 0.0D;
                    double d4 = 0.0D;
                    double d5 = 0.0D;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }

                // 音效
                worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                BlockPattern.PatternHelper blockpattern$patternhelper = BlockChurchPortalFrame.getOrCreatePortalShape().match(worldIn, pos);

                if (blockpattern$patternhelper != null)
                {
                    BlockPos blockpos = blockpattern$patternhelper.getFrontTopLeft();

                    for (int j = 0; j < 3; ++j)
                    {
                        for (int k = 0; k < 2; ++k)
                        {
                            worldIn.setBlockState(blockpos.add(0, -j - 1, 0).offset(blockpattern$patternhelper.getForwards().rotateY(), 1+k), BlockInit.CHURCH_PORTAL.getDefaultState(), 2);
                        }
                    }

                    worldIn.playBroadcastSound(1038, blockpos.add(1, 0, 1), 0);
                }

                return EnumActionResult.SUCCESS;
            }
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }


}
