package com.xwm.magicmaid.entity.ai.strawberry;

import com.xwm.magicmaid.entity.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.maid.EnumAttackTypes;
import com.xwm.magicmaid.entity.maid.EnumModes;
import com.xwm.magicmaid.event.NetworkLoader;
import com.xwm.magicmaid.util.ParticlePacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class EntityAIConviction extends EntityAIBase
{
    private static final int COLDTIME = 100;
    private static final int PERFORMTIME = 20;
    private EntityMagicMaid maid;
    private EntityLivingBase owner;
    private int tick = 0;
    private int performTick = 0;

    public EntityAIConviction(EntityMagicMaid maid){
        this.maid = maid;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {

        System.out.println("ower: " + maid.hasOwner() + " weaponType: "
                + maid.getWeaponType() + " mode: " + EnumModes.valueOf(maid.getMode()));

        if (!maid.hasOwner())
            return false;
//        if (EnumWeapons.valueOf(maid.getWeaponType()) != EnumWeapons.REPATENCE)
//            return false;
        if (EnumModes.valueOf(maid.getMode()) != EnumModes.FIGHT)
            return false;
        System.out.println("tick: " + tick);
        return tick++ >= COLDTIME;
    }

    public boolean shouldContinueExecuting(){
        return this.performTick < PERFORMTIME;
    }

    public void startExecuting()
    {
        System.out.println("start");
        this.owner = this.maid.getEntityWorld().getPlayerEntityByUUID(this.maid.getOwnerID());
        this.maid.setState(3);
        this.tick = 0;
    }


    public void updateTask()
    {
        if (performTick++ < PERFORMTIME-1)
            return;

    }

    public void resetTask(){
        this.maid.setState(0);
        this.performTick = 0;
    }

    private void playParticle(AxisAlignedBB bb) {

    }

}
