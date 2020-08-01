package com.xwm.magicmaid.entity.ai;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIWeaponKeepPos extends EntityAIBase
{
    private EntityMaidWeapon weapon;

    public EntityAIWeaponKeepPos(EntityMaidWeapon weapon)
    {
        this.weapon = weapon;
    }
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        System.out.println("judge");
        return weapon != null && weapon.maid != null;
    }

    @Override
    public void updateTask()
    {
        BlockPos standPos = weapon.maid.getPosition();
        standPos = standPos.add(weapon.maid.weaponStandbyPos.getX(), weapon.maid.weaponStandbyPos.getY(), weapon.maid.weaponStandbyPos.getZ());
        weapon.setPosition(standPos.getX(), standPos.getY(), standPos.getZ());
        System.out.println(weapon.getPosition().toString());
    }
}
