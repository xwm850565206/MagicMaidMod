package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemImmortal extends ItemArmor
{
    public ItemImmortal(String name) {
        super(name);
        enumEquipment = EnumEquipment.IMMORTAL;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "拥有悠长历史的传说级铠甲，摆在教堂前厅的");
        tooltip.add(TextFormatting.YELLOW + "陈列柜里，据说它能够使人不死不灭");
    }
}
