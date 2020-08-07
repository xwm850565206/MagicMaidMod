package com.xwm.magicmaid.enumstorage;

public enum EnumMode
{
    SERVE, FIGHT, SITTING, BOSS;

    public static EnumMode valueOf(int mode) {
        return EnumMode.values()[mode];
    }

    public static int toInt(EnumMode mode){
        for (int i = 0; i < EnumMode.values().length; i++) if (mode == EnumMode.values()[i]) return i;
        return 0;
    }
}
