package com.xwm.magicmaid.creativetab;

import com.xwm.magicmaid.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CreativeTabMaid extends CreativeTabs {

    public static final CreativeTabMaid CREATIVE_TAB_MAID = new CreativeTabMaid("creative_tab_maid");

    public CreativeTabMaid(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemInit.itemLostKey);
    }
}
