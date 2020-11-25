package com.xwm.magicmaid.entity.ai.martha;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityBossCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.init.PotionInit;
import com.xwm.magicmaid.network.CustomerParticlePacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.registry.MagicRenderRegistry;
import com.xwm.magicmaid.util.helper.MagicEquipmentUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class EntityAIConviction extends EntityAIBase
{
    private static final int PERFORMTIME = 40;
    private EntityMagicMaid maid;
    private EntityLivingBase owner;
    private int tick = 0;
    private int performTick = 0;
    private double radius = 4;
    private Random random = new Random();
    private int id = MagicRenderRegistry.allocateArea();

    public EntityAIConviction(EntityMagicMaid maid){
        this.maid = maid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

        if (!maid.hasOwner() && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;
        if (EnumEquipment.valueOf(maid.getWeaponType()) != EnumEquipment.CONVICTION)
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
        this.radius = 4 + 3 * maid.getRank();
        this.tick = 0;
    }


    public void updateTask()
    {
        AxisAlignedBB area = MagicEquipmentUtils.getUsingArea(this.maid.getWeaponFromSlot(), this.maid, this.maid.getEntityBoundingBox());
        if (this.maid instanceof IEntityBossCreature)
            ((IEntityBossCreature) this.maid).createWarningArea(id, area);

        if (performTick++ < PERFORMTIME-1)
            return;

        playParticle(this.maid.getEntityBoundingBox());
        List<EntityLivingBase> entityLivings = this.maid.world.getEntitiesWithinAABB(EntityLivingBase.class,
                area);

        for (EntityLivingBase entityLivingBase : entityLivings)
        {
            try {
                if (!maid.isEnemy(entityLivingBase))
                    continue;

                if (entityLivingBase.isPotionActive(PotionInit.PROTECT_BLESS_EFFECT)) {
                    entityLivingBase.heal(entityLivingBase.getMaxHealth());
                    continue; //有守护者祝福不会被定罪攻击,而是恢复至满血
                }

                if (maid.getRank() >= 1)
                    entityLivingBase.setHealth(1);
                if (EnumMode.valueOf(maid.getMode()) == EnumMode.BOSS ) {
                    if (entityLivingBase instanceof EntityPlayer){
                        entityLivingBase.sendMessage(new TextComponentString("定罪之力，定夺世间一切罪恶"));
                        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(this.maid, "clear " + entityLivingBase.getName());
                        MagicEquipmentUtils.setHealth(entityLivingBase, 0);
                    }
                    entityLivingBase.setDead();
                }

                if (entityLivingBase instanceof  EntityLiving) //失去所有任务
                    removeTasks((EntityLiving) entityLivingBase);

                playTipParticle(entityLivingBase.getEntityBoundingBox());

            } catch (Exception e){
                ;
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

    private void playTipParticle(AxisAlignedBB bb)
    {
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.maxY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        for (int i = 0; i < 4; i++) {
            CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                    d0 + random.nextDouble(),
                    d1 + random.nextDouble(),
                    d2 + random.nextDouble(), EnumCustomParticles.CONVICTION);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);
        }
    }

    private void playParticle(AxisAlignedBB bb) {
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.minY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        double perAngle = 360 / 10.0;
        double perHeight = (bb.maxY - bb.minY) / 6.0;
        double perRadius = radius / 10.0;
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 10; j++)
            {
                for (int k = 0; k < 20; k++) {
                    CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                            d0 + perRadius * k * Math.sin(Math.toRadians(j * perAngle)),
                            d1 + perHeight * i,
                            d2 + perRadius * k * Math.cos(Math.toRadians(j * perAngle)), EnumCustomParticles.CROSS);
                    NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
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
