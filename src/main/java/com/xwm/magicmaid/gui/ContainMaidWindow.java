package com.xwm.magicmaid.gui;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.object.item.equipment.ItemArmor;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


public class ContainMaidWindow extends Container
{
    private InventoryPlayer player;
    private EntityMagicMaid maid;

    public Slot weaponSlot;
    public Slot armorSlot;

    public ContainMaidWindow(InventoryPlayer inventory, EntityMagicMaid maid) {
        this.player = inventory;
        this.maid = maid;

        weaponSlot = new Slot(maid, 0, 8, 8){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemWeapon;
            }
        };

        armorSlot = new Slot(maid, 1, 9, 8 + 18){
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


    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
//        System.out.println("transfer slot: " + index);
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
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
