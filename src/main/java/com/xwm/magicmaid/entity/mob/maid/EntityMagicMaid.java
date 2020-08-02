package com.xwm.magicmaid.entity.mob.maid;


import com.google.common.base.Predicate;
import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.ai.EntityAIMaidFollow;
import com.xwm.magicmaid.entity.ai.EntityAIMaidOwerHurtTarget;
import com.xwm.magicmaid.entity.ai.EntityAIMaidOwnerHurtByTarget;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponConviction;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponRepantence;
import com.xwm.magicmaid.entity.mob.weapon.EnumEquipments;
import com.xwm.magicmaid.object.item.ItemConviction;
import com.xwm.magicmaid.object.item.ItemEquipment;
import com.xwm.magicmaid.object.item.ItemRepantence;
import com.xwm.magicmaid.object.item.ItemWeapon;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
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

import javax.annotation.Nullable;
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
    private static final DataParameter<Integer> WEAPONTYPE = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);

    private EnumModes oldMode = null;

    public BlockPos weaponStandbyPos = new BlockPos(0, this.height+1, 0);

    public final NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY); //todo 保存背包里的信息

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
        this.dataManager.register(MODE, 1); //todo test
        this.dataManager.register(STATE, 0); //0-standard
        this.dataManager.register(WEAPONID, Optional.fromNullable(null));
        this.dataManager.register(OWNERID, Optional.fromNullable(null));
        this.dataManager.register(WEAPONTYPE, 0);
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIMaidFollow(this, 1.5, 8, 3));
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
    }

    //todo 让女仆切换状态
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        //todo
        if (EnumModes.valueOf(this.getMode()) == EnumModes.BOSS)
            return false;

        if (!world.isRemote && hand == EnumHand.MAIN_HAND){
//            this.setState((this.getState() + 1) % 4);
//            System.out.println(this.getState());
            ItemStack stack = player.getHeldItem(hand);
            if (stack.isEmpty() && this.hasOwner() && this.getOwnerID().equals(player.getUniqueID())) {
                if (player.isSneaking()) {
                    player.openGui(Main.instance, Reference.GUI_MAID_WINDOW, world, (int) this.posX, (int) this.posY, (int) this.posZ);
                }
                else if(EnumModes.valueOf(getMode()) == EnumModes.SITTING){
                    this.setMode(EnumModes.toInt(oldMode));
                }
                else {
                    this.oldMode = EnumModes.valueOf(getMode());
                    this.setMode(EnumModes.toInt(EnumModes.SITTING));
                }
                return true;
            }
            else if (stack.getItem().equals(Items.DIAMOND) && !this.hasOwner())
            {
                if(!player.isCreative())
                    stack.shrink(1);
                this.setOwnerID(player.getUniqueID());
                return true;
            }
//            ItemStack stack = player.getHeldItem(hand);
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
        compound.setInteger("weaponType", this.getWeaponType());

        if (this.getWeaponID() == null)
            compound.setString("weaponID", "");
        else
            compound.setString("weaponID", this.getWeaponID().toString());

        if (this.getOwnerID() == null)
            compound.setString("ownerID", "");
        else
            compound.setString("ownerID", this.getOwnerID().toString());

        for (int i = 0; i < this.inventory.size(); i++){
            ItemStack stack = inventory.get(i);
            if (stack.isEmpty())
                compound.setInteger("inventory" + i, -1);
            else {
                EnumEquipments j = ((ItemWeapon) (stack.getItem())).enumEquipment;
                compound.setInteger("inventory" + i, EnumEquipments.toInt(j));
            }
        }
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
        this.setWeaponType(compound.getInteger("weaponType"));

        if (compound.hasKey("weaponID") && !compound.getString("weaponID").equals(""))
            this.setWeaponID(UUID.fromString(compound.getString("weaponID")));
        else
            this.setWeaponID(null);

        if (compound.hasKey("ownerID") && !compound.getString("ownerID").equals(""))
            this.setOwnerID(UUID.fromString(compound.getString("ownerID")));
        else
            this.setOwnerID(null);

        for (int i = 0; i < this.inventory.size(); i++){
            int j = compound.getInteger("inventory" + i);
            if (j == -1){
                this.inventory.set(i, ItemStack.EMPTY);
            }
            else {
                EnumEquipments j1 = EnumEquipments.valueOf(j);
                this.inventory.set(i, new ItemStack(ItemEquipment.valueOf(j1)));
            }
        }

        this.oldMode = EnumModes.valueOf(getMode());
    }

    public boolean isSitting(){
        return EnumModes.valueOf(getMode()) == EnumModes.SITTING; //todo 让女仆待命
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

    public void setWeaponType(int type) {this.dataManager.set(WEAPONTYPE, type);}

    public int getWeaponType() {return this.dataManager.get(WEAPONTYPE);}

    public int getAttackDamage(EnumAttackTypes type){
        return 10; //todo 随着成长造成的伤害不同，不同女仆也不同
    }

    public void turnToBossMode() {
        //todo 女仆的boss形态 击败boss形态的女仆 会掉落物品，用于武器合成，女仆召唤。
    }

    public void getWeapon(ItemWeapon weapon){

        if (this.world.isRemote)
            return;

        createWeapon(EnumEquipments.toInt(weapon.enumEquipment),
                EnumEquipments.toEntityMaidWeapon(weapon.enumEquipment, world));

        //todo 得到武器后要进行一系列的操作来维护
    }

    public void loseWeapon(ItemWeapon weapon){

        if (this.world.isRemote)
            return;

        try {
            EntityMaidWeapon weapon1 = EntityMaidWeapon.getWeaponFromUUID(world, getWeaponID());
            weapon1.setDead();
            setWeaponID(null);
            setWeaponType(0);
        } catch (Exception e){
            ; //可能武器被其他模组杀死了
        }
        //todo 失去武器也要修改一系列数据来维护
    }

    public void createWeapon(int weaponType, EntityMaidWeapon weapon)
    {
        if (weapon == null)
            return;
        weapon.setMaid(this);
        weapon.setPosition(this.posX, this.posY, this.posZ);
        this.setWeaponID(weapon.getUniqueID());
        this.setWeaponType(weaponType);
        this.world.spawnEntity(weapon);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return 2;
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
            if (itemStack.getItem() instanceof ItemWeapon)
                this.loseWeapon((ItemWeapon) itemStack.getItem());
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
            if (itemstack.getItem() instanceof ItemWeapon)
                this.loseWeapon((ItemWeapon) itemstack.getItem());

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

    @Override
    public void clear() {
        this.inventory.clear();
    }


    public static EntityMagicMaid getMaidFromUUID(World world, UUID uuid)
    {
        List<EntityMagicMaid> maids = world.getEntities(EntityMagicMaid.class, new Predicate<EntityMagicMaid>() {
            @Override
            public boolean apply(@Nullable EntityMagicMaid input) {
                return input.getUniqueID().equals(uuid);
            }
        });
        if (maids.size() != 0)
            return maids.get(0);
        else
            return null;
    }
}