package com.xwm.magicmaid.entity.mob.weapon;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.init.EntityInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.object.item.ItemEquipment;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public enum EnumEquipments
{
    NONE,
    REPATENCE, CONVICTION, PROTECTOR,
    DEMONKILLINGSWORD, IMMORTAL,
    PANDORA, WHISPER, WISE;

    public static int toInt(EnumEquipments weapon)
    {
        for (int i = 0; i < EnumEquipments.values().length; i++) if (weapon == EnumEquipments.values()[i])
            return i;
        return 0;
    }

    public static EnumEquipments valueOf(int weapon){
        return EnumEquipments.values()[weapon];
    }

    public static ItemEquipment toItemEquipment(EnumEquipments equipment)
    {
        for (Item item : ItemInit.ITEMS)
        {
            if (item instanceof  ItemEquipment)
            {
                if (((ItemEquipment) item).enumEquipment.equals(equipment))
                    return (ItemEquipment) item;
            }
        }

        return null;
    }

    public static EntityMaidWeapon toEntityMaidWeapon(EnumEquipments equipment, World world)
    {
        switch (equipment){
            case NONE: return null;
            case REPATENCE: return new EntityMaidWeaponRepantence(world);
            case CONVICTION: return new EntityMaidWeaponConviction(world);
            case PROTECTOR: return null;//盔甲没有实体
            case DEMONKILLINGSWORD: return null; //todo 还没写
            case IMMORTAL: return null; //盔甲没有实体
            case PANDORA: return null; //todo 还没写
            case WHISPER: return null; //todo 还没写
            case WISE: return null; //盔甲没有实体
        }

        return null;
    }
}
