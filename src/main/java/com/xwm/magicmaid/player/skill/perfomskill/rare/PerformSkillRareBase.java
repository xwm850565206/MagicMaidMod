package com.xwm.magicmaid.player.skill.perfomskill.rare;

import com.xwm.magicmaid.player.skill.perfomskill.PerformSkillBase;

public abstract class PerformSkillRareBase extends PerformSkillBase
{
    @Override
    public String getName() {
        return super.getName() + ".rare";
    }
}
