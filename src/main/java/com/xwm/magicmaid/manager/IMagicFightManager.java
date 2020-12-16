package com.xwm.magicmaid.manager;

import com.xwm.magicmaid.entity.mob.basic.AbstractEntityMagicCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAvoidThingCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

public interface IMagicFightManager
{
    /**
     * 让avoid生物死亡 avoid 通常是avoid setHealth 和 attackedEntityFrom
     * @param creature
     */
    void setDead(IEntityAvoidThingCreature creature);

    /**
     * 让magicCreature死亡
     * @param creature
     */
    void setDead(AbstractEntityMagicCreature creature);


    /**
     * 给avoid生物上锁(防止伤害)
     * @param creature avoid 生物
     * @param amount 伤害上限
     */
    void lock(IEntityAvoidThingCreature creature, int amount);

    /**
     * 给avoid生物解锁(一般用于让本模组造成伤害)
     * @param creature avoid生物
     * @return 解锁前的伤害上限
     */
    int unlock(IEntityAvoidThingCreature creature);


    /**
     * 计算伤害值
     * @param attacker 攻击者
     * @param target 受击者
     * @param stack 使用的物品
     * @param amount 施加的伤害值
     * @return
     */
    float caculateDamageAmount(@Nullable EntityLivingBase attacker, EntityLivingBase target, @Nullable ItemStack stack, float amount);

    /**
     * 对 magic creature capability 固有属性进行增减(提高减伤率 降低减伤率 等等)
     * @param creature 被影响的生物
     * @param attribute 被增减的属性
     * @param modifier 增减的信息
     * @param overwrite 是否覆盖之前的modifier
     */
    void applyAttribute(AbstractEntityMagicCreature creature, IAttribute attribute, AttributeModifier modifier, boolean overwrite);

    /**
     * 移除 magic creature capability 固有属性的modifier
     * @param creature 被移除的生物
     * @param attribute 被移除的modifier所在的属性
     * @param modifier 移除的modifier，传入null时表示移除所有的modifier
     */
    void removeAttribute(AbstractEntityMagicCreature creature, IAttribute attribute, @Nullable AttributeModifier modifier);


    /**
     * 对entityLivingBase造成伤害
     * @param entityLivingBase 被造成伤害的生物
     * @param source 伤害源
     * @param amount 伤害值
     * @return
     */
    boolean attackEntityFrom(EntityLivingBase entityLivingBase, DamageSource source, float amount);


    /**
     * 设置生物的血量值
     * @param entityLivingBase 生物
     * @param amount 血量值
     */
    void setHealth(EntityLivingBase entityLivingBase, float amount);
}
