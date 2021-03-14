package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.object.item.interfaces.ICanGetSkillPoint;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemGhost extends ItemBase implements ICanGetSkillPoint
{
    public ItemGhost(String name) {
        super(name);
    }

    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "逝者的灵魂，徘徊在彼岸");
        tooltip.add("");
        tooltip.add(TextFormatting.GREEN + "可以吸收点数: " + getSkillPoint(stack, null));
    }

    @Override
    public int getSkillPoint(ItemStack stack, EntityPlayer player) {
        return 3;
    }
}
