package com.xwm.magicmaid.entity.maid;


import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.ai.EntityAIMaidFollow;
import com.xwm.magicmaid.entity.ai.EntityAIMaidOwerHurtTarget;
import com.xwm.magicmaid.entity.ai.EntityAIMaidOwnerHurtByTarget;
import com.xwm.magicmaid.entity.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.object.item.ItemWeapon;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.google.common.base.Optional;

import java.util.List;
import java.util.UUID;

public class EntityMagicMaid extends EntityCreature implements IInventory
{
    /** props **/
    private static final DataParameter<Integer> HEALTHBARNUM = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> LEVEL = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> EXP = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HASWEAPON = EntityDataManager.<Boolean>createKey(EntityMagicMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HASARMOR = EntityDataManager.<Boolean>createKey(EntityMagicMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> MODE = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<UUID>> WEAPONID = EntityDataManager.<Optional<UUID>>createKey(EntityMagicMaid.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> RANK = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<UUID>> OWNERID = EntityDataManager.<Optional<UUID>>createKey(EntityMagicMaid.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public BlockPos weaponStandbyPos = new BlockPos(0, this.height+1, 0);

    public final NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);

    public EntityMagicMaid(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 1.2F);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(HEALTHBARNUM, 10);
        this.dataManager.register(LEVEL, 1);
        this.dataManager.register(EXP, 1000);
        this.dataManager.register(RANK, 2);
        this.dataManager.register(HASWEAPON, false);
        this.dataManager.register(HASARMOR, false);
        this.dataManager.register(MODE, 0);
        this.dataManager.register(STATE, 0);
        this.dataManager.register(WEAPONID, Optional.fromNullable(null));
        this.dataManager.register(OWNERID, Optional.fromNullable(null));
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIMaidFollow(this, 1.0, 8, 3));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAIMaidOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIMaidOwerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));

    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1000000000);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000000298023224D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
//        if (!world.isRemote)
//        {
//            System.out.println("interact");
//            createWeapon(0);
//            return true;
//        }
        //todo
        if (!world.isRemote){
//            this.setState((this.getState() + 1) % 4);
//            System.out.println(this.getState());
            ItemStack stack = player.getHeldItem(hand);
//            System.out.println("ownerID: " + this.getOwnerID());
            if (stack.getItem().equals(Items.DIAMOND) && !this.hasOwner())
            {
//                System.out.println("good");
                stack.shrink(1);
                this.setOwnerID(player.getUniqueID());
                return true;
            }
        }

        if (world.isRemote)
        {
//            createWeapon(0);
//            player.openGui(Main.instance, Reference.GUI_MAID_WINDOW, world, (int)this.posX, (int)this.posY, (int)this.posZ);
            return true;
        }

        return false;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("healthBarNum", this.getHealthBarNum());
        compound.setInteger("level", this.getLevel());
        compound.setInteger("exp", this.getExp());
        compound.setBoolean("hasWeapon", this.hasWeapon());
        compound.setBoolean("hasArmor", this.hasArmor());
        compound.setInteger("mode", this.getMode());
        compound.setInteger("state", this.getState());

        if (this.getWeaponID() == null)
            compound.setString("weaponID", "");
        else
            compound.setString("weaponID", this.getWeaponID().toString());

        if (this.getOwnerID() == null)
            compound.setString("ownerID", "");
        else
            compound.setString("ownerID", this.getOwnerID().toString());

    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setHealthbarnum(compound.getInteger("healthBarNum"));
        this.setLevel(compound.getInteger("level"));
        this.setExp(compound.getInteger("exp"));
        this.setHasWeapon(compound.getBoolean("hasWeapon"));
        this.setHasArmor(compound.getBoolean("hasArmor"));
        this.setMode(compound.getInteger("mode"));
        this.setState(compound.getInteger("state"));

        if (compound.hasKey("weaponID") && !compound.getString("weaponID").equals(""))
            this.setWeaponID(UUID.fromString(compound.getString("weaponID")));
        else
            this.setWeaponID(null);

        if (compound.hasKey("ownerID") && !compound.getString("ownerID").equals(""))
            this.setOwnerID(UUID.fromString(compound.getString("ownerID")));
        else
            this.setOwnerID(null);
    }

    public boolean isSitting(){
        return false; //todo 让女仆待命
    }

    public void setHealthbarnum(int healthbarnum){
        this.dataManager.set(HEALTHBARNUM, healthbarnum);
    }

    public int getHealthBarNum(){
        return this.dataManager.get(HEALTHBARNUM);
    }

    public void setExp(int exp){
        this.dataManager.set(EXP, exp);
    }

    public int getExp(){
        return this.dataManager.get(EXP);
    }

    public void setLevel(int level){
        this.dataManager.set(LEVEL, level);
    }

    public int getLevel(){
        return this.dataManager.get(LEVEL);
    }

    public void setHasWeapon(boolean hasWeapon)
    {
        this.dataManager.set(HASWEAPON, hasWeapon);
    }

    public boolean hasWeapon(){
        return this.dataManager.get(HASWEAPON);
    }

    public void setHasArmor(boolean hasArmor){
        this.dataManager.set(HASARMOR, hasArmor);
    }

    public boolean hasArmor(){
        return this.dataManager.get(HASARMOR);
    }

    public void setMode(int mode){
        this.dataManager.set(MODE, mode);
    }

    public int getMode(){
        return this.dataManager.get(MODE);
    }

    public int getState() {
        return this.dataManager.get(STATE);
    }

    public void setState(int state) {
        this.dataManager.set(STATE, state);
    }

    public void setWeaponID(UUID uuid){
        this.dataManager.set(WEAPONID, Optional.fromNullable(uuid));
    }

    public UUID getWeaponID(){
        return (UUID)((Optional)this.dataManager.get(WEAPONID)).orNull();
    }

    public void setOwnerID(UUID uuid){
        this.dataManager.set(OWNERID, Optional.fromNullable(uuid));
    }

    public UUID getOwnerID(){
        return (UUID)((Optional)this.dataManager.get(OWNERID)).orNull();
    }

    public boolean hasOwner(){
        return this.getOwnerID() != null;
    }

    public void setRank(int rank) {this.dataManager.set(RANK, rank);}

    public int getRank() {return this.dataManager.get(RANK);}

    public void turnToBossMode() {
        //todo 女仆的boss形态 击败boss形态的女仆 会掉落物品，用于武器合成，女仆召唤。
    }

    public void getWeapon(ItemWeapon weapon){
        createWeapon(0);
        //todo 得到武器后要进行一系列的操作来维护
    }

    public void loseWeapon(ItemWeapon weapon){
//        EntityMaidWeapon weapon1 = this.world.getPlayerEntityByUUID(this.getWeaponID());
//        weapon1.setDead();
        //todo 失去武器也要修改一系列数据来维护
    }

    public void createWeapon(int weaponID)
    {
        EntityMaidWeapon weapon = new EntityMaidWeapon(this.world);
        weapon.setMaid(this);
        weapon.setPosition(this.posX, this.posY, this.posZ);
        this.setWeaponID(weapon.getUniqueID());
        this.world.spawnEntity(weapon);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
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
        return list != null && !((ItemStack)list.get(index)).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
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
            nonnulllist.set(index, ItemStack.EMPTY);

            if (itemstack.getItem() instanceof ItemWeapon)
                this.loseWeapon((ItemWeapon) itemstack.getItem());

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
            if (stack.getItem() instanceof ItemWeapon)
                this.getWeapon((ItemWeapon) stack.getItem());
            nonnulllist.set(index, stack);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    @Override
    public int getInventoryStackLimit() {
        return 2;
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

    @Override
    public void clear() {
        this.inventory.clear();
    }
}
