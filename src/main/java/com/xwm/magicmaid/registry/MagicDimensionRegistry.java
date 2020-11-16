package com.xwm.magicmaid.registry;

import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.util.Reference;

import java.util.HashMap;

public class MagicDimensionRegistry
{
    private static HashMap<Integer, Integer> FROM_TO = new HashMap<>();

    public static void register(int from, int to)
    {
        FROM_TO.put(from, to);
    }

    public static int getNextDimension(int from)
    {
        return FROM_TO.getOrDefault(from, 0);
    }

    public static void registerAll()
    {
        register(0, DimensionInit.DIMENSION_CHURCH);
    }
}
