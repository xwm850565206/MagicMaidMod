package com.xwm.magicmaid.entity.ai.selina;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelina;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

import java.util.Random;

import static com.xwm.magicmaid.registry.MagicEquipmentRegistry.PANDORA;

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

        if (!maid.hasOwner() && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;
        if (MagicEquipmentRegistry.getAttribute(maid.getWeaponType()) != PANDORA)
            return false;
        if (EnumMode.valueOf(maid.getMode()) != EnumMode.FIGHT && EnumMode.valueOf(maid.getMode()) != EnumMode.BOSS)
            return false;

        return tick++ >= maid.getAttackColdTime(PANDORA);
    }

    public boolean shouldContinueExecuting(){
        return this.performTick++ < PERFORMTIME;
    }

    public void startExecuting()
    {
        world = this.maid.world;
        this.maid.setState(EnumSelineState.toInt(EnumSelineState.CHANT));
        this.maid.setIsPerformAttack(true);

        if (pandorasBox == null)
            pandorasBox = (EntityMaidWeaponPandorasBox) EntityMaidWeapon.getWeaponFromUUID(world, this.maid.getWeaponID());
        if (pandorasBox != null)
            pandorasBox.setOpen(true);
        if (maid.getRank() >= 1){
//            spawnBats();
        }
        this.performTick = 0;
    }

    public void resetTask(){
        this.maid.setState(EnumSelineState.toInt(EnumSelineState.STANDARD));
        this.maid.setIsPerformAttack(false);
        if (pandorasBox != null)
            pandorasBox.setOpen(false);
        this.tick = 0;
    }

    private void spawnBats(){
       //todo
    }
}
