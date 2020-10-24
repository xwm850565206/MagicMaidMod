package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.object.item.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemEliminateSoulNail extends ItemBase
{
    public ItemEliminateSoulNail(String name) {
        super(name);
        setMaxStackSize(16);
    }

    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "教堂惩罚罪人的刑具，被钉上的人的灵魂会被慢慢抹去");
    }
}
