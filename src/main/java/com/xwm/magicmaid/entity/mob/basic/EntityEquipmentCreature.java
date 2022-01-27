package com.xwm.magicmaid.entity.mob.basic;

import com.google.common.base.Optional;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityEquipmentCreature;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
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
    public InventoryEquipment inventory = new InventoryEquipment(this);

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

        ItemStackHelper.saveAllItems(compound, this.inventory.getInventory());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setFirstGetArmor(compound.getBoolean("isFirstGetArmor"));
        this.setWeaponType(compound.getString("weaponType"));
        this.setArmorType(compound.getString("armorType"));
        this.inventory.setInventory(NonNullList.<ItemStack>withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY));
        ItemStackHelper.loadAllItems(compound, this.inventory.getInventory());
        for (ItemStack itemStack : inventory.getInventory()) {
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
            InventoryHelper.dropInventoryItems(world, this, this.inventory);
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
        return this.inventory.getStackInSlot(0);
    }

    @Override
    public ItemStack getArmorFromSlot()
    {
        return this.inventory.getStackInSlot(1);
    }


    protected boolean shouldDropEquipment() {
        return true;
    }

    @Override
    public InventoryEquipment getInventory() {
        return this.inventory;
    }

    @Override
    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory.setInventory(inventory);
    }
}
