package com.xwm.magicmaid.init;

import com.xwm.magicmaid.world.dimension.DimensionChurch;
import com.xwm.magicmaid.world.gen.WorldGenFlowers;
import com.xwm.magicmaid.world.gen.WorldGenHolyStone;
import com.xwm.magicmaid.world.gen.WorldGenHut;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
        DimensionManager.registerDimension(DIMENSION_CHURCH, CHURCH);
    }

    public static void registerWorldGenerators()
    {
        GameRegistry.registerWorldGenerator(new WorldGenHolyStone(), 10);
        GameRegistry.registerWorldGenerator(new WorldGenFlowers(), 10);
        GameRegistry.registerWorldGenerator(new WorldGenHut(), 1);
    }
}
