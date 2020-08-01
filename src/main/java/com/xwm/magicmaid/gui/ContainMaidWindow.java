package com.xwm.magicmaid.gui;

import com.xwm.magicmaid.entity.maid.EntityMagicMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainMaidWindow extends Container
{
    private InventoryPlayer player;
    private EntityMagicMaid maid;

    public ContainMaidWindow(InventoryPlayer inventory, EntityMagicMaid maid) {
        this.player = inventory;
        this.maid = maid;


        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 74 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 132));
        }


//        for (int i = 0; i < 2; i++){
//            addSlotToContainer(new Slot(maid, i, 8 + i, 8 + i * 18));
//        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
        System.out.println("Slot " + slotId + " clicked");
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
//        System.out.println("transfer slot: " + index);
//        ItemStack itemstack = ItemStack.EMPTY;
//        Slot slot = inventorySlots.get(index);
//
//        if (slot != null && slot.getHasStack()) {
//            ItemStack itemstack1 = slot.getStack();
//            itemstack = itemstack1.copy();
//
//            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
//
//            if (index < containerSlots) {
//                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
//                    return ItemStack.EMPTY;
//                }
//            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
//                return ItemStack.EMPTY;
//            }
//
//            if (itemstack1.getCount() == 0) {
//                slot.putStack(ItemStack.EMPTY);
//            } else {
//                slot.onSlotChanged();
//            }
//
//            if (itemstack1.getCount() == itemstack.getCount()) {
//                return ItemStack.EMPTY;
//            }
//
//            slot.onTake(player, itemstack1);
//        }

//        return itemstack;
        return null;
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
