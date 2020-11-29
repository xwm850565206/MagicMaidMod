package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MagicEquipmentRegistry
{
    private static HashMap<EnumEquipment, ItemEquipment> EQUIPMENT = new HashMap<>();
    private static HashMap<EnumEquipment, Item> PIECE = new HashMap<>();
    private static HashMap<EnumAttackType, List<Integer>> ATTACK = new HashMap<>();

    public static void registerEquipmentPiece(EnumEquipment enumEquipment, ItemEquipment equipment, Item piece)
    {
        EQUIPMENT.put(enumEquipment, equipment);
        PIECE.put(enumEquipment, piece);
    }

    public static void registerEquipmentAttack(EnumAttackType type, List<Integer> damage)
    {
        ATTACK.put(type, damage);
    }

    public static List<Integer> getEquipmentAttack(EnumAttackType type) {
        return ATTACK.getOrDefault(type, new ArrayList<>());
    }

    public static int getEquipmentAttack(EnumAttackType type, int level) {
        List<Integer> attackList = getEquipmentAttack(type);
        if (attackList.size() <= level)
            return 0;
        else
            return attackList.get(level);
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
        registerEquipmentPiece(EnumEquipment.REPATENCE, (ItemEquipment) ItemInit.ITEM_REPANTENCE, ItemInit.itemRepentancePiece);
        registerEquipmentPiece(EnumEquipment.CONVICTION, (ItemEquipment) ItemInit.ITEM_CONVICTION, ItemInit.itemConvictionPiece);
        registerEquipmentPiece(EnumEquipment.PROTECTOR, (ItemEquipment) ItemInit.ITEM_PROTECTOR, ItemInit.itemProtectorPiece);
        registerEquipmentPiece(EnumEquipment.DEMONKILLINGSWORD, (ItemEquipment) ItemInit.ITEM_DEMON_KILLER_SWORD, ItemInit.itemDemonKillerPiece);
        registerEquipmentPiece(EnumEquipment.IMMORTAL, (ItemEquipment) ItemInit.ITEM_IMMORTAL, ItemInit.itemImmortalPiece);
        registerEquipmentPiece(EnumEquipment.PANDORA, (ItemEquipment) ItemInit.ITEM_PANDORA, ItemInit.itemPandoraPiece);
        registerEquipmentPiece(EnumEquipment.WHISPER, (ItemEquipment) ItemInit.ITEM_WHISPER, ItemInit.itemWhisperPiece);
        registerEquipmentPiece(EnumEquipment.WISE, (ItemEquipment) ItemInit.ITEM_WISE, ItemInit.itemWisePiece);
    }
}
