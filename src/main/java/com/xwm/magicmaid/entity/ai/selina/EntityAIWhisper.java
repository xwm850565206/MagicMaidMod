package com.xwm.magicmaid.entity.ai.selina;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityBossCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelina;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import com.xwm.magicmaid.network.CustomerParticlePacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.registry.CustomRenderRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;
import java.util.Random;

public class EntityAIWhisper extends EntityAIBase
{
    private static final int PERFORMTIME = 60;
    private static final int perAngle = 10;
    private static final int[] hexameron = new int[]{0, 60, 120, 180, 240, 300};

    private EntityMagicMaidSelina maid;
    private int tick = 0;
    private int performTick = 0;
    private World world;
    private Random random = new Random();
    private EntityMaidWeaponWhisper whisper;
    private EntityLivingBase target;
    private AxisAlignedBB cbb;
    private BlockPos cpos;
    private float radius = 4.0f;
    private List<EntityLivingBase> entityLivingBaseList;
    private int id = CustomRenderRegistry.allocateArea();


    public EntityAIWhisper(EntityMagicMaidSelina maid)
    {
        this.maid = maid;
        whisper = (EntityMaidWeaponWhisper) EntityMaidWeapon.getWeaponFromUUID(world, this.maid.getWeaponID());
    }
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

        if (!maid.hasOwner() && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;
        if (EnumEquipment.valueOf(maid.getWeaponType()) != EnumEquipment.WHISPER)
            return false;
        if (EnumMode.valueOf(maid.getMode()) != EnumMode.FIGHT && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;
        tick++;
        if (this.maid.getAttackTarget() == null)
            return false;
        return tick >= maid.getAttackColdTime(EnumAttackType.WHISPER);
    }

    public boolean shouldContinueExecuting(){
        return this.performTick++ < PERFORMTIME;
    }

    public void startExecuting(){
        world = this.maid.world;
        this.maid.setState(EnumSelineState.toInt(EnumSelineState.STIMULATE));
        this.maid.setIsPerformAttack(true);
        if (whisper == null)
            whisper = (EntityMaidWeaponWhisper) EntityMaidWeapon.getWeaponFromUUID(world, this.maid.getWeaponID());
        if (whisper != null)
            whisper.setAttack(true);
        this.performTick = 0;
        this.target = this.maid.getAttackTarget();
        if (this.target != null) {
            this.cbb = this.target.getEntityBoundingBox();
            this.cpos = this.target.getPosition();
        }
        this.radius = 4 + maid.getRank() * 4;
    }

    public void updateTask()
    {
        if (this.cbb == null)
            return;

        AxisAlignedBB area = cbb.grow(radius, 2, radius);
        if (this.maid instanceof IEntityBossCreature)
            ((IEntityBossCreature) this.maid).createWarningArea(id, area);

        if (target != null)
            this.maid.getLookHelper().setLookPositionWithEntity(target, 90, 90);

        //画圆
        float d0 = (float) ((cbb.minX + cbb.maxX) / 2.0);
        float d1 = (float) cbb.minY;
        float d2 = (float) ((cbb.minZ + cbb.maxZ) / 2.0);
        for (int i = 0; i < 36; i++){
            CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                    d0 + radius * Math.sin(Math.toRadians(i * perAngle)),
                    d1,
                    d2 + radius * Math.cos(Math.toRadians(i * perAngle)), EnumCustomParticles.WHISPER);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);
        }
        //六芒星
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                int k0 = i + j*2 % 6;
                int k1 = i + (j+1)*2 % 6;
                float t0 = (float) (d0 + radius * Math.sin(Math.toRadians(hexameron[k0])));
                float t1 = d1;
                float t2 = (float) (d2 + radius * Math.cos(Math.toRadians(hexameron[k0])));

                float t3 = (float) (d0 + radius * Math.sin(Math.toRadians(hexameron[k1])));
                float t4 = d1;
                float t5 = (float) (d2 + radius * Math.cos(Math.toRadians(hexameron[k1])));

                playLinearParticle(t0, t1, t2, t3, t4, t5);
            }
        }

        if (performTick == PERFORMTIME - 5) {
            //attack
            entityLivingBaseList = world.getEntitiesWithinAABB(EntityLivingBase.class, cbb.grow(radius, 2, radius));
        }

        if (performTick >= PERFORMTIME - 3) {

            for (EntityLivingBase entityLivingBase : entityLivingBaseList)
            {
                try{
                    if (!this.maid.isEnemy(entityLivingBase))
                        continue;
                    entityLivingBase.attackEntityFrom(DamageSource.LIGHTNING_BOLT, this.maid.getAttackDamage(EnumAttackType.WHISPER));
                    entityLivingBase.setHealth(entityLivingBase.getHealth() - 10 * maid.getRank());
                    world.playEvent(3000, entityLivingBase.getPosition(), 10);
                    EntityLightningBolt bolt = new EntityLightningBolt(world, entityLivingBase.posX + random.nextInt(2* (int) radius) - radius, cpos.getY(), entityLivingBase.posZ + random.nextInt(2 * (int) radius) - radius, true);
                    world.addWeatherEffect(bolt);
                    world.spawnEntity(bolt);

                }catch (Exception e){
                    ;
                }
            }

        }


    }

    public void resetTask(){
        this.maid.setState(EnumSelineState.toInt(EnumSelineState.STANDARD));
        this.maid.setIsPerformAttack(false);
        if (whisper != null)
            whisper.setAttack(false);
        this.tick = 0;

        if (this.maid instanceof IEntityBossCreature)
            ((IEntityBossCreature) this.maid).removeWarningArea(id);
    }

    private void playLinearParticle(float t0, float t1, float t2, float t3, float t4, float t5)
    {
        float gap = 10;
        float d0 = (t3 - t0) / gap;
        float d1 = (t1 + t4) / 2.0f;
        float d2 = (t5 - t2) / gap;

        for (int i = 0; i <= 10; i++)
        {
            CustomerParticlePacket particlePacket =
                    new CustomerParticlePacket(t0 + d0 * i, d1, t2 + d2 * i, EnumCustomParticles.WHISPER);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(maid.getEntityWorld().provider.getDimension(), t0 + d0 * i, d1, t2 + d2 * i, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);

        }
    }
}
