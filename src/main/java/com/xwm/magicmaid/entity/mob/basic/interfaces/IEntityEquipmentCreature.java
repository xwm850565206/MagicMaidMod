package com.xwm.magicmaid.entity.mob.basic.interfaces;

import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;

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

    NonNullList<ItemStack> getInventory();

    void setInventory(NonNullList<ItemStack> inventory);

    ItemStack getWeaponFromSlot();

    ItemStack getArmorFromSlot();

    AxisAlignedBB getUsingArea(ItemStack stack, EntityLivingBase player, AxisAlignedBB bb);
}
