package com.xwm.magicmaid.object.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemNoSoulGhost extends ItemBase
{
    public ItemNoSoulGhost(String name) {
        super(name);
    }

    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "被提炼走灵魂的魂魄，是最好的容器");
    }
}
