package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.object.block.BlockChurchPortalFrame;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

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
                BlockPos pos1 = player.getPosition().offset(player.getHorizontalFacing().rotateY());
                pos1 = pos1.offset(player.getHorizontalFacing());
                for (int i = 0; i < 16; ++i)
                {
                    double d0 = pos1.getX() + itemRand.nextFloat() / 2 - 0.25;
                    double d1 = pos1.getY() + player.eyeHeight - 0.15F;
                    double d2 = pos1.getZ() + itemRand.nextFloat() / 2 - 0.25;
                    ParticleSpawner.spawnParticle(EnumCustomParticles.PANDORA, d0, d1, d2, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
                }
                return EnumActionResult.SUCCESS;
            }
            else
            {
                worldIn.setBlockState(pos, iblockstate.withProperty(BlockChurchPortalFrame.GOSPELS, Boolean.TRUE), 2);
                worldIn.updateComparatorOutputLevel(pos, BlockInit.CHURCH_PORTAL_FRAME);
                itemstack.shrink(1);

                // 音效
                worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENCHANT_THORNS_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
