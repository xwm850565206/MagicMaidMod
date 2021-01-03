package com.xwm.magicmaid.object.item.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ICanGetSkillPoint
{
    int getSkillPoint(ItemStack stack, EntityPlayer player);
}
