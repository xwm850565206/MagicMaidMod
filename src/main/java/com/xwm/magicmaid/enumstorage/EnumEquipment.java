package com.xwm.magicmaid.enumstorage;

import com.xwm.magicmaid.entity.mob.weapon.*;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import net.minecraft.world.World;

public enum EnumEquipment
{
    NONE,
    REPATENCE, CONVICTION, PROTECTOR,
    DEMONKILLINGSWORD, IMMORTAL,
    PANDORA, WHISPER, WISE;

    public static int toInt(EnumEquipment weapon)
    {
        for (int i = 0; i < EnumEquipment.values().length; i++) if (weapon == EnumEquipment.values()[i])
            return i;
        return 0;
    }

    public static EnumEquipment valueOf(int weapon){
        return EnumEquipment.values()[weapon];
    }

    public static ItemEquipment toItemEquipment(EnumEquipment equipment)
    {
        return MagicEquipmentRegistry.getEquipment(equipment);
    }

    public static EntityMaidWeapon toEntityMaidWeapon(EnumEquipment equipment, World world)
    {
        switch (equipment){
            case NONE: return null;
            case REPATENCE: return new EntityMaidWeaponRepantence(world);
            case CONVICTION: return new EntityMaidWeaponConviction(world);
            case PROTECTOR: return null;//盔甲没有实体
            case DEMONKILLINGSWORD: return null; //大剑没有实体
            case IMMORTAL: return null; //盔甲没有实体
            case PANDORA: return new EntityMaidWeaponPandorasBox(world);
            case WHISPER: return new EntityMaidWeaponWhisper(world);
            case WISE: return null; //盔甲没有实体
        }
        return null;
    }
}
