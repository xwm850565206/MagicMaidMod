package com.xwm.magicmaid.player.skill.perfomskill.secret;

import com.xwm.magicmaid.player.skill.perfomskill.PerformSkillBase;

public abstract class PerformSkillSecretBase extends PerformSkillBase
{
    @Override
    public String getName() {
        return super.getName() + ".secret";
    }
}
