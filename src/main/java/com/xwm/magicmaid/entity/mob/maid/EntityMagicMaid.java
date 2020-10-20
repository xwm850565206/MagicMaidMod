package com.xwm.magicmaid.entity.mob.maid;


import com.google.common.base.Predicate;
import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.ai.*;
import com.xwm.magicmaid.entity.mob.basic.EntityEquipmentCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityTameableCreature;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.ParticlePacket;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.handlers.PunishOperationHandler;
import com.xwm.magicmaid.world.dimension.DimensionChurch;
import com.xwm.magicmaid.world.dimension.MagicMaidFightManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import com.google.common.base.Optional;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityMagicMaid extends EntityEquipmentCreature implements IEntityTameableCreature
{

    /**
     * IEntityTameableCreature
     */
    private static final DataParameter<Optional<UUID>> OWNERID = EntityDataManager.<Optional<UUID>>createKey(EntityMagicMaid.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private EnumMode oldMode = null;

    protected EntityMaidWeapon weapon = null;
    public BlockPos weaponStandbyPos = new BlockPos(0, this.height+1, 0);

    public EntityMagicMaid(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 1.2F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(OWNERID, Optional.fromNullable(null));
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(3, new EntityAIMaidFollow(this, 1.5, 8, 3));

        this.targetTasks.addTask(2, new EntityAINearestAttackableTargetAvoidOwner(this, EntityMob.class, true, new EnemySelect(this)));
        this.targetTasks.addTask(1, new EntityAIMagicCreatureOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIMagicCreatureOwerHurtTarget(this));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000000298023224D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (EnumMode.valueOf(this.getMode()) == EnumMode.BOSS)
            return false;

        if (!world.isRemote && hand == EnumHand.MAIN_HAND){
            ItemStack stack = player.getHeldItem(hand);
            //打开ui
            if (player.isSneaking() && this.hasOwner() && this.getOwnerID().equals(player.getUniqueID())){
                player.openGui(Main.instance, Reference.GUI_MAID_WINDOW, world, (int) this.posX, (int) this.posY, (int) this.posZ);
                /*SoundPacket packet = new SoundPacket(1, getPosition());
                NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 40.0D);
                NetworkLoader.instance.sendToAllAround(packet, target);*/
            } //转换模式
            else if (stack.isEmpty() && this.hasOwner() && this.getOwnerID().equals(player.getUniqueID())) {
                if(EnumMode.valueOf(getMode()) == EnumMode.SITTING){
                    this.setSitting(false);
                }
                else {
                    this.setSitting(true);
                }
                return true;
            }
            //设置主人
            else if (stack.getItem().equals(ItemInit.itemLostKey) && !this.hasOwner())
            {
                if(!player.isCreative())
                    stack.shrink(1);
                this.setOwnerID(player.getUniqueID());
                for (int i = 0; i < 4; i++) {
                    ParticlePacket particlePacket = new ParticlePacket(
                            this.posX + rand.nextDouble() * 0.1,
                            this.posY + this.height + rand.nextDouble(),
                            this.posZ + rand.nextDouble() * 0.1, EnumParticleTypes.HEART);
                    NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(this.getEntityWorld().provider.getDimension(), posX, posY, posZ, 40.0D);
                    NetworkLoader.instance.sendToAllAround(particlePacket, target);
                }
                return true;
            }
            //升阶 todo 经验系统被修改过了还没检查
            else if (stack.getItem().equals(ItemInit.itemRemainingSoft)){
                if (getRank() < 2 && getExp() == 100){
                    if (!player.isCreative())
                        stack.shrink(1);
                    setRank(getRank() + 1);
                    setExp(0);
                }
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
        if (hasWeapon() && !(this instanceof EntityMagicMaidRett)){
            if (weapon == null)
                weapon = EntityMaidWeapon.getWeaponFromUUID(world, getWeaponID());
            if (weapon == null || weapon.isDead){
                getEquipment(ItemEquipment.valueOf(EnumEquipment.valueOf(getWeaponType())));
            }
        }
    }


    public void onEntityUpdate()
    {
        if (this.getTrueHealth() > 0){
            if (this.isDead) this.isDead = false;
            if (this.deathTime > 0) this.deathTime = 0;
        }
        super.onEntityUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

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
    public void getEquipment(ItemEquipment equipment) {

    }

    @Override
    public void loseEquipment(ItemEquipment equipment) {

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
    public void setHealth(float health)
    {
        float curHealth = getHealth();
        float minus = curHealth - health;

        if (minus < 0)
            super.setHealth(health);
        else if (EnumMode.valueOf(getMode()) == EnumMode.BOSS && shouldAvoidSetHealth((int) minus)){
            World world = getEntityWorld();
            if (!world.isRemote) {
                EntityPlayer player = world.getClosestPlayerToEntity(this, 20);
                try {
                    player.sendMessage(new TextComponentString("检测到高额穿透伤害，尝试清除玩家物品"));
                } catch (Exception e){
                    e.printStackTrace();
                }
                if (player != null)
                    PunishOperationHandler.punishPlayer((EntityPlayerMP) player, 3, "");
            }
        }
        else
            super.setHealth(health);
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

    public static class EnemySelect implements Predicate<EntityLivingBase> {
        public final EntityMagicMaid maid;
        public EnemySelect(EntityMagicMaid maid){
            this.maid = maid;
        }
        @Override
        public boolean apply(@Nullable EntityLivingBase input) {
            return maid.isEnemy(input);
        }

        @Override
        public boolean equals(@Nullable Object object) {
            return object instanceof EnemySelect && maid.getUniqueID().equals(((EnemySelect) object).maid.getUniqueID());
        }
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
