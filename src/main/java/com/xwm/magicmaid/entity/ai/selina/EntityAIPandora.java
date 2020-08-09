package com.xwm.magicmaid.entity.ai.selina;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelina;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

import java.util.Random;

//todo
public class EntityAIPandora extends EntityAIBase
{
    private static final int COLDTIME = 60;
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
        if (EnumMode.valueOf(maid.getMode()) != EnumMode.FIGHT)
            return false;
        System.out.println("tick: " + tick);
        return tick++ >= COLDTIME;
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
        this.performTick = 0;
    }

    public void resetTask(){
        this.maid.setState(EnumSelineState.toInt(EnumSelineState.STANDARD));
        if (pandorasBox != null)
            pandorasBox.setOpen(false);
        this.tick = 0;
    }
}
