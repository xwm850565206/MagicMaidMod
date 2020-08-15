package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemRepantence extends ItemWeapon
{
    public ItemRepantence(String name) {
        super(name);
        enumEquipment = EnumEquipment.REPATENCE;
    }
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "修女祷告时使用的物品，听说能够镇压邪灵");
    }
}
