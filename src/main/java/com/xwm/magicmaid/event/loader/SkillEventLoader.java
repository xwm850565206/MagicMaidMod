package com.xwm.magicmaid.event.loader;

import com.xwm.magicmaid.event.SkillLevelUpEvent;
import com.xwm.magicmaid.player.skill.IAttributeSkill;
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
    public static void onAttributeSkillLevelUp(SkillLevelUpEvent<IAttributeSkill> event)
    {
        IAttributeSkill skill = event.getSkill();
        skill.perform(event.getPlayer());
        System.out.println("level up: " + skill.getName());
    }
}
