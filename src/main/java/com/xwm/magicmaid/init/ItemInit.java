package com.xwm.magicmaid.init;

import com.xwm.magicmaid.object.item.ItemHolyStone;
import com.xwm.magicmaid.object.item.ItemMemoryPieceMartha;
import com.xwm.magicmaid.object.item.equipment.*;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit
{
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item itemRepantence = new ItemRepantence("repantence");
    public static final Item itemConviction = new ItemConviction("conviction");
    public static final Item itemDemonKillerSowrd = new ItemDemonKillerSword("demonkiller");
    public static final Item itemPandorasBox = new ItemPandora("pandorasbox");
    public static final Item itemWhisper = new ItemWhisper("whisper");

    public static final Item itemHolyStone = new ItemHolyStone("holystone");

    public static final Item itemMemoryPieceMartha = new ItemMemoryPieceMartha("memorypiecemartha");
    public static final Item itemMemoryPieceSelina = new ItemMemoryPieceMartha("memorypieceselina");
    public static final Item itemMemoryPieceRett = new ItemMemoryPieceMartha("memorypiecerett");

}
