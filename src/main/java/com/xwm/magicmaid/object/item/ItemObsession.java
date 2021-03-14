package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.object.item.interfaces.ICanGetSkillPoint;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemObsession extends ItemBase implements ICanGetSkillPoint
{
    public ItemObsession(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "在那个黑暗的时代，仍不乏有纯粹的灵魂");
        tooltip.add(TextFormatting.YELLOW + "他们受到教会的审判后，灵魂不得解脱，化作执念");
        tooltip.add("");
        tooltip.add(TextFormatting.GREEN + "可以吸收点数: " + getSkillPoint(stack, null));
    }

    @Override
    public int getSkillPoint(ItemStack stack, EntityPlayer player) {
        return 2;
    }
}
