package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.mob.weapon.EnumEquipments;

public class ItemProtecter extends ItemArmor {

    public ItemProtecter(String name) {
        super(name);
        enumEquipment = EnumEquipments.PROTECTOR;
    }
}
