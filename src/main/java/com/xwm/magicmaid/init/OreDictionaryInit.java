package com.xwm.magicmaid.init;

import com.xwm.magicmaid.util.Reference;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryInit
{
    public static void register()
    {
        OreDictionary.registerOre("ingotHolyStone" + Reference.MODID, ItemInit.ITEM_HOLY_STONE);
        OreDictionary.registerOre("oreHolyStone" + Reference.MODID, BlockInit.BLOCK_HOLY_STONE);
    }
}
