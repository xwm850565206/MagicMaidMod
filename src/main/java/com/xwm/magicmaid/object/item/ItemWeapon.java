package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.mob.weapon.EnumWeapons;
import com.xwm.magicmaid.init.ItemInit;

public class ItemWeapon extends ItemBase
{
    public EnumWeapons enumWeapon;

    public ItemWeapon(String name) {
        super(name);
        this.maxStackSize = 1;
    }

    public static ItemWeapon valueOf(EnumWeapons weapon)
    {
        if (weapon == EnumWeapons.CONVICTION)
            return (ItemWeapon) ItemInit.ItemConviction;
        else if (weapon == EnumWeapons.REPATENCE)
            return (ItemWeapon)ItemInit.ItemRepantence;
        return null;
    }
}
