package com.xwm.magicmaid.entity.maid;

public enum  EnumModes
{
    SERVE, FIGHT, BOSS;

    public static EnumModes valueOf(int mode) {
        return EnumModes.values()[mode];
    }

    public static int toInt(EnumModes mode){
        for (int i = 0; i < EnumModes.values().length; i++) if (mode == EnumModes.values()[i]) return i;
        return 0;
    }
}
