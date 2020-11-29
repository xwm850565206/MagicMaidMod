package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.object.item.ItemBase;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemEquipment extends ItemBase
{

    public EnumEquipment enumEquipment;

    public ItemEquipment(String name) {
        super(name);
        setMaxStackSize(1);
    }

    public static ItemEquipment valueOf(EnumEquipment equipment)
    {
        return EnumEquipment.toItemEquipment(equipment);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        ItemEquipment equipment = (ItemEquipment) toRepair.getItem();
        Item piece = MagicEquipmentRegistry.getPiece(equipment.enumEquipment);
        return repair.getItem() == piece;
    }
}
