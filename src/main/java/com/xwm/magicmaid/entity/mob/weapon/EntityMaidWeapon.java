package com.xwm.magicmaid.entity.mob.weapon;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
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
    public static EnumEquipments enumEquipment;

    private static final DataParameter<com.google.common.base.Optional<UUID>> OWNERID = EntityDataManager.<Optional<UUID>>createKey(EntityMagicMaid.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityMagicMaid maid = null;
    protected AxisAlignedBB boundBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);;

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
    }

    public void setHealth(float health)
    {
        return; //防止被误杀
    }


    @Override
    public void onUpdate()
    {
        if (!world.isRemote)
        {
            if (maid == null && this.getOwnerID() != null){
                maid = EntityMagicMaid.getMaidFromUUID(world, this.getOwnerID());
                if (maid == null)
                    this.setDead();
            }

            if (maid != null) {
                BlockPos standPos = maid.getPosition();
                standPos = standPos.add(maid.weaponStandbyPos.getX(), maid.weaponStandbyPos.getY(), maid.weaponStandbyPos.getZ());
                setPosition(standPos.getX(), standPos.getY(), standPos.getZ());
            }
        }

        super.onUpdate();
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


    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        if (this.getOwnerID() == null)
            compound.setString("ownerID", "");
        else
            compound.setString("ownerID", this.getOwnerID().toString());
    }

    public void setOwnerID(UUID uuid){
        this.dataManager.set(OWNERID, Optional.fromNullable(uuid));
    }

    public UUID getOwnerID(){
        return (UUID)((Optional)this.dataManager.get(OWNERID)).orNull();
    }


    @Override
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return this.boundBox;
    }


    public void setMaid(EntityMagicMaid maid){
        this.maid = maid;
        this.setOwnerID(maid.getUniqueID());
    }

    public static EntityMaidWeapon getWeaponFromUUID(World world, UUID uuid)
    {
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
    }
}
