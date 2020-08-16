package com.xwm.magicmaid.init;

import com.xwm.magicmaid.object.item.*;
import com.xwm.magicmaid.object.item.equipment.*;
import com.xwm.magicmaid.object.item.piece.*;
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
    public static final Item itemProtector = new ItemProtector("protector");
    public static final Item itemImmortal = new ItemImmortal("immortal");
    public static final Item itemWise = new ItemWise("wise");

    public static final Item itemMemoryPieceMartha = new ItemMemoryPieceMartha("memorypiecemartha");
    public static final Item itemMemoryPieceSelina = new ItemMemoryPieceMartha("memorypieceselina");
    public static final Item itemMemoryPieceRett = new ItemMemoryPieceMartha("memorypiecerett");
    public static final Item itemProtectorPiece = new ItemProtectorPiece("protectorpiece");
    public static final Item itemImmortalPiece = new ItemImmortalPiece("immortalpiece");
    public static final Item itemWisePiece = new ItemWisePiece("wisepiece");
    public static final Item itemRepentancePiece = new ItemRepantencePiece("repantencepiece");
    public static final Item itemConvictionPiece = new ItemConvictionPiece("convictionpiece");
    public static final Item itemDemonKillerPiece = new ItemDemonKillerPiece("demonkillerpiece");
    public static final Item itemPandoraPiece = new ItemPandoraPiece("pandorapiece");
    public static final Item itemWhisperPiece = new ItemWhisperPiece("whisperpiece");

    public static final Item itemMemoryMartha = new ItemMemoryMartha("memorymartha");
    public static final Item itemMemoryRett = new ItemMemoryRett("memoryrett");
    public static final Item itemMemorySelina = new ItemMemorySelina("memoryselina");


    public static final Item itemHolyStone = new ItemHolyStone("holystone");
    public static final Item itemLostKey = new ItemLostKey("lostkey");
    public static final Item itemTheGospels = new ItemTheGospels("the_gospels");
    public static final Item itemRemainingSoft = new ItemRemainingSoft("remaining_soft");

    public static final Item itemHolyFruitMartha = new ItemHolyFruitMartha("holy_fruit_martha");
    public static final Item itemHolyFruitRett = new ItemHolyFruitRett("holy_fruit_rett");
    public static final Item itemHolyFruitSelina = new ItemHolyFruitSelina("holy_fruit_selina");
}
