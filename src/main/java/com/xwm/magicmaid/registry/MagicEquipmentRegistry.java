package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import net.minecraft.item.Item;

import java.util.HashMap;

public class MagicItemRegisty
{
    private static HashMap<EnumEquipment, ItemEquipment> EQUIPMENT = new HashMap<>();
    private static HashMap<EnumEquipment, Item> PIECE = new HashMap<>();

    public static void register(EnumEquipment enumEquipment, ItemEquipment equipment, Item piece)
    {
        EQUIPMENT.put(enumEquipment, equipment);
        PIECE.put(enumEquipment, piece);
    }

    public static void registerWithoutPiece(EnumEquipment enumEquipment, ItemEquipment equipment)
    {
        EQUIPMENT.put(enumEquipment, equipment);
    }

    public static ItemEquipment getEquipment(EnumEquipment enumEquipment)
    {
        return EQUIPMENT.getOrDefault(enumEquipment, null);
    }

    public static Item getPiece(EnumEquipment enumEquipment)
    {
        return PIECE.getOrDefault(enumEquipment, null);
    }

    public static void registerAllEquipment()
    {
        register(EnumEquipment.REPATENCE, (ItemEquipment) ItemInit.ITEM_REPANTENCE, ItemInit.itemRepentancePiece);
        register(EnumEquipment.CONVICTION, (ItemEquipment) ItemInit.ITEM_CONVICTION, ItemInit.itemConvictionPiece);
        register(EnumEquipment.PROTECTOR, (ItemEquipment) ItemInit.ITEM_PROTECTOR, ItemInit.itemProtectorPiece);
        register(EnumEquipment.DEMONKILLINGSWORD, (ItemEquipment) ItemInit.ITEM_DEMON_KILLER_SWORD, ItemInit.itemDemonKillerPiece);
        register(EnumEquipment.IMMORTAL, (ItemEquipment) ItemInit.ITEM_IMMORTAL, ItemInit.itemImmortalPiece);
        register(EnumEquipment.PANDORA, (ItemEquipment) ItemInit.ITEM_PANDORA, ItemInit.itemPandoraPiece);
        register(EnumEquipment.WHISPER, (ItemEquipment) ItemInit.ITEM_WHISPER, ItemInit.itemWhisperPiece);
        register(EnumEquipment.WISE, (ItemEquipment) ItemInit.ITEM_WISE, ItemInit.itemWisePiece);
    }
}
