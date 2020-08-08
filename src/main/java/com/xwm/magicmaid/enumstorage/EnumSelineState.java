package com.xwm.magicmaid.enumstorage;

public enum EnumSelineState
{
    STANDARD, SITTING, SERVE, CHANT, STIMULATE;

    public static EnumSelineState valueOf(int state) {
    return EnumSelineState.values()[state];
}

    public static int toInt(EnumSelineState state){
        for (int i = 0; i < EnumSelineState.values().length; i++) if (state == EnumSelineState.values()[i]) return i;
        return 0;
    }
}
