package com.xwm.magicmaid.entity.mob.basic;

import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.entity.SPacketEntityData;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class InventoryEquipment implements IInventory
{
    public NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY); //保存背包里的信息
    private EntityEquipmentCreature equipmentOwner;

    public InventoryEquipment(EntityEquipmentCreature equipmentOwner)
    {
        this.equipmentOwner = equipmentOwner;
    }

    public InventoryEquipment(EntityEquipmentCreature equipmentOwner, NonNullList<ItemStack> inventory)
    {
        this(equipmentOwner);
        this.inventory = inventory;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    public void setInventory(NonNullList<ItemStack> inventory)
    {
        this.inventory = inventory;
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    /**
     * Returns the stack in the given slot.
     *
     * @param index
     */
    @Override
    public ItemStack getStackInSlot(int index) {

        return this.inventory.size() <= index ? ItemStack.EMPTY :
                this.inventory.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     *
     * @param index
     * @param count
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {

        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, index, count);
        if (itemstack.getItem() instanceof ItemEquipment)
            this.equipmentOwner.loseEquipment((ItemEquipment) itemstack.getItem());
        return itemstack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     *
     * @param index
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemstack = ItemStackHelper.getAndRemove(this.inventory, index);
        if (itemstack.getItem() instanceof ItemEquipment)
            this.equipmentOwner.loseEquipment((ItemEquipment) itemstack.getItem());
        return itemstack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     *
     * @param index
     * @param stack
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= 0 && index < this.inventory.size())
        {
            if (this.inventory.get(index).getItem() instanceof ItemEquipment)
                this.equipmentOwner.loseEquipment((ItemEquipment) this.inventory.get(index).getItem());
            this.inventory.set(index, stack);
            if (stack.getItem() instanceof ItemEquipment)
                this.equipmentOwner.getEquipment((ItemEquipment) stack.getItem());
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    @Override
    public void markDirty() {
        //todo 不知道实体的markDirty怎么写
        if (this.equipmentOwner.world.isRemote)
            return; //客户端不向服务器更新
        NBTTagCompound compound = new NBTTagCompound();
        ItemStackHelper.saveAllItems(compound, this.inventory);
        SPacketEntityData packet = new SPacketEntityData(this.equipmentOwner.getEntityId(), this.equipmentOwner.getEntityWorld().provider.getDimension(), 4, compound);
        NetworkLoader.instance.sendToAll(packet);
    }
    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     *
     * @param player
     */
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     *
     * @param index
     * @param stack
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    /**
     * 这个方法容易出事
     */
    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
