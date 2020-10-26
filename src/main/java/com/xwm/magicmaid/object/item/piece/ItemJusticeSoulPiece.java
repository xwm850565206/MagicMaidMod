package com.xwm.magicmaid.object.item.piece;

import com.xwm.magicmaid.object.item.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemJusticeSoulPiece extends ItemBase
{
    public ItemJusticeSoulPiece(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "正义的残魂，这么纯粹的灵魂十分罕见");
    }
}
