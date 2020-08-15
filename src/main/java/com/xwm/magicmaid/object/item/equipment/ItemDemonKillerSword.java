package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemDemonKillerSword extends ItemWeapon
{
    public ItemDemonKillerSword(String name) {
        super(name);
        enumEquipment = EnumEquipment.DEMONKILLINGSWORD;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "一把从教堂枯井中找到的破剑，直到骑士长蕾特");
        tooltip.add(TextFormatting.YELLOW + "拿起它，紫红色的光芒瞬间迸发");
    }
}
