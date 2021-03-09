package com.xwm.magicmaid.event;

import com.xwm.magicmaid.player.skill.ISkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * todo
 * 这个时间最好在服务器fire
 * @param <T> 升级的技能类别
 */
public class SkillLevelUpEvent<T extends ISkill> extends Event
{
    private T skill;
    private EntityPlayer player;

    public SkillLevelUpEvent(T skill, EntityPlayer player)
    {
        this.skill = skill;
        this.player = player;
    }

    public T getSkill() {
        return skill;
    }

    public void setSkill(T skill) {
        this.skill = skill;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    public static class Pre extends SkillLevelUpEvent {
        public Pre(ISkill skill, EntityPlayer player) {
            super(skill, player);
        }
    }

    public static class Post extends SkillLevelUpEvent {
        public Post(ISkill skill, EntityPlayer player) {
            super(skill, player);
        }
    }
}
