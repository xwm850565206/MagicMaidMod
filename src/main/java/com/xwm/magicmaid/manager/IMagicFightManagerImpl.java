package com.xwm.magicmaid.manager;

import com.xwm.magicmaid.entity.mob.basic.AbstractEntityMagicCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAvoidThingCreature;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

public class IMagicFightManagerImpl implements IMagicFightManager {

    private static IMagicFightManager instance = null;

    @Override
    public void setDead(IEntityAvoidThingCreature creature) {
        unlock(creature);
        if (creature instanceof AbstractEntityMagicCreature)
            setDead((AbstractEntityMagicCreature)creature);
        else if (creature instanceof Entity) {
            ((Entity) creature).setDead();
        }
    }

    @Override
    public void setDead(AbstractEntityMagicCreature creature) {
        creature.setHealth(0);
        creature.setDead();
    }

    @Override
    public void lock(IEntityAvoidThingCreature creature, int amount) {
        creature.setAvoidSetHealth(amount);
        creature.setAvoidDamage(amount);
    }

    @Override
    public int unlock(IEntityAvoidThingCreature creature) {
        int lock = creature.getAvoidSetHealth();
        creature.setAvoidSetHealth(-1);
        creature.setAvoidDamage(-1);
        return lock;
    }

    /**
     * 计算伤害值
     * @param attacker 攻击者
     * @param target 受击者
     * @param stack 使用的物品
     * @param amount 施加的伤害值
     * @return
     */
    @Override
    public float caculateDamageAmount(@Nullable EntityLivingBase attacker, EntityLivingBase target, @Nullable ItemStack stack, float amount) {
        double reduction = 0;
        double ignore = 0;

        if (attacker != null && attacker.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null)) {
            ICreatureCapability capability = attacker.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            ignore += capability == null ? 0 : capability.getIgnoreReduction();
        }

        if (stack != null && stack.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null)) {
            ICreatureCapability capability = stack.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            ignore += capability == null ? 0 : capability.getIgnoreReduction();
        }

        if (target.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null)) {
            ICreatureCapability capability = target.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            reduction = capability == null ? 0 : capability.getInjuryReduction();
        }

        amount = (float)Math.max (amount * (1 - (reduction - ignore)), 0);
        return amount;
    }

    @Override
    public void applyAttribute(AbstractEntityMagicCreature creature, IAttribute attribute, AttributeModifier modifier, boolean overwrite) {
        try {
            if (!creature.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
                return;

            ICreatureCapability capability = creature.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (capability == null)
                return;

            if (overwrite) { // 如果覆盖 就把之前的全部移除
                capability.getAttributeMap().getAttributeInstance(attribute).removeAllModifiers();
            }

            capability.getAttributeMap().getAttributeInstance(attribute).applyModifier(modifier);

        } catch (IllegalArgumentException e) {
            ; // 已经添加了
//        } catch (NullPointerException e) {
//            creature.getAttributeMap().registerAttribute(attribute).applyModifier(modifier);// 可能没有这个属性
//        } catch (Exception e) {
//            ;
        }
    }

    @Override
    public void removeAttribute(AbstractEntityMagicCreature creature, IAttribute attribute, @Nullable AttributeModifier modifier) {

        try {
            if (!creature.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
                return;

            ICreatureCapability capability = creature.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (capability == null)
                return;

            if (modifier == null)
                capability.getAttributeMap().getAttributeInstance(attribute).removeAllModifiers();
            else {
                capability.getAttributeMap().getAttributeInstance(attribute).removeModifier(modifier);
            }
        } catch (NullPointerException e) {
            ;
        }
    }

    @Override
    public boolean attackEntityFrom(EntityLivingBase entityLivingBase, DamageSource source, float amount) {

        int lock = 0;
        if (entityLivingBase instanceof IEntityAvoidThingCreature) {
           lock = unlock((IEntityAvoidThingCreature) entityLivingBase);
        }
        boolean flag = false;
        try {
            if (entityLivingBase instanceof AbstractEntityMagicCreature)
                flag = entityLivingBase.attackEntityFrom(source, amount);
            else if (source.getImmediateSource() != null && source.getImmediateSource() instanceof EntityLivingBase)
                flag = entityLivingBase.attackEntityFrom(source, caculateDamageAmount((EntityLivingBase) source.getImmediateSource(), entityLivingBase, null, amount));
            else if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityLivingBase)
                flag = entityLivingBase.attackEntityFrom(source, caculateDamageAmount((EntityLivingBase) source.getTrueSource(), entityLivingBase, null, amount));
            else
                flag = entityLivingBase.attackEntityFrom(source, caculateDamageAmount(null, entityLivingBase, null, amount));
        }
        catch (Exception e) {
            return false;
        }
        if (entityLivingBase instanceof IEntityAvoidThingCreature) {
            lock((IEntityAvoidThingCreature) entityLivingBase, lock);
        }

        return flag;
    }

    @Override
    public void setHealth(EntityLivingBase entityLivingBase, float amount) {
        int lock = 0;
        if (entityLivingBase instanceof IEntityAvoidThingCreature) {
            lock = unlock((IEntityAvoidThingCreature) entityLivingBase);
        }

        try {
            entityLivingBase.setHealth(amount);
        }
        catch (Exception e) {
            ;
        }

        if (entityLivingBase instanceof IEntityAvoidThingCreature) {
            lock((IEntityAvoidThingCreature) entityLivingBase, lock);
        }
    }

    /**
     * 单例模式，用于魔法生物的战斗结算，在e构造函数中调用
     * @return 返回实例
     */
    public static IMagicFightManager getInstance() {
        if (instance == null)
            instance = new IMagicFightManagerImpl();
        return instance;
    }
}
