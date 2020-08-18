package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.world.dimension.ChurchTeleporter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
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
        if (worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        else if (worldIn.provider.getDimension() != DimensionInit.DIMENSION_CHURCH){
            playerIn.changeDimension(DimensionInit.DIMENSION_CHURCH, new ChurchTeleporter((WorldServer) worldIn, DimensionInit.DIMENSION_CHURCH, playerIn.posX, playerIn.posY, playerIn.posZ));
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
