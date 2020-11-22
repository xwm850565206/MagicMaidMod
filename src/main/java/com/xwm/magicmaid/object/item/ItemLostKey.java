package com.xwm.magicmaid.object.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemLostKey extends ItemBase
{
    public ItemLostKey(String name) {
        super(name);
        setMaxStackSize(64);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "正义和邪恶同时缠绕在");
        tooltip.add(TextFormatting.YELLOW + "这把钥匙上,为什么会这样？");
    }
}
