package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemProtector extends ItemArmor {

    public ItemProtector(String name) {
        super(name);
        this.setEquipmentAttribute(MagicEquipmentRegistry.PROTECTOR);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "教堂的圣器，传说是大主教镇压恶鬼修斯时");
        tooltip.add(TextFormatting.YELLOW + "所穿戴的服侍，其中蕴含着圣洁的力量");
    }
}
