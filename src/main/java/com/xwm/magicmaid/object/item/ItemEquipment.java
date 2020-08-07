package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.enumstorage.EnumEquipment;

public class ItemEquipment extends ItemBase
{

    public EnumEquipment enumEquipment;

    public ItemEquipment(String name) {
        super(name);
    }

    public static ItemEquipment valueOf(EnumEquipment equipment)
    {
        return EnumEquipment.toItemEquipment(equipment);
    }
}
