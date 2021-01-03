package com.xwm.magicmaid.event;

import com.xwm.magicmaid.player.skill.ISkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SkillChangedEvent extends Event
{
    private EntityPlayer entityPlayer;
    private ISkill oldSkill;
    private ISkill newSkill;

    public SkillChangedEvent(EntityPlayer entityPlayer, ISkill oldSkill, ISkill newSkill) {
        this.entityPlayer = entityPlayer;
        this.oldSkill = oldSkill;
        this.newSkill = newSkill;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public void setEntityPlayer(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

    public ISkill getOldSkill() {
        return oldSkill;
    }

    public void setOldSkill(ISkill oldSkill) {
        this.oldSkill = oldSkill;
    }

    public ISkill getNewSkill() {
        return newSkill;
    }

    public void setNewSkill(ISkill newSkill) {
        this.newSkill = newSkill;
    }
}
