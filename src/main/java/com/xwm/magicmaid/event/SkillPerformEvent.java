package com.xwm.magicmaid.event;

import com.xwm.magicmaid.player.skill.ISkill;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SkillPerformEvent<T extends ISkill> extends Event
{
    private T skill;
    private EntityLivingBase player;
    private BlockPos pos;

    public SkillPerformEvent(T skill, EntityLivingBase player, BlockPos pos) {
        this.skill = skill;
        this.player = player;
        this.pos = pos;
    }

    public T getSkill() {
        return skill;
    }

    public void setSkill(T skill) {
        this.skill = skill;
    }

    public EntityLivingBase getPlayer() {
        return player;
    }

    public void setPlayer(EntityLivingBase player) {
        this.player = player;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
}
