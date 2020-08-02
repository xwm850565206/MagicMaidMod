package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.mob.weapon.EnumEquipments;

public class ItemConviction extends ItemWeapon
{
    public ItemConviction(String name) {
        super(name);
        enumEquipment = EnumEquipments.CONVICTION;
    }
}
