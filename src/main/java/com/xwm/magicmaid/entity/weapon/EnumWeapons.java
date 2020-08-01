package com.xwm.magicmaid.entity.weapon;

public enum  EnumWeapons
{
    NONE, REPATENCE, CONVICTION;

    public static int toInt(EnumWeapons weapon)
    {
        for (int i = 0; i < EnumWeapons.values().length; i++) if (weapon == EnumWeapons.values()[i])
            return i;
        return 0;
    }

    public static EnumWeapons valueOf(int weapon){
        return EnumWeapons.values()[weapon];
    }
}
