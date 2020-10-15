package com.xwm.magicmaid.entity.mob.basic;

import com.google.common.base.Optional;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityEquipmentCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public abstract class EntityEquipmentCreature extends EntityMagicRankCreature implements IEntityEquipmentCreature
{
    protected static int max_inventory_size = 2;
    public final NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(max_inventory_size, ItemStack.EMPTY); //保存背包里的信息

    private static final DataParameter<Boolean> HAS_WEAPON = EntityDataManager.<Boolean>createKey(EntityMagicMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_ARMOR = EntityDataManager.<Boolean>createKey(EntityMagicMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<UUID>> WEAPON_ID = EntityDataManager.<Optional<UUID>>createKey(EntityMagicMaid.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> WEAPON_TYPE = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ARMOR_TYPE = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);

    public EntityEquipmentCreature(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HAS_WEAPON, false);
        this.dataManager.register(HAS_ARMOR, false);
        this.dataManager.register(WEAPON_ID, Optional.fromNullable(null));
        this.dataManager.register(WEAPON_TYPE, EnumEquipment.toInt(EnumEquipment.NONE));
        this.dataManager.register(ARMOR_TYPE, EnumEquipment.toInt(EnumEquipment.NONE));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("hasWeapon", this.hasWeapon());
        compound.setBoolean("hasArmor", this.hasArmor());
        compound.setInteger("weaponType", this.getWeaponType());
        compound.setInteger("armorType", this.getArmorType());

        if (this.getWeaponID() == null)
            compound.setString("weaponID", "");
        else
            compound.setString("weaponID", this.getWeaponID().toString());

        for (int i = 0; i < this.inventory.size(); i++){
            ItemStack stack = inventory.get(i);
            if (stack.isEmpty())
                compound.setInteger("inventory" + i, -1);
            else {
                EnumEquipment j = ((ItemEquipment) (stack.getItem())).enumEquipment;
                compound.setInteger("inventory" + i, EnumEquipment.toInt(j));
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setHasWeapon(compound.getBoolean("hasWeapon"));
        this.setHasArmor(compound.getBoolean("hasArmor"));
        this.setWeaponType(compound.getInteger("weaponType"));
        this.setArmorType(compound.getInteger("armorType"));

        for (int i = 0; i < this.inventory.size(); i++) {
            int j = compound.getInteger("inventory" + i);
            if (j == -1) {
                this.inventory.set(i, ItemStack.EMPTY);
            } else {
                EnumEquipment j1 = EnumEquipment.valueOf(j);
                this.inventory.set(i, new ItemStack(ItemEquipment.valueOf(j1)));
            }
        }
    }


    @Override
    public void setHasWeapon(boolean hasWeapon) {
        this.dataManager.set(HAS_WEAPON, hasWeapon);
    }

    @Override
    public boolean hasWeapon() {
        return this.dataManager.get(HAS_WEAPON);
    }

    @Override
    public void setHasArmor(boolean hasArmor) {
        this.dataManager.set(HAS_ARMOR, hasArmor);
    }

    @Override
    public boolean hasArmor() {
        return this.dataManager.get(HAS_ARMOR);
    }

    @Override
    public void setWeaponID(UUID uuid) {
        this.dataManager.set(WEAPON_ID, Optional.fromNullable(uuid));
    }

    @Override
    public UUID getWeaponID() {
        return (UUID)((Optional)this.dataManager.get(WEAPON_ID)).orNull();
    }

    @Override
    public void setWeaponType(int type) {
        this.dataManager.set(WEAPON_TYPE, type);
    }

    @Override
    public int getWeaponType() {
        return this.dataManager.get(WEAPON_TYPE);
    }

    @Override
    public void setArmorType(int type) {
        this.dataManager.set(ARMOR_TYPE, type);
    }

    @Override
    public int getArmorType() {
        return this.dataManager.get(ARMOR_TYPE);
    }


    @Override
    public abstract void getEquipment(ItemEquipment equipment);

    @Override
    public abstract void loseEquipment(ItemEquipment equipment);


    @Override
    public int getSizeInventory() {
        return max_inventory_size;
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
        List<ItemStack> list = null;
        if (inventory.size() > index)
            list = inventory;
        if (list != null && !((ItemStack)list.get(index)).isEmpty() )
        {
            ItemStack itemStack = ItemStackHelper.getAndSplit(list, index, count);
            if (itemStack.getItem() instanceof ItemEquipment)
                this.loseEquipment((ItemEquipment) itemStack.getItem());
            return itemStack;
        }
        else return ItemStack.EMPTY;
    }

    /**
     * Removes a stack from the given slot and returns it.
     *
     * @param index
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {
        NonNullList<ItemStack> nonnulllist = null;
        if (inventory.size() > index)
            nonnulllist = inventory;
        if (nonnulllist != null && !((ItemStack)nonnulllist.get(index)).isEmpty())
        {
            ItemStack itemstack = nonnulllist.get(index);
            if (itemstack.getItem() instanceof ItemEquipment)
                this.loseEquipment((ItemEquipment) itemstack.getItem());

            nonnulllist.set(index, ItemStack.EMPTY);

            return itemstack;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     *
     * @param index
     * @param stack
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = null;
        if (inventory.size() > index)
            nonnulllist = inventory;
        if (nonnulllist != null) {
            if (stack.getItem() instanceof ItemEquipment)
                this.getEquipment((ItemEquipment) stack.getItem());
            nonnulllist.set(index, stack);
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

}
