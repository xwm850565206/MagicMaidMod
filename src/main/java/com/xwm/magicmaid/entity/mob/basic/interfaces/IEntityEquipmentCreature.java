package com.xwm.magicmaid.entity.mob.basic.interfaces;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import net.minecraft.inventory.IInventory;

import java.util.UUID;

/**
 * 生物可以拥有装备的接口
 */
public interface IEntityEquipmentCreature extends IInventory
{
    void setHasWeapon(boolean hasWeapon);

    boolean hasWeapon();

    void setHasArmor(boolean hasArmor);

    boolean hasArmor();

    void setWeaponID(UUID uuid);

    UUID getWeaponID();

    void setWeaponType(int type);

    int getWeaponType();

    void setArmorType(int type);

    int getArmorType();

    void getEquipment(ItemEquipment equipment);

    void loseEquipment(ItemEquipment equipment);

}