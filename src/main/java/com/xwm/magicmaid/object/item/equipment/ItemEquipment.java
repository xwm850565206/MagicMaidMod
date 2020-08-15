package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.object.item.ItemBase;

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
}
