package com.xwm.magicmaid.entity.ai.rett;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRett;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import org.lwjgl.Sys;

import java.util.Random;

public class EntityAIDemonKillerAttack extends EntityAIBase
{
    private static final int COLDTIME = 100;
    private static final int PERFORMTIME = 45;
    private EntityMagicMaidRett maid;
    private int tick = 0;
    private int performTick = 0;
    private Random random = new Random();

    public EntityAIDemonKillerAttack(EntityMagicMaidRett maid)
    {
        this.maid = maid;
    }

    public  boolean shouldExecute(){
        this.maid.debug();
        System.out.println("tick : " + tick);
//        if (!maid.hasOwner() && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS) //如果没有主人又不是boss就不放技能
//            return false;
//        if (EnumEquipment.valueOf(maid.getWeaponType()) != EnumEquipment.DEMONKILLINGSWORD)
//            return false;
//        if (EnumMode.valueOf(maid.getMode()) != EnumMode.FIGHT)
//            return false;
//        if (maid.getAttackTarget() == null)
//            return false;
        return tick++ >= COLDTIME;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting(){
        return this.performTick < PERFORMTIME;
    }

    public void startExecuting()
    {
        System.out.println("start");
        this.tick = 0;
    }

    public void updateTask()
    {
        this.maid.setPerformtick(performTick);

        if (performTick == 5){
            System.out.println("attack1");
//            this.maid.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_ATTACK1));
        }
        else if (performTick == 10) {
            System.out.println("attack2");
//            this.maid.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_ATTACK2));
        }
        else if (performTick == 20) {
            System.out.println("attack3");
//            this.maid.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_ATTACK3));
        }
        else if (performTick == 30) {
            System.out.println("attack4");
//            this.maid.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_ATTACK4));
        }


//        maid
        performTick++;
    }

    public void resetTask(){
        this.maid.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_STANDARD));
        this.performTick = 0;
        this.maid.setPerformtick(performTick);
    }
}
