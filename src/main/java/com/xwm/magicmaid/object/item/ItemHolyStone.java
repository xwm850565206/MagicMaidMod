package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.Main;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemHolyStone extends ItemBase
{
    public ItemHolyStone(String name) {
        super(name);
        this.setMaxDamage(0); // This makes it so your item doesn't have the damage bar at the bottom of its icon, when "damaged" similar to the Tools.
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "这块石头里蕴含着非凡的能量");

        if (stack.getItemDamage() > 0)
        {
            int level = stack.getItemDamage();
            if (level < 2) {
                tooltip.add(TextFormatting.GRAY + "品质: +" + level);
            }
            else if (level < 4) {
                tooltip.add(TextFormatting.YELLOW + "品质: +" + level);
            }
            else{
                tooltip.add(TextFormatting.LIGHT_PURPLE + "品质: +" + level);
            }
        }
    }


    @Override
    public void registerModels()
    {
        for (int i = 0; i <= 6; i++)
            Main.proxy.registerItemRenderer(this, i, "inventory");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return stack.getItemDamage() > 0 || stack.isItemEnchanted();
    }
}
