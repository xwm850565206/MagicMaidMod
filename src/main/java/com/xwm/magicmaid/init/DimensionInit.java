package com.xwm.magicmaid.init;

import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.world.dimension.DimensionChurch;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class DimensionInit
{
    public static int DIMENSION_CHURCH = findFreeDimensionID();

    private static Integer findFreeDimensionID() {
        for (int i = 2; i < Integer.MAX_VALUE; i++) {
            if (!DimensionManager.isDimensionRegistered(i)) {
                return i;
            }
        }
        return null;
    }
    public static final DimensionType CHURCH = DimensionType.register("church", "_church", DIMENSION_CHURCH, DimensionChurch.class, false);

    public static void registerDimensions()
    {
        DimensionManager.registerDimension(Reference.DIMENSION_CHURCH, CHURCH);
    }
}
