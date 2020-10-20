package com.xwm.magicmaid.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainMagicCircle extends Container
{
    public IInventory magicCircle;

    public ContainMagicCircle(InventoryPlayer inventory, IInventory magicCircle)
    {
        this.magicCircle = magicCircle;

        //放置原料槽位
        addSlotToContainer(new Slot(magicCircle, 0, 13, 8));
        addSlotToContainer(new Slot(magicCircle, 1, 59, 8));
        addSlotToContainer(new Slot(magicCircle, 2, 13, 54));
        addSlotToContainer(new Slot(magicCircle, 3, 59, 54));

        //放置烧炼物槽位
        addSlotToContainer(new Slot(magicCircle, 4, 36, 30));

        //生成物槽位
        addSlotToContainer(new Slot(magicCircle, 5, 140, 15));
        addSlotToContainer(new Slot(magicCircle, 6, 140, 15 + 18));
        addSlotToContainer(new Slot(magicCircle, 7, 140, 15 + 18 * 2));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(inventory, k, 8 + k * 18, 142));
        }
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
