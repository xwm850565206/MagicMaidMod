package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemMemoryRett extends ItemBase
{
    public ItemMemoryRett(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.LIGHT_PURPLE + "一段被黑魔法封印住的信息");
        tooltip.add(TextFormatting.YELLOW + "可以右键使用");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (handIn == EnumHand.MAIN_HAND){
            playerIn.openGui(Main.instance, Reference.GUI_MAID_MEMORY, worldIn, 1, 0,0 );
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        else
            return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
}
