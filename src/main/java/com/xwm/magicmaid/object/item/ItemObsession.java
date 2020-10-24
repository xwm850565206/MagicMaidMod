package com.xwm.magicmaid.object.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemObsession extends ItemBase
{
    public ItemObsession(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "在那个黑暗的时代，仍不乏有纯粹的灵魂");
        tooltip.add(TextFormatting.YELLOW + "他们受到教会的审判后，灵魂不得解脱，化作执念");
    }
}
