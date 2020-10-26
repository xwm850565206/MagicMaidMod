package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemInstructionBook extends ItemBase
{
    public ItemInstructionBook(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.openGui(Main.instance, Reference.GUI_INSTRUCTION_BOOK, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
        playerIn.addStat(StatList.getObjectUseStats(Items.WRITTEN_BOOK));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.LIGHT_PURPLE + "苦力怕著");
        tooltip.add(TextFormatting.LIGHT_PURPLE + "这本书请详细阅读");
    }
}
