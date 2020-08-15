package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemConviction extends ItemWeapon
{
    public ItemConviction(String name) {
        super(name);
        enumEquipment = EnumEquipment.CONVICTION;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "教堂的圣器，传说是大主教镇压恶鬼修斯时");
        tooltip.add(TextFormatting.YELLOW + "所用的武器，光是靠近它就让人心生敬畏");
    }
}
