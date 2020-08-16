package com.xwm.magicmaid.object.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemRemainingSoft extends ItemBase
{
    public ItemRemainingSoft(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "残破的记忆中还有些许余温");
        tooltip.add(TextFormatting.YELLOW + "对女仆右键使用升阶");
    }
}
