package com.xwm.magicmaid.entity.mob.basic;

import com.google.common.base.Optional;
import com.xwm.magicmaid.entity.ai.EntityAIMagicCreatureOwerHurtTarget;
import com.xwm.magicmaid.entity.ai.EntityAIMagicCreatureOwnerHurtByTarget;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityTameableCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityTameableCreature extends EntityMagicRankCreature implements IEntityTameableCreature
{

    private static final DataParameter<Optional<UUID>> OWNERID = EntityDataManager.<Optional<UUID>>createKey(EntityMagicMaid.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private EnumMode oldMode = null;

    public EntityTameableCreature(World worldIn) {
        super(worldIn);
    }

    public boolean isEnemy(EntityLivingBase entityLivingBase)
    {
        boolean flag = super.isEnemy(entityLivingBase);
        if (!flag)
            return false;
        else
        {
            if (this.getOwnerID() != null && this.getOwnerID().equals(entityLivingBase.getUniqueID()))
                return false;
            if (entityLivingBase instanceof IEntityTameableCreature && ((IEntityTameableCreature) entityLivingBase).hasOwner() && this.getOwnerID().equals(((IEntityTameableCreature) entityLivingBase).getOwnerID()))
                return false;
            if (entityLivingBase instanceof EntityTameable && ((EntityTameable) entityLivingBase).getOwnerId() != null && this.getOwnerID().equals(((EntityTameable) entityLivingBase).getOwnerId()))
                return false;

            return true;
        }
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.targetTasks.addTask(1, new EntityAIMagicCreatureOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIMagicCreatureOwerHurtTarget(this));

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(OWNERID, Optional.fromNullable(null));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("level", this.getLevel());
        compound.setInteger("exp", this.getExp());
        compound.setInteger("rank", this.getRank());

        if (this.getOwnerID() == null)
            compound.setString("ownerID", "");
        else
            compound.setString("ownerID", this.getOwnerID().toString());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("ownerID") && !compound.getString("ownerID").equals(""))
            this.setOwnerID(UUID.fromString(compound.getString("ownerID")));
        else
            this.setOwnerID(null);

        this.oldMode = EnumMode.valueOf(getMode());
    }


    @Override
    public void setOwnerID(UUID uuid) {
        this.dataManager.set(OWNERID, Optional.fromNullable(uuid));
    }

    @Override
    public UUID getOwnerID() {
        return (UUID)((Optional)this.dataManager.get(OWNERID)).orNull();
    }

    @Override
    public boolean hasOwner() {
        return this.getOwnerID() != null;
    }

    @Override
    public boolean isSitting() {
        return EnumMode.valueOf(getMode()) == EnumMode.SITTING;
    }

    @Override
    public void setSitting(boolean sitting) {
        if (sitting){
            oldMode = EnumMode.valueOf(getMode());
            setMode(EnumMode.toInt(EnumMode.SITTING));
        }
        else {
            if (oldMode == null) oldMode = EnumMode.SERVE;
            setMode(EnumMode.toInt(oldMode));
        }
    }
}
