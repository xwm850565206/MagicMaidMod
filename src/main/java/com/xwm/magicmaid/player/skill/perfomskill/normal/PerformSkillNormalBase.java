package com.xwm.magicmaid.player.skill.perfomskill.normal;

import com.xwm.magicmaid.player.skill.perfomskill.PerformSkillBase;

public abstract class PerformSkillNormalBase extends PerformSkillBase
{
    @Override
    public String getName() {
        return super.getName() + ".normal";
    }
}
