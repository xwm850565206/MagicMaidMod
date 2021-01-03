package com.xwm.magicmaid.event.loader;

import com.xwm.magicmaid.event.SkillLevelUpEvent;
import com.xwm.magicmaid.player.skill.IAttributeSkill;
import com.xwm.magicmaid.player.skill.IPassiveSkill;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
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
    public static void onSkillLevelUp(SkillLevelUpEvent<ISkill> event)
    {
        ISkill skill = event.getSkill();
        if (skill instanceof IAttributeSkill) {
            ((IAttributeSkill) skill).perform(event.getPlayer());
            System.out.println("level up: " + skill.getName());
        }
        else if (skill instanceof IPassiveSkill) {
            ; //todo
        }
        else if (skill instanceof IPerformSkill) {
            ; //todo
        }
    }
}
