package com.xwm.magicmaid.event.loader;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.mob.basic.AbstractEntityMagicCreature;
import com.xwm.magicmaid.event.SkillLevelUpEvent;
import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.player.skill.IAttributeSkill;
import com.xwm.magicmaid.player.skill.IPassiveSkill;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SkillEventLoader
{
    /**
     * 当属性技能升级时，需要刷新玩家的被动
     * @param event
     */
    @SubscribeEvent
    public static void onSkillLevelUp(SkillLevelUpEvent.Post event)
    {
        ISkill skill = event.getSkill();
        if (skill instanceof IAttributeSkill) {
            ((IAttributeSkill) skill).perform(event.getPlayer());
            if(event.getPlayer().getEntityWorld().isRemote)
                IMagicCreatureManagerImpl.getInstance().updateToServer(event.getPlayer()); // 被动技能更新了基本属性，要通知服务器
            Main.logger.info("level up: " + skill.getName() + " level: " + skill.getLevel());
        }
        else if (skill instanceof IPassiveSkill) {
            ; //todo
        }
        else if (skill instanceof IPerformSkill) {
            ; //todo
        }
    }

    /**
     * 战斗逻辑 无视减伤与减伤的实现
     */
    @SubscribeEvent
    public void onEntityAttacked(LivingDamageEvent event)
    {
        if (event.getEntityLiving() instanceof AbstractEntityMagicCreature)
            return; // 涉及到惩罚机制，单独判断
        else {
            try {
                event.setAmount(IMagicCreatureManagerImpl.getInstance().caculateDamageAmount((EntityLivingBase) event.getSource().getImmediateSource(), event.getEntityLiving(), null, event.getAmount()));
            } catch (Exception e ){
                e.printStackTrace();
            }
        }
    }

    /**
     * note: remove 在属性技能的perform实现了相关逻辑，这个函数需要条件如暴击，才能触发
     * 战斗逻辑 普通伤害率的实现
     */
    @SubscribeEvent
    public void onPlayerAttackEntity(CriticalHitEvent event)
    {
//        try {
//            EntityPlayer player = event.getEntityPlayer();
//            if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
//            {
//                ICreatureCapability creatureCapability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
//                float modifier = (float) creatureCapability.getNormalDamageRate();
//                event.setDamageModifier(event.getDamageModifier() * modifier);
//
//            }
//        } catch (Exception e ){
//            e.printStackTrace();
//        }
    }

}
