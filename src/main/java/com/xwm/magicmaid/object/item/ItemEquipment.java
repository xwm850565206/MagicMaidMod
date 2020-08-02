package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.mob.weapon.EnumEquipments;
import com.xwm.magicmaid.init.ItemInit;

public class ItemEquipment extends ItemBase
{

    public EnumEquipments enumEquipment;

    public ItemEquipment(String name) {
        super(name);
    }

    public static ItemEquipment valueOf(EnumEquipments equipment)
    {
        return EnumEquipments.toItemEquipment(equipment);
    }
}
