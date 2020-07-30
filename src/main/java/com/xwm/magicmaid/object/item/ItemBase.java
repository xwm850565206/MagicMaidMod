package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.creativetab.CreativeTabMaid;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.util.IHasModel;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel
{
    public ItemBase(String name)
    {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabMaid.CREATIVE_TAB_MAID);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels()
    {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}