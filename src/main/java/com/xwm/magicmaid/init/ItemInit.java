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

    public static final Item ITEME_INSTRUCCTION_BOOK = new ItemInstructionBook("instruction_book");

    public static final Item ITEM_REPANTENCE = new ItemRepantence("repantence");
    public static final Item ITEM_CONVICTION = new ItemConviction("conviction");
    public static final Item ITEM_DEMON_KILLER_SWORD = new ItemDemonKillerSword("demonkiller");
    public static final Item ITEM_PANDORA = new ItemPandora("pandorasbox");
    public static final Item ITEM_WHISPER = new ItemWhisper("whisper");
    public static final Item ITEM_PROTECTOR = new ItemProtector("protector");
    public static final Item ITEM_IMMORTAL = new ItemImmortal("immortal");
    public static final Item ITEM_WISE = new ItemWise("wise");

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


    public static final Item ITEM_HOLY_STONE = new ItemHolyStone("holystone");
    public static final Item ITEM_LOST_KEY = new ItemLostKey("lostkey");
    public static final Item ITEM_THE_GOSPELS = new ItemTheGospels("the_gospels");
    public static final Item ITEM_REMAINING_SOFT = new ItemRemainingSoft("remaining_soft");

    public static final Item itemHolyFruitMartha = new ItemHolyFruitMartha("holy_fruit_martha");
    public static final Item itemHolyFruitRett = new ItemHolyFruitRett("holy_fruit_rett");
    public static final Item itemHolyFruitSelina = new ItemHolyFruitSelina("holy_fruit_selina");

    public static final Item ITEM_BIAN_FLOWER_POLLEN = new ItemBianFlowerPollen("bian_flower_pollen");
    public static final Item ITEM_OBSESSION = new ItemObsession("obsession");
    public static final Item ITEM_OBSESSION_SWORD = new ItemObsessionSword("obsession_sword");
    public static final Item ITEM_GHOST = new ItemGhost("ghost");
    public static final Item ITEM_GHOST_OBSESSION_SWORD = new ItemGhostObsessionSword("ghost_obsession_sword");
    public static final Item ITEM_ELIMINATE_SOUL_NAIL = new ItemEliminateSoulNail("eliminate_soul_nail");
    public static final Item ITEM_NO_SOUL_GHOST = new ItemNoSoulGhost("no_soul_ghost");
    public static final Item ITEM_ABSORB_SOUL_COMPASS = new ItemAbsorbSoulCompass("absorb_soul_compass");
    public static final Item ITEM_JUSTICE_SOUL_PIECE = new ItemJusticeSoulPiece("justice_soul_piece");
    public static final Item ITEM_EVIL_SOUL_PIECE = new ItemEvilSoulPiece("evil_soul_piece");

    public static final Item ITEM_JUSTICE = new ItemJustice("justice");
    public static final Item ITEM_EVIL = new ItemEvil("evil");

    public static final Item SKILL_BOOK = new ItemSkillBook("skill_book");
}
