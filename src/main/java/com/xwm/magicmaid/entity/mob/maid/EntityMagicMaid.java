package com.xwm.magicmaid.entity.mob.maid;


import com.google.common.base.Predicate;
import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.ai.EntityAIMaidFollow;
import com.xwm.magicmaid.entity.ai.EntityAIMaidOwerHurtTarget;
import com.xwm.magicmaid.entity.ai.EntityAIMaidOwnerHurtByTarget;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityTameable;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import com.google.common.base.Optional;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityMagicMaid extends EntityCreature implements IInventory
{
    /** props **/
    private static final DataParameter<Integer> MAXHEALTHBARNUM = EntityDataManager.<Integer>createKey(EntityMagicMaid.class, DataSerializers.VARINT);
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

    private EnumMode oldMode = null;

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
        this.dataManager.register(MAXHEALTHBARNUM, 10); // 100滴血每一条 10条血条
        this.dataManager.register(HEALTHBARNUM, 10);
        this.dataManager.register(LEVEL, 1);
        this.dataManager.register(EXP, 0);
        this.dataManager.register(RANK, 0);
        this.dataManager.register(HASWEAPON, false);
        this.dataManager.register(HASARMOR, false);
        this.dataManager.register(MODE, EnumMode.toInt(EnumMode.SITTING)); //todo test
        this.dataManager.register(STATE, 0); //0-standard
        this.dataManager.register(WEAPONID, Optional.fromNullable(null));
        this.dataManager.register(OWNERID, Optional.fromNullable(null));
        this.dataManager.register(WEAPONTYPE, EnumEquipment.toInt(EnumEquipment.NONE));
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);
    }

    //todo 让女仆切换状态
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        //todo
        if (EnumMode.valueOf(this.getMode()) == EnumMode.BOSS)
            return false;

        if (!world.isRemote && hand == EnumHand.MAIN_HAND){
//            this.setState((this.getState() + 1) % 4);
//            System.out.println(this.getState());
            ItemStack stack = player.getHeldItem(hand);
            if (stack.isEmpty() && this.hasOwner() && this.getOwnerID().equals(player.getUniqueID())) {
                if (player.isSneaking()) {
                    player.openGui(Main.instance, Reference.GUI_MAID_WINDOW, world, (int) this.posX, (int) this.posY, (int) this.posZ);
                }
                else if(EnumMode.valueOf(getMode()) == EnumMode.SITTING){
                    this.setMode(EnumMode.toInt(oldMode));
                }
                else {
                    this.oldMode = EnumMode.valueOf(getMode());
                    this.setMode(EnumMode.toInt(EnumMode.SITTING));
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
        if (this.getAttackTarget() != null && !this.isEnemy(this.getAttackTarget()))
            this.setAttackTarget(null);
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
                EnumEquipment j = ((ItemEquipment) (stack.getItem())).enumEquipment;
                compound.setInteger("inventory" + i, EnumEquipment.toInt(j));
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
                EnumEquipment j1 = EnumEquipment.valueOf(j);
                this.inventory.set(i, new ItemStack(ItemEquipment.valueOf(j1)));
            }
        }

        this.oldMode = EnumMode.valueOf(getMode());
    }

    public boolean isSitting(){
        return EnumMode.valueOf(getMode()) == EnumMode.SITTING; //todo 让女仆待命
    }

    public void setMaxHealthbarnum(int maxhealthbarnum){
        this.dataManager.set(MAXHEALTHBARNUM, maxhealthbarnum);
    }

    public int getMaxHealthBarnum(){
        return dataManager.get(MAXHEALTHBARNUM);
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

    public int getAttackDamage(EnumAttackType type){
        return 10; //todo 随着成长造成的伤害不同，不同女仆也不同
    }

    public int getAttackColdTime(EnumAttackType type){
        return 100;
    }

    public void turnToBossMode() {
        //todo 女仆的boss形态 击败boss形态的女仆 会掉落物品，用于武器合成，女仆召唤。
    }

    public void getEquipment(ItemEquipment equipment){

    }

    public void loseEquipment(ItemEquipment equipment){

    }

    public void switchMode(){

    }

    public boolean isEnemy(EntityLivingBase entityLivingBase)
    {
        if (entityLivingBase == null)
            return false;
        if (this == entityLivingBase)
            return false;
        if (this.getOwnerID() == entityLivingBase.getUniqueID())
            return false;
        if (entityLivingBase instanceof EntityMagicMaid && ((EntityMagicMaid) entityLivingBase).hasOwner() && this.getOwnerID() == ((EntityMagicMaid) entityLivingBase).getOwnerID())
            return false;
        if (entityLivingBase instanceof EntityTameable && ((EntityTameable) entityLivingBase).getOwnerId() != null && this.getOwnerID() == ((EntityTameable) entityLivingBase).getOwnerId())
            return false;
        if (entityLivingBase instanceof EntityMaidWeapon)
            return false;
        if (entityLivingBase instanceof EntityBat)
            return false;
        return true;
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

    @Override
    public void clear() {
        this.inventory.clear();
    }


    public static EntityMagicMaid getMaidFromUUID(World world, UUID uuid)
    {
        try{
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
        } catch (NullPointerException e){
            return null;
        }

    }

    @Override
    public void onDeathUpdate(){
        if (this.getHealthBarNum() > 0){ //如果血条没掉完 是不会死的
            this.setHealthbarnum(this.getHealthBarNum()-1);
            this.setHealth(this.getMaxHealth());
        }
        else{
            super.onDeathUpdate();
        }
    }

    @Override
    public void setDead(){
        if (getTrueHealth() == 0) { //血条没掉完不允许被杀死 所以指令应该没用
            super.setDead();
        }
    }

    @Override
    public void setHealth(float health) //todo 这里可能有问题 要测试一下
    {
        float curHealth = getHealth();
        if (curHealth - health > 100){
            World world = getEntityWorld();
            if (!world.isRemote) {
                try {
                    EntityPlayer player = world.getClosestPlayerToEntity(this, 20);
                    player.sendMessage(new TextComponentString("消失吧！"));
                    FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(this, "clear " + player.getName());
                } catch (Exception e){
                    ;
                }
            }
            return;
        }
        super.setHealth(health);
    }

    public float getTrueMaxHealth(){
        return getMaxHealthBarnum() * getMaxHealth();
    }

    public float getTrueHealth(){
        return getMaxHealth() * getHealthBarNum() + getHealth();
    }

    public void heal(float healAmount){
        if (healAmount < 0)
            return;
        float t = healAmount + getHealth();
        int barHeal = (int)(t / getMaxHealth());
        float heal = t - barHeal * getMaxHealth();
        setHealthbarnum(Math.min(getHealthBarNum()+barHeal, getMaxHealthBarnum()));
        super.heal(heal);
    }

    @Override
    public void setNoAI(boolean disabled)
    {
        return; //不允许暂停ai
    }

    public void debug(){
        System.out.println("state: " + EnumRettState.valueOf(this.getState())
                + " mode: " + EnumMode.valueOf(this.getMode())
                + " owner: " + this.hasOwner() + " Equipment: " + EnumEquipment.valueOf(this.getWeaponType())
                + " rank: " + this.getRank() + " health: " + this.getTrueHealth());
    }
}
