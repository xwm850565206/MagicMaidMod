package com.xwm.magicmaid.entity.mob.weapon;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityMaidWeapon extends EntityLivingBase
{
    public static EnumEquipment enumEquipment;

    private static final DataParameter<com.google.common.base.Optional<UUID>> OWNERID = EntityDataManager.<Optional<UUID>>createKey(EntityMaidWeapon.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Boolean> BINARY_MODE = EntityDataManager.createKey(EntityMaidWeapon.class, DataSerializers.BOOLEAN);

    public EntityMagicMaid maid = null; //作为女仆的武器时
    public EntityLivingBase otherOwner = null; //拥有一个临时的主人，主要是为了玩家可以右键丢出使用

    public AxisAlignedBB boundBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);;

    public EntityMaidWeapon(World worldIn) {
        super(worldIn);
        this.setSize(0.2f, 0.4f);

        this.isImmuneToFire = true;
        this.noClip = true;
        this.setNoGravity(true); // 没有引力
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(OWNERID, Optional.fromNullable(null));
        this.dataManager.register(BINARY_MODE, false);
    }

    public boolean getAlwaysRenderNameTag()
    {
        return false;
    }

    @Override
    public void onLivingUpdate()
    {
       if (!getBinaryMode())
           doMaidOwnerUpdate();
       else
           doOhterOwnerUpdate();

        super.onLivingUpdate();
    }

    protected void doMaidOwnerUpdate()
    {
        if (!world.isRemote)
        {
            if (maid == null && this.getOwnerID() != null){
                maid = EntityMagicMaid.getMaidFromUUID(world, this.getOwnerID());
                if (maid == null || maid.isDead) {
                    this.setDead();
                }
            }

            if (maid != null) {
                if (maid.isDead)
                    this.setDead();
                else
                {
                    //跟随主人
                    BlockPos pos = this.maid.weaponStandbyPos.add(this.maid.posX, this.maid.posY, this.maid.posZ);
                    this.moveToBlockPosAndAngles(pos, this.rotationYaw, this.rotationPitch);
                }
            }
        }
    }

    /**
     * 那些可以被玩家使用的武器需要复写这个方法，在onLivingUpdate里被调用
     */
    protected void doOhterOwnerUpdate() {
        if (!world.isRemote && otherOwner == null) {
//            EntityItem item = new EntityItem(world, posX, posY, posZ, this.activeItemStack);
//            world.spawnEntity(item);
            this.setDead();
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return null;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return new ArrayList<ItemStack>();
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {

    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     *
     * @param compound
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("ownerID") && !compound.getString("ownerID").equals(""))
            this.setOwnerID(UUID.fromString(compound.getString("ownerID")));
        else
            this.setOwnerID(null);

        this.setBinaryMode(compound.getBoolean("binary_mode"));
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        if (this.getOwnerID() == null)
            compound.setString("ownerID", "");
        else
            compound.setString("ownerID", this.getOwnerID().toString());

        compound.setBoolean("binary_mode", getBinaryMode());
    }

    public void setOwnerID(UUID uuid){
        this.dataManager.set(OWNERID, Optional.fromNullable(uuid));
    }

    public UUID getOwnerID(){
        return (UUID)((Optional)this.dataManager.get(OWNERID)).orNull();
    }

    public void setBinaryMode(boolean mode) {
        this.dataManager.set(BINARY_MODE, mode);
    }

    public Boolean getBinaryMode() {
        return this.dataManager.get(BINARY_MODE);
    }


    @Override
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return this.boundBox;
    }


    public void setMaid(EntityMagicMaid maid){
        this.maid = maid;
        this.setBinaryMode(false);
        this.setOwnerID(maid.getUniqueID());
    }

    public void setOtherOwner(EntityLivingBase otherOwner) {
        this.otherOwner = otherOwner;
        this.noClip = false;
        this.boundBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0).grow(0.1);
        this.setBinaryMode(true);
    }

    public static EntityMaidWeapon getWeaponFromUUID(World world, UUID uuid)
    {
        try{
            List<EntityMaidWeapon> weapons = world.getEntities(EntityMaidWeapon.class, new Predicate<EntityMaidWeapon>() {
                @Override
                public boolean apply(@Nullable EntityMaidWeapon input) {
                    return input.getUniqueID().equals(uuid);
                }
            });
            if (weapons.size() != 0)
                return weapons.get(0);
            else
                return null;
        } catch (NullPointerException e)
        {
            return null;
        }

    }
}
