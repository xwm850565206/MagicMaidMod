package com.xwm.magicmaid.entity.mob.basic.interfaces;

import com.xwm.magicmaid.object.item.equipment.EquipmentAttribute;
import net.minecraft.entity.EntityLivingBase;

/**
 * 生物可以进行攻击的接口
 */
public interface IEntityAttackableCreature
{
    int getAttackDamage(EquipmentAttribute type);

    int getAttackColdTime(EquipmentAttribute type);

    boolean isEnemy(EntityLivingBase entityLivingBase);

    void setIsPerformAttack(boolean isPerformAttack);

    boolean isPerformAttack();
}
