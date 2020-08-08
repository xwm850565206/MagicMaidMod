package com.xwm.magicmaid.init;

import com.xwm.magicmaid.object.item.ItemConviction;
import com.xwm.magicmaid.object.item.ItemDemonKillerSword;
import com.xwm.magicmaid.object.item.ItemPandora;
import com.xwm.magicmaid.object.item.ItemRepantence;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit
{
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item ItemRepantence = new ItemRepantence("repantence");
    public static final Item ItemConviction = new ItemConviction("conviction");
    public static final Item ItemDemonKillerSowrd = new ItemDemonKillerSword("demonkiller");
    public static final Item ItemPandorasBox = new ItemPandora("pandorasbox");

}
