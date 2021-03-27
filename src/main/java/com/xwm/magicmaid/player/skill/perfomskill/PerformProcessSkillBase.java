package com.xwm.magicmaid.player.skill.perfomskill;

public abstract class PerformProcessSkillBase extends PerformSkillBase
{
    /**
     * 每个tick age都会加1，到达maxAge后，技能释放完成
     * @return 最大年龄
     */
   public abstract int getMaxAge();
}
