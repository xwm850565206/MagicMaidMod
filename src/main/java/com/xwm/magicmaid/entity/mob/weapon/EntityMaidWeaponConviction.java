package com.xwm.magicmaid.entity.mob.weapon;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityMultiHealthCreature;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.manager.IMagicFightManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.entity.SPacketEntityData;
import com.xwm.magicmaid.network.particle.SPacketThreeParamParticle;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.List;

public class EntityMaidWeaponConviction extends EntityMaidWeapon
{
    private int tick = 0;
    private boolean gap = true;
    private double perAngle = 360 / 6;
    private double radius = 0.5;
    private double perHeight = height / 6;

    private int attackTick = 0;
    private int attackRadius = 4;
    private ItemStack itemConviction;

    public EntityMaidWeaponConviction(World worldIn) {
        super(worldIn);
        enumEquipment = EnumEquipment.CONVICTION;
    }

    public EntityMaidWeaponConviction(World world, ItemStack stack)
    {
        this(world);
        this.itemConviction = stack.copy();
        this.attackRadius = (int) MagicEquipmentUtils.getRadiusFromAxisAlignedBB(MagicEquipmentUtils.getUsingArea(stack, null, null));
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (world.isRemote) {
            gap = !gap;
            if (gap)
                return;
            this.tick++;
            if (tick > 12) tick = 0;

            int t = tick % 6;
            double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
            double d1 = this.getEntityBoundingBox().minY;
            double d2 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;

            ParticleSpawner.spawnParticle(EnumCustomParticles.CROSS, d0 + radius * Math.sin(Math.toRadians(t * perAngle)), d1 + perHeight * t, d2 + radius * Math.cos(Math.toRadians(t * perAngle)), 0, 0, 0);
        }
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        this.attackTick++;
        if (this.attackTick == 80)
            this.attackTick = 0;
    }

    @Override
    protected void doOhterOwnerUpdate()
    {
        super.doOhterOwnerUpdate();

        if (otherOwner == null)
            return;
        if (world.isRemote)
            return;

        if (attackTick == 40)
        {
            AxisAlignedBB bb = MagicEquipmentUtils.getUsingArea(this.itemConviction, this, this.getEntityBoundingBox());

            playParticle(bb);

            int damage = MagicEquipmentUtils.getAttackDamage(itemConviction, ((ItemWeapon)itemConviction.getItem()).getAttackType());
            List<EntityLivingBase> entityLivings = this.world.getEntitiesWithinAABB(EntityLivingBase.class, bb);

            for (EntityLivingBase entityLivingBase : entityLivings)
            {

                try {
                    float health = entityLivingBase.getHealth();
                    if (entityLivingBase instanceof IEntityMultiHealthCreature)
                        health = ((IEntityMultiHealthCreature) entityLivingBase).getTrueHealth();

                    if (health <= damage) {
                        if (entityLivingBase == otherOwner || entityLivingBase == this || !MagicEquipmentUtils.checkEnemy(otherOwner, entityLivingBase)) {
                            ;
                        }
                        else if (entityLivingBase instanceof EntityPlayer) {
                            InventoryHelper.dropInventoryItems(entityLivingBase.getEntityWorld(), entityLivingBase.getPosition(), ((EntityPlayer) entityLivingBase).inventory);
                            entityLivingBase.setDead();
                        }
                        else if (entityLivingBase instanceof EntityLiving){
                            if (((EntityLiving) entityLivingBase).getAttackTarget() == this.otherOwner || entityLivingBase instanceof EntityMob) {
                                removeTasks((EntityLiving) entityLivingBase);
                                IMagicFightManagerImpl.getInstance().setHealth(entityLivingBase, 1);
                                //有罪
                                NBTTagCompound entityData = entityLivingBase.getEntityData();
                                entityData.setBoolean(Reference.EFFECT_CONVICTION, true);
                                SPacketEntityData packet = new SPacketEntityData(entityLivingBase.getEntityId(),
                                        entityLivingBase.getEntityWorld().provider.getDimension(),
                                        0,
                                        "true",
                                        Reference.EFFECT_CONVICTION);
                                NetworkLoader.instance.sendToDimension(packet, this.world.provider.getDimension());
                            }
                            else if ((entityLivingBase instanceof EntityVillager) && ((EntityVillager) entityLivingBase).getWorld().provider.getDimension() == DimensionInit.DIMENSION_CHURCH) { // 教堂维度的村民也有罪
                                NBTTagCompound entityData = entityLivingBase.getEntityData();
                                entityData.setBoolean(Reference.EFFECT_CONVICTION, true);
                                SPacketEntityData packet = new SPacketEntityData(entityLivingBase.getEntityId(),
                                        entityLivingBase.getEntityWorld().provider.getDimension(),
                                        0,
                                        "true",
                                        Reference.EFFECT_CONVICTION);
                                NetworkLoader.instance.sendToDimension(packet, this.world.provider.getDimension());
                            }
                            else {
                                NBTTagCompound entityData = entityLivingBase.getEntityData();
                                entityData.setBoolean(Reference.EFFECT_CONVICTION, false);
                                SPacketEntityData packet = new SPacketEntityData(entityLivingBase.getEntityId(),
                                        entityLivingBase.getEntityWorld().provider.getDimension(),
                                        0,
                                        "false",
                                        Reference.EFFECT_CONVICTION);
                                NetworkLoader.instance.sendToDimension(packet, this.world.provider.getDimension());
                            }
                        }
                        else { // 无罪
                            NBTTagCompound entityData = entityLivingBase.getEntityData();
                            entityData.setBoolean(Reference.EFFECT_CONVICTION, false);
                            SPacketEntityData packet = new SPacketEntityData(entityLivingBase.getEntityId(),
                                    entityLivingBase.getEntityWorld().provider.getDimension(),
                                    0,
                                    "false",
                                    Reference.EFFECT_CONVICTION);
                            NetworkLoader.instance.sendToDimension(packet, this.world.provider.getDimension());
                        }
                    }
                } catch (Exception e) {
                    ;
                }
            }
        }
        else if (attackTick == 50)
            this.moveToBlockPosAndAngles(otherOwner.getPosition().add(0, otherOwner.height / 2.0, 0), rotationYaw, rotationPitch);
        else if (attackTick == 80) {
            EntityItem item = new EntityItem(world, posX, posY, posZ, this.itemConviction);
            world.spawnEntity(item);
            this.setDead();
        }
        else if (attackTick > 50 && this.getDistance(otherOwner) < 4){
            if (otherOwner.getHeldItemMainhand().isEmpty()) {
                otherOwner.setHeldItem(EnumHand.MAIN_HAND, itemConviction);
                this.setDead();
            }
        }
    }

    private void playParticle(AxisAlignedBB bb) {
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.minY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        double perAngle = 360 / 10.0;
        double perHeight = (bb.maxY - bb.minY) / 3.0;
        double perRadius = bb.getAverageEdgeLength() / 10.0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 10; j++)
            {
                for (int k = 0; k < 10; k++) {
                    SPacketThreeParamParticle particlePacket = new SPacketThreeParamParticle(
                            d0 + perRadius * k * Math.sin(Math.toRadians(j * perAngle)),
                            d1 + perHeight * i,
                            d2 + perRadius * k * Math.cos(Math.toRadians(j * perAngle)), EnumCustomParticles.CROSS);
                    NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(this.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
                    NetworkLoader.instance.sendToAllAround(particlePacket, target);
                }
            }
    }

    private void removeTasks(EntityLiving entityLiving)
    {
        List<EntityAITasks.EntityAITaskEntry> taskEntryList = new ArrayList<EntityAITasks.EntityAITaskEntry>(entityLiving.targetTasks.taskEntries);
        for (EntityAITasks.EntityAITaskEntry ai : taskEntryList)
        {
            entityLiving.targetTasks.removeTask(ai.action);
        }

        taskEntryList = new ArrayList<EntityAITasks.EntityAITaskEntry>(entityLiving.tasks.taskEntries);
        for (EntityAITasks.EntityAITaskEntry ai : taskEntryList)
        {
            entityLiving.tasks.removeTask(ai.action);
        }
    }
}
