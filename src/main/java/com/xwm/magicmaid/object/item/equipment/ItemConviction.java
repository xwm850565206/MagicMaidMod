package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;

public class ItemConviction extends ItemWeapon
{
    public ItemConviction(String name) {
        super(name);
        enumEquipment = EnumEquipment.CONVICTION;
    }
}