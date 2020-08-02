package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.mob.weapon.EnumEquipments;
import com.xwm.magicmaid.init.ItemInit;

public class ItemEquipment extends ItemBase
{

    public EnumEquipments enumEquipment;

    public ItemEquipment(String name) {
        super(name);
    }

    public static ItemEquipment valueOf(EnumEquipments weapon)
    {
        if (weapon == EnumEquipments.CONVICTION)
            return (ItemEquipment) ItemInit.ItemConviction;
        else if (weapon == EnumEquipments.REPATENCE)
            return (ItemEquipment)ItemInit.ItemRepantence;
        return null;
    }
}
