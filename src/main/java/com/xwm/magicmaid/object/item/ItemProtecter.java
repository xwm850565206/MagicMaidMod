package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.enumstorage.EnumEquipment;

public class ItemProtecter extends ItemArmor {

    public ItemProtecter(String name) {
        super(name);
        enumEquipment = EnumEquipment.PROTECTOR;
    }
}
