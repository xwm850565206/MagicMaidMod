package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemArmor;

public class ItemProtecter extends ItemArmor {

    public ItemProtecter(String name) {
        super(name);
        enumEquipment = EnumEquipment.PROTECTOR;
    }
}
