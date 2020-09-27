package com.xwm.magicmaid.entity.mob.basic.interfaces;

import com.xwm.magicmaid.enumstorage.EnumAttackType;
import net.minecraft.entity.EntityLivingBase;

/**
 * 生物可以进行攻击的接口
 */
public interface IEntityAttackableCreature
{
    int getAttackDamage(EnumAttackType type);

    int getAttackColdTime(EnumAttackType type);

    boolean isEnemy(EntityLivingBase entityLivingBase);

    void setIsPerformAttack(boolean isPerformAttack);

    boolean isPerformAttack();
}
