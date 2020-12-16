package com.xwm.magicmaid.gui.magiccircle;

import com.xwm.magicmaid.object.tileentity.TileEntityMagicCircle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.List;

public class ContainerMagicCircle extends Container
{
    public static List<Vector2d> SLOT_POSITION = new ArrayList<Vector2d>() {{
       add(new Vector2d(13, 8));
       add(new Vector2d(59, 8));
       add(new Vector2d(13, 54));
       add(new Vector2d(59, 54));
       add(new Vector2d(35, 32));
       add(new Vector2d(140, 15));
       add(new Vector2d(140, 15 + 18));
       add(new Vector2d(140, 15 + 18 * 2));
    }};

    private TileEntityMagicCircle magicCircle;

    public ContainerMagicCircle(InventoryPlayer inventory, TileEntityMagicCircle magicCircleIn)
    {
        this.magicCircle = magicCircleIn;

        //放置原料槽位
        addSlotToContainer(new Slot(magicCircle, 0, 13, 8){
//            @Override
//            public void onSlotChanged() {
//                magicCircle.markDirty();
//            }
        });
        addSlotToContainer(new Slot(magicCircle, 1, 59, 8) {
//            @Override
//            public void onSlotChanged() {
//                magicCircle.markDirty();
//            }
        });
        addSlotToContainer(new Slot(magicCircle, 2, 13, 54) {
//            @Override
//            public void onSlotChanged() {
//                magicCircle.markDirty();
//            }
        });
        addSlotToContainer(new Slot(magicCircle, 3, 59, 54) {
//            @Override
//            public void onSlotChanged() {
//                magicCircle.markDirty();
//            }
        });

        //放置烧炼物槽位
        addSlotToContainer(new Slot(magicCircle, 4, 35, 32) {
//            @Override
//            public void onSlotChanged() {
//                magicCircle.markDirty();
//            }
        });

        //生成物槽位
        addSlotToContainer(new Slot(magicCircle, 5, 140, 15) {
//            @Override
//            public void onSlotChanged() {
//                magicCircle.markDirty();
//            }
        });
        addSlotToContainer(new Slot(magicCircle, 6, 140, 15 + 18) {
//            @Override
//            public void onSlotChanged() {
//                magicCircle.markDirty();
//            }
        });
        addSlotToContainer(new Slot(magicCircle, 7, 140, 15 + 18 * 2) {
//            @Override
//            public void onSlotChanged() {
//                magicCircle.markDirty();
//            }
        });

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
