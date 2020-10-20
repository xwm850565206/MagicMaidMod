package com.xwm.magicmaid.entity.mob.weapon;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.network.CustomerParticlePacket;
import com.xwm.magicmaid.network.DistinationParticlePacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.UpdateEntityPacket;
import com.xwm.magicmaid.object.item.equipment.PlayerEquipmentUtils;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class EntityMaidWeaponPandorasBox extends EntityMaidWeapon
{
    private static final DataParameter<Boolean> ISOPEN = EntityDataManager.<Boolean>createKey(EntityMaidWeaponPandorasBox.class, DataSerializers.BOOLEAN);
    private int radius = 4;

    public int tick = 0; //client side use to control animation

    public EntityMaidWeaponPandorasBox(World worldIn) {
        super(worldIn);
        enumEquipment = EnumEquipment.PANDORA;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(ISOPEN, false);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        this.tick++;
        if (this.tick == 360)
            this.tick = 0;
    }

    protected void doMaidOwnerUpdate()
    {
        super.doMaidOwnerUpdate();

        if (this.maid != null){
            if (isOpen()){
                List<EntityLivingBase> entityLivingBases = world.getEntitiesWithinAABB(EntityLivingBase.class,
                        this.getEntityBoundingBox().grow(radius + maid.getRank() * 2, radius, radius + maid.getRank() * 2));
                try{
                    for (EntityLivingBase entityLivingBase : entityLivingBases)
                    {
                        if (!this.maid.isEnemy(entityLivingBase))
                            continue;
                        float health = entityLivingBase.getHealth();
                        entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this.maid),
                                this.maid.getAttackDamage(EnumAttackType.PANDORA));
                        if (health == entityLivingBase.getHealth() && health > 0){
                            entityLivingBase.setHealth(0);
                            if (entityLivingBase instanceof EntityPlayerMP) {
                                entityLivingBase.sendMessage(new TextComponentString("攻击不生效，尝试直接斩杀(原因见说终焉记事)"));
                            }
                        }
                        this.maid.heal(this.maid.getAttackDamage(EnumAttackType.PANDORA)); //吸血给自己
                        playParticle(entityLivingBase);
                    }
                }catch (Exception e){
                    ; //有的生物受到攻击会出错
                }
            }
        }
    }

    protected void doOhterOwnerUpdate() {

        super.doOhterOwnerUpdate();

        if (world.isRemote)
            return;

        if (otherOwner == null)
            return;
        if (tick == 20)
            this.setOpen(true);
        else if (tick == 100)
            this.moveToBlockPosAndAngles(otherOwner.getPosition().add(0, otherOwner.height / 2.0, 0), rotationYaw, rotationPitch);
        else if (tick == 120) {
            EntityItem item = new EntityItem(world, posX, posY, posZ, new ItemStack(ItemInit.itemPandorasBox));
            world.spawnEntity(item);
            this.setDead();
        }
        else if (tick > 100 && this.getDistance(otherOwner) < 1){
            if (otherOwner.getHeldItemMainhand().isEmpty()) {
                otherOwner.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemInit.itemPandorasBox));
                this.setDead();
            }
        }

        if (isOpen()) {
            List<EntityLivingBase> entityLivingBases = world.getEntitiesWithinAABB(EntityLivingBase.class,
                    this.getEntityBoundingBox().grow(radius + 2, radius, radius + 2));
            try {
                for (EntityLivingBase entityLivingBase : entityLivingBases) {
                    if (!PlayerEquipmentUtils.checkEnemy(otherOwner, entityLivingBase))
                        continue;
                    int damage = PlayerEquipmentUtils.getAttackDamage(otherOwner, EnumAttackType.PANDORA);
                    entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(otherOwner),
                            damage);

                    otherOwner.heal(damage); //吸血给自己
                    playParticle(entityLivingBase);
                }
            } catch (Exception e) {
                ; //有的生物受到攻击会出错
            }
        }
    }

    private void playParticle(EntityLivingBase entityLivingBase)
    {
        AxisAlignedBB abb = entityLivingBase.getEntityBoundingBox();
        AxisAlignedBB cbb = this.getEntityBoundingBox();
        double d0 = (abb.minX + abb.maxX) / 2.0;
        double d1 = abb.maxY;
        double d2 = (abb.minZ + abb.maxZ) / 2.0;
        double d3 = (cbb.minX + cbb.maxX) / 2.0;
        double d4 = (cbb.minY + cbb.maxY) / 2.0;
        double d5 = (cbb.minZ + cbb.maxZ) / 2.0;
        for (int i = 0; i < 4; i++) {
            DistinationParticlePacket particlePacket = new DistinationParticlePacket(
                            d0 + rand.nextDouble(),
                            d1 + rand.nextDouble(),
                            d2 + rand.nextDouble(), d3, d4, d5, EnumCustomParticles.PANDORA);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(this.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);
        }
    }

    public void setOpen(boolean isOpen)
    {
        this.dataManager.set(ISOPEN, isOpen);
    }

    public boolean isOpen(){
        return this.dataManager.get(ISOPEN);
    }

}
