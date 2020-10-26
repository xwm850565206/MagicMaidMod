package com.xwm.magicmaid.object.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemGhost extends ItemBase
{
    public ItemGhost(String name) {
        super(name);
    }

    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "逝者的灵魂，徘徊在彼岸");
    }
}
