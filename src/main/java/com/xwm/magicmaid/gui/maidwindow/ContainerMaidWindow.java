package com.xwm.magicmaid.gui.maidwindow;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.object.item.equipment.ItemArmor;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerMaidWindow extends Container
{
    private InventoryPlayer player;
    private EntityMagicMaid maid;

    public Slot weaponSlot;
    public Slot armorSlot;

    public ContainerMaidWindow(InventoryPlayer inventory, EntityMagicMaid maid) {
        this.player = inventory;
        this.maid = maid;

        weaponSlot = new Slot(maid.inventory, 0, 8, 8){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemWeapon;
            }
        };

        armorSlot = new Slot(maid.inventory, 1, 9, 8 + 18){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemArmor;
            }
        };

        addSlotToContainer(weaponSlot);
        addSlotToContainer(armorSlot);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }


    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 2)
            {
                if (!this.mergeItemStack(itemstack1, 2, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (weaponSlot.isItemValid(itemstack))
            {
                if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (armorSlot.isItemValid(itemstack)) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Determines whether supplied player can use this container
     *
     * @param playerIn
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
