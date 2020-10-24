package com.xwm.magicmaid.object.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemAbsorbSoulCompass extends ItemBase
{
    public ItemAbsorbSoulCompass(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "被执念灌注的磁石，对灵魂具有很强的引力");
    }
}
