package com.xwm.magicmaid.entity.mob.basic;

import com.google.common.base.Optional;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityEquipmentCreature;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.entity.SPacketEntityData;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class EntityEquipmentCreature extends EntityMagicRankCreature implements IEntityEquipmentCreature
{
    public NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY); //保存背包里的信息

    private static final DataParameter<Boolean> IS_FIRST_GET_ARMOR = EntityDataManager.createKey(EntityEquipmentCreature.class, DataSerializers.BOOLEAN); //第一次获得防具血量调整
    private static final DataParameter<Optional<UUID>> WEAPON_ID = EntityDataManager.<Optional<UUID>>createKey(EntityEquipmentCreature.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<String> WEAPON_TYPE = EntityDataManager.<String>createKey(EntityEquipmentCreature.class, DataSerializers.STRING);
    private static final DataParameter<String> ARMOR_TYPE = EntityDataManager.<String>createKey(EntityEquipmentCreature.class, DataSerializers.STRING);

    public EntityEquipmentCreature(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_FIRST_GET_ARMOR, true);
        this.dataManager.register(WEAPON_ID, Optional.fromNullable(null));
        this.dataManager.register(WEAPON_TYPE, MagicEquipmentRegistry.NONE.getName());
        this.dataManager.register(ARMOR_TYPE, MagicEquipmentRegistry.NONE.getName());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isFirstGetArmor", this.isFirstGetArmor());
        compound.setString("weaponType", this.getWeaponType());
        compound.setString("armorType", this.getArmorType());

        if (this.getWeaponID() == null)
            compound.setString("weaponID", "");
        else
            compound.setString("weaponID", this.getWeaponID().toString());

        ItemStackHelper.saveAllItems(compound, this.inventory);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setFirstGetArmor(compound.getBoolean("isFirstGetArmor"));
        this.setWeaponType(compound.getString("weaponType"));
        this.setArmorType(compound.getString("armorType"));
        this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventory);
        for (ItemStack itemStack : inventory) {
            if (itemStack.getItem() instanceof ItemEquipment)
                this.getEquipment((ItemEquipment) itemStack.getItem());
        }

        this.getEquipment(MagicEquipmentRegistry.getAttribute(getWeaponType()).getEquipment());
        this.getEquipment(MagicEquipmentRegistry.getAttribute(getArmorType()).getEquipment());

        super.readEntityFromNBT(compound);
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);
        if (getTrueHealth() <= 0 && !world.isRemote && shouldDropEquipment())
            InventoryHelper.dropInventoryItems(world, this, this);
    }

    @Override
    public boolean isFirstGetArmor() {
        return this.dataManager.get(IS_FIRST_GET_ARMOR);
    }

    @Override
    public void setFirstGetArmor(boolean isFirstGetArmor) {
        this.dataManager.set(IS_FIRST_GET_ARMOR, isFirstGetArmor);
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
    public void setWeaponType(String type) {
        this.dataManager.set(WEAPON_TYPE, type);
    }

    @Override
    public String getWeaponType() {
        return this.dataManager.get(WEAPON_TYPE);
    }

    @Override
    public void setArmorType(String type) {
        this.dataManager.set(ARMOR_TYPE, type);
    }

    @Override
    public String getArmorType() {
        return this.dataManager.get(ARMOR_TYPE);
    }


    @Override
    public abstract void getEquipment(ItemEquipment equipment);

    @Override
    public abstract void loseEquipment(ItemEquipment equipment);

    @Override
    public ItemStack getWeaponFromSlot()
    {
        return this.inventory.get(0);
    }

    @Override
    public ItemStack getArmorFromSlot()
    {
        return this.inventory.get(1);
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

        return ItemStackHelper.getAndSplit(this.inventory, index, count);
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
            this.loseEquipment((ItemEquipment) itemstack.getItem());
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
                this.loseEquipment((ItemEquipment) this.inventory.get(index).getItem());
            this.inventory.set(index, stack);
            if (stack.getItem() instanceof ItemEquipment)
                this.getEquipment((ItemEquipment) stack.getItem());
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
        if (world.isRemote)
            return; //客户端不向服务器更新
        NBTTagCompound compound = new NBTTagCompound();
        ItemStackHelper.saveAllItems(compound, this.inventory);
        SPacketEntityData packet = new SPacketEntityData(this.getEntityId(), this.getEntityWorld().provider.getDimension(), 4, compound);
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

    protected boolean shouldDropEquipment() {
        return true;
    }

}
