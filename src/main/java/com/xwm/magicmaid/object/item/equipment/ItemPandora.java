package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemPandora extends ItemWeapon
{
    public ItemPandora(String name) {
        super(name);
        enumEquipment = EnumEquipment.PANDORA;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "充满黑暗力量的魔盒，里面");
        tooltip.add(TextFormatting.YELLOW + "似乎拥有无尽的力量");
    }

}
