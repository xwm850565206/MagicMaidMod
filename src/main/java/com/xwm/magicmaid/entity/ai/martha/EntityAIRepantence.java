package com.xwm.magicmaid.entity.ai.martha;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityBossCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.manager.IMagicFightManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.particle.SPacketParticle;
import com.xwm.magicmaid.network.particle.SPacketThreeParamParticle;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.registry.MagicRenderRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;
import java.util.Random;

public class EntityAIRepantence extends EntityAIBase
{
    private static final int PERFORMTIME = 40;
    private EntityMagicMaid maid;
    private EntityLivingBase owner;
    private int tick = 0;
    private int performTick = 0;
    private Random random = new Random();
    private int id = MagicRenderRegistry.allocateArea();

    public EntityAIRepantence(EntityMagicMaid maid){
        this.maid = maid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

        if (!maid.hasOwner() && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS) //如果没有主人又不是boss就不放技能
            return false;
        if (EnumEquipment.valueOf(maid.getWeaponType()) != EnumEquipment.REPATENCE)
            return false;
        if (EnumMode.valueOf(maid.getMode()) != EnumMode.FIGHT && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;

        return tick++ >= this.maid.getAttackColdTime(EnumAttackType.CONVICTION);
    }

    public boolean shouldContinueExecuting(){
        return this.performTick < PERFORMTIME;
    }

    public void startExecuting()
    {
        if (maid.hasOwner())
            this.owner = this.maid.getEntityWorld().getPlayerEntityByUUID(this.maid.getOwnerID());
        this.maid.setState(3);
        this.maid.setIsPerformAttack(true);
        this.tick = 0;
    }


    public void updateTask()
    {
        AxisAlignedBB area = MagicEquipmentUtils.getUsingArea(this.maid.getWeaponFromSlot(), this.maid, this.maid.getEntityBoundingBox());
        if (this.maid instanceof IEntityBossCreature)
            ((IEntityBossCreature) this.maid).createWarningArea(id, area);

        if (performTick++ < PERFORMTIME-1)
            return;

        World world = this.maid.getEntityWorld();
        AxisAlignedBB bb = this.maid.getEntityBoundingBox().grow(10, 1, 10);
        List<EntityLivingBase> entityLivingBases = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
        for (EntityLivingBase entityLivingBase : entityLivingBases)
        {
            try{
                if (!maid.isEnemy(entityLivingBase))
                    continue;
//                float health = entityLivingBase.getHealth();
                IMagicFightManagerImpl.getInstance().attackEntityFrom(entityLivingBase, new EntityDamageSource("repantence_attack", maid).setDamageBypassesArmor(),
                        maid.getAttackDamage(EnumAttackType.REPANTENCE));
//                if (health == entityLivingBase.getHealth() && health > 0){
//                    entityLivingBase.setHealth(0);
//                    if (entityLivingBase instanceof EntityPlayerMP) {
//                        entityLivingBase.sendMessage(new TextComponentString("攻击不生效，尝试直接斩杀(原因见说终焉记事)"));
//                    }
//                }
                playParticle(entityLivingBase.getEntityBoundingBox());

                if (maid.getRank() >= 2) //2阶造成伤害回血
                    maid.heal(maid.getAttackDamage(EnumAttackType.REPANTENCE));

                if (entityLivingBase.getHealth() <= 0){
                    playDeathParticle(entityLivingBase.getEntityBoundingBox());
                }
            } catch (Exception e){
                ; //有可能出现问题
            }
        }

        if (this.maid instanceof IEntityBossCreature)
            ((IEntityBossCreature) this.maid).removeWarningArea(id);
    }

    public void resetTask(){
        this.maid.setState(0);
        this.maid.setIsPerformAttack(false);
        this.performTick = 0;
    }

    private void playParticle(AxisAlignedBB bb){

        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.minY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        double radius = Math.max((bb.maxX - bb.minX), (bb.maxZ - bb.minZ)) / 2.0 + 0.25;
        double perAngle = 360 / 10.0;
        double perHeight = (bb.maxY - bb.minY) / 6.0;
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 10; j++)
            {
                SPacketParticle sPacketParticle = new SPacketParticle(
                        d0 + radius * Math.sin(Math.toRadians(j * perAngle)),
                        d1 + perHeight * i,
                        d2 + radius * Math.cos(Math.toRadians(j * perAngle)), EnumParticleTypes.SMOKE_LARGE);
                NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
                NetworkLoader.instance.sendToAllAround(sPacketParticle, target);
            }
    }

    private void playDeathParticle(AxisAlignedBB bb){
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = (bb.maxY);
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        for (int j = 0; j < 2; j++)
        {
            SPacketThreeParamParticle particlePacket = new SPacketThreeParamParticle(
                    d0 + random.nextDouble(),
                    d1 + random.nextDouble(),
                    d2 + random.nextDouble(), EnumCustomParticles.SOUL);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);
        }
    }
}
