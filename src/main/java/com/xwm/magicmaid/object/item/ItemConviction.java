package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.mob.weapon.EnumWeapons;

public class ItemConviction extends ItemWeapon
{
    public ItemConviction(String name) {
        super(name);
        enumWeapon = EnumWeapons.CONVICTION;
    }
}
