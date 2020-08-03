package com.xwm.magicmaid.init;

import com.xwm.magicmaid.world.biome.BiomeRuins;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeInit
{
    public static final Biome RUINS = new BiomeRuins();

    public static void registerBiomes()
    {
        initBiome(RUINS, "ruins", BiomeType.WARM, Type.DENSE, Type.MAGICAL, Type.DRY);
    }

    private static Biome initBiome(Biome biome, String name, BiomeType biomeType, Type... type)
    {
        biome.setRegistryName(name);
        ForgeRegistries.BIOMES.register(biome);
        BiomeDictionary.addTypes(biome, type);
        BiomeManager.addBiome(biomeType, new BiomeEntry(biome, 10));
//        BiomeManager.addSpawnBiome(biome);
        return biome;
    }
}
