package com.xwm.magicmaid.registry;

import com.google.common.collect.Lists;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponConviction;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponRepantence;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.object.item.equipment.*;
import net.minecraft.init.Items;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicEquipmentRegistry
{
    public static List<EquipmentAttribute> ATTRIBUTES_LIST = new ArrayList<>();
    public static Map<String, EquipmentAttribute> ATTRIBUTES = new HashMap<>();

    public static EquipmentAttribute getAttribute(String name)
    {
        return ATTRIBUTES.getOrDefault(name, NONE);
    }

    public static void updateMap() {
        ATTRIBUTES_LIST.forEach((item) -> {
            String name1 = item.getName();
            if (name1 == null)
                name1 = "";
            ATTRIBUTES.put(name1, item);
        });
    }

    public static EquipmentAttribute NONE = new EquipmentAttribute()
            .setName("none")
            .setType(EquipmentAttribute.EquipmentType.NONE)
            .setPiece(Items.AIR)
            .setAttackDamage(Lists.newArrayList(0, 0, 0, 0, 0, 0, 0, 0))
            .setBaseArea(new Vec3d(0, 0, 0));

    public static EquipmentAttribute NORMAL = new EquipmentAttribute()
            .setName("normal")
            .setType(EquipmentAttribute.EquipmentType.NONE)
            .setPiece(Items.AIR)
            .setAttackDamage(Lists.newArrayList(0, 0, 0, 0, 0, 0, 0, 0))
            .setBaseArea(new Vec3d(0, 0, 0));

    public static EquipmentAttribute REPANTENCE = new EquipmentAttribute()
            .setName("repantence")
            .setEquipment((ItemEquipment) ItemInit.ITEM_REPANTENCE)
            .setPiece(ItemInit.itemRepentancePiece)
            .setEntityClass(EntityMaidWeaponRepantence.class)
            .setAttackDamage(ItemInit.ITEM_REPANTENCE.getAttackDamage())
            .setBaseArea(new Vec3d(6, 6, 6))
            .setGrowArea(new Vec3d(0.5, 0.5, 0.5))
            .setType(EquipmentAttribute.EquipmentType.WEAPON);

    public static EquipmentAttribute CONVICTION = new EquipmentAttribute()
            .setName("conviction")
            .setEquipment((ItemEquipment) ItemInit.ITEM_CONVICTION)
            .setPiece(ItemInit.itemConvictionPiece)
            .setEntityClass(EntityMaidWeaponConviction.class)
            .setAttackDamage(ItemInit.ITEM_CONVICTION.getAttackDamage())
            .setBaseArea(new Vec3d(4, 3, 4))
            .setGrowArea(new Vec3d(1.0, 0.0, 1.0))
            .setType(EquipmentAttribute.EquipmentType.WEAPON);

    public static EquipmentAttribute PROTECTOR = new EquipmentAttribute()
            .setName("protector")
            .setEquipment((ItemEquipment) ItemInit.ITEM_PROTECTOR)
            .setPiece(ItemInit.itemProtectorPiece)
            .setType(EquipmentAttribute.EquipmentType.ARMOR);

    public static EquipmentAttribute DEMONKILLINGSWORD = new EquipmentAttribute()
            .setName("demon_killer_sword")
            .setEquipment((ItemEquipment) ItemInit.ITEM_DEMON_KILLER_SWORD)
            .setPiece(ItemInit.itemDemonKillerPiece)
            .setAttackDamage(ItemInit.ITEM_DEMON_KILLER_SWORD.getAttackDamage())
            .setBaseArea(new Vec3d(1, 1, 1))
            .setGrowArea(new Vec3d(0.0, 0.0, 0.0))
            .setType(EquipmentAttribute.EquipmentType.WEAPON);

    public static EquipmentAttribute IMMORTAL = new EquipmentAttribute()
            .setName("immortal")
            .setEquipment((ItemEquipment) ItemInit.ITEM_IMMORTAL)
            .setPiece(ItemInit.itemImmortalPiece)
            .setType(EquipmentAttribute.EquipmentType.ARMOR);

    public static EquipmentAttribute PANDORA = new EquipmentAttribute()
            .setName("pandora")
            .setEquipment((ItemEquipment) ItemInit.ITEM_PANDORA)
            .setPiece(ItemInit.itemPandoraPiece)
            .setEntityClass(EntityMaidWeaponPandorasBox.class)
            .setAttackDamage(ItemInit.ITEM_PANDORA.getAttackDamage())
            .setBaseArea(new Vec3d(4, 4, 4))
            .setGrowArea(new Vec3d(0.5, 0.5, 0.5))
            .setType(EquipmentAttribute.EquipmentType.WEAPON);

    public static EquipmentAttribute WHISPER = new EquipmentAttribute()
            .setName("whisper")
            .setEquipment((ItemEquipment) ItemInit.ITEM_WHISPER)
            .setPiece(ItemInit.itemWhisperPiece)
            .setEntityClass(EntityMaidWeaponWhisper.class)
            .setAttackDamage(ItemInit.ITEM_WHISPER.getAttackDamage())
            .setBaseArea(new Vec3d(4, 2, 4))
            .setGrowArea(new Vec3d(0.75, 0, 0.75))
            .setType(EquipmentAttribute.EquipmentType.WEAPON);

    public static EquipmentAttribute WISE = new EquipmentAttribute()
            .setName("wise")
            .setEquipment((ItemEquipment) ItemInit.ITEM_WISE)
            .setPiece(ItemInit.itemWisePiece)
            .setType(EquipmentAttribute.EquipmentType.ARMOR);

    static {
        ItemInit.ITEM_REPANTENCE.setEquipmentAttribute(REPANTENCE);
        ItemInit.ITEM_CONVICTION.setEquipmentAttribute(CONVICTION);
        ItemInit.ITEM_PROTECTOR.setEquipmentAttribute(PROTECTOR);
        ItemInit.ITEM_DEMON_KILLER_SWORD.setEquipmentAttribute(DEMONKILLINGSWORD);
        ItemInit.ITEM_IMMORTAL.setEquipmentAttribute(IMMORTAL);
        ItemInit.ITEM_WHISPER.setEquipmentAttribute(WHISPER);
        ItemInit.ITEM_PANDORA.setEquipmentAttribute(PANDORA);
        ItemInit.ITEM_WISE.setEquipmentAttribute(WISE);
        updateMap();
    }

}
