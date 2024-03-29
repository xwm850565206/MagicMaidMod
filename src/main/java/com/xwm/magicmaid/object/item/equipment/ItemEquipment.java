package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.object.item.ItemBase;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemEquipment extends ItemBase
{
    protected EquipmentAttribute equipmentAttribute;

    public ItemEquipment(String name) {
        super(name);
        setMaxStackSize(1);
    }


    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        ItemEquipment equipment = (ItemEquipment) toRepair.getItem();
        Item piece = equipment.getEquipmentAttribute().getPiece();
        return piece != null && repair.getItem() == piece;
    }


    /**
     * 得到当前物品的等级 0~7级
     * @param stack
     * @return
     */
    public int getLevel(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();
        return (compound != null && compound.hasKey(Reference.MODID + "_level" )) ? compound.getInteger(Reference.MODID + "_level") : 0;
    }

    /**
     * 设置当前物品的等级
     * @param stack
     * @param level
     */
    public void setLevel(ItemStack stack, int level)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();
        compound.setInteger(Reference.MODID + "_level", level);
        stack.setTagCompound(compound);
    }

    /**
     * 注入魔法，用于物品满级后的进一步提升
     * @param stack 提升的itemstack
     */
    public void infuseMagic(ItemStack stack)
    {

    }

    public EquipmentAttribute getEquipmentAttribute() {
        return equipmentAttribute != null ? equipmentAttribute : MagicEquipmentRegistry.NONE;
    }

    public void setEquipmentAttribute(EquipmentAttribute equipmentAttribute)
    {
        this.equipmentAttribute = equipmentAttribute;
    }

}
