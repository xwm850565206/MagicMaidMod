package com.xwm.magicmaid.enumstorage;

public enum  EnumRettState
{
    STANDARD, SITTING, SERVE,
    DEMON_KILLER_STANDARD, DEMON_KILLER_ATTACK1, DEMON_KILLER_ATTACK2, DEMON_KILLER_ATTACK3, DEMON_KILLER_ATTACK4;

    public static EnumRettState valueOf(int state) {
    return EnumRettState.values()[state];
}

    public static int toInt(EnumRettState mode){
        for (int i = 0; i < EnumRettState.values().length; i++) if (mode == EnumRettState.values()[i]) return i;
        return 0;
    }
}
