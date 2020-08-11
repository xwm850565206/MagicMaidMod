package com.xwm.magicmaid.entity.ai.selina;

import com.xwm.magicmaid.entity.ai.EntityAIAttackMeleeBat;
import com.xwm.magicmaid.entity.ai.EntityAINearestAttackableTargetAvoidOwner;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelina;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

//todo
public class EntityAIPandora extends EntityAIBase
{
    private static final int PERFORMTIME = 100;

    private EntityMagicMaidSelina maid;
    private int tick = 0;
    private int performTick = 0;
    private World world;
    private Random random = new Random();
    private EntityMaidWeaponPandorasBox pandorasBox;

    public EntityAIPandora(EntityMagicMaidSelina maid)
    {
        this.maid = maid;
        pandorasBox = (EntityMaidWeaponPandorasBox) EntityMaidWeapon.getWeaponFromUUID(world, this.maid.getWeaponID());
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

//        maid.debug();

        if (!maid.hasOwner() && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;
        if (EnumEquipment.valueOf(maid.getWeaponType()) != EnumEquipment.PANDORA)
            return false;
        if (EnumMode.valueOf(maid.getMode()) != EnumMode.FIGHT && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;
        System.out.println("tick: " + tick);
        return tick++ >= maid.getAttackColdTime(EnumAttackType.PANDORA);
    }

    public boolean shouldContinueExecuting(){
        return this.performTick++ < PERFORMTIME;
    }

    public void startExecuting()
    {
        world = this.maid.world;
        this.maid.setState(EnumSelineState.toInt(EnumSelineState.CHANT));

        if (pandorasBox == null)
            pandorasBox = (EntityMaidWeaponPandorasBox) EntityMaidWeapon.getWeaponFromUUID(world, this.maid.getWeaponID());
        if (pandorasBox != null)
            pandorasBox.setOpen(true);
        if (maid.getRank() >= 1){
            spawnBats(); //todo
        }
        this.performTick = 0;
    }

    public void resetTask(){
        this.maid.setState(EnumSelineState.toInt(EnumSelineState.STANDARD));
        if (pandorasBox != null)
            pandorasBox.setOpen(false);
        this.tick = 0;
    }

    private void spawnBats(){
        for (int i = 0; i < 5 + random.nextInt(5); i ++)
        {
            EntityBat bat = new EntityBat(world);
            try {//没有这个属性的生物就赋予这个属性 有的会报错，就catch住
                bat.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
                bat.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5);
                bat.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(5);
                bat.targetTasks.addTask(1, new EntityAINearestAttackableTargetAvoidOwner(bat, maid, EntityLivingBase.class, true));
                bat.tasks.addTask(2, new EntityAIAttackMeleeBat(bat, 1.2, false));
                AxisAlignedBB bb = pandorasBox.getEntityBoundingBox();
                double d0 = (bb.minX + bb.maxX) / 2.0;
                double d1 = (bb.minY + bb.maxY) / 2.0;
                double d2 = (bb.minZ + bb.maxZ) / 2.0;
                bat.setPosition(d0, d1, d2);
                world.spawnEntity(bat);
            } catch (IllegalArgumentException e){
                ;
            }

        }
    }
}
