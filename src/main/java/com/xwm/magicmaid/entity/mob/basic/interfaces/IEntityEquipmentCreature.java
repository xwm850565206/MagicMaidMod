package com.xwm.magicmaid.entity.mob.basic.interfaces;

import com.xwm.magicmaid.entity.mob.basic.InventoryEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.UUID;

/**
 * 生物可以拥有装备的接口
 */
public interface IEntityEquipmentCreature
{
    boolean isFirstGetArmor();

    void setFirstGetArmor(boolean isFirstGetArmor);

    void setWeaponID(UUID uuid);

    UUID getWeaponID();

    void setWeaponType(String type);

    String getWeaponType();

    void setArmorType(String type);

    String getArmorType();

    void getEquipment(ItemEquipment equipment);

    void loseEquipment(ItemEquipment equipment);

    InventoryEquipment getInventory();

    void setInventory(NonNullList<ItemStack> inventory);

    ItemStack getWeaponFromSlot();

    ItemStack getArmorFromSlot();

    AxisAlignedBB getUsingArea(ItemStack stack, EntityLivingBase player, AxisAlignedBB bb);
}
