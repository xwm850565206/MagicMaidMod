package com.xwm.magicmaid.object.item.equipment;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemWise extends ItemArmor
{
    public ItemWise(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "一件破烂的法袍，上面涌动的");
        tooltip.add(TextFormatting.YELLOW + "魔法纹路没人能够看到");
    }
}
