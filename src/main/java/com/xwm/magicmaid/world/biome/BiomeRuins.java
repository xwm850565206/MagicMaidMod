package com.xwm.magicmaid.world.biome;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.biome.Biome;

public class BiomeRuins extends Biome
{
    public BiomeRuins() {
        super(new BiomeProperties("ruins")
                .setBaseHeight(0.125f)
                .setHeightVariation(0.05f)
                .setTemperature(0.5f)
                .setRainfall(0.8f)
                .setWaterColor(0xDDDDDD));
//        topBlock = Blocks.STONE.getDefaultState();
        this.decorator.treesPerChunk = 1;

        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        //this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityBat.class, 1, 1, 1));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityVillager.class, 1, 1, 1));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityZombie.class, 1, 4, 4));

//        this.spawnableMonsterList.add(new SpawnListEntry(EntityWither.class, 10, 1, 5));
    }

    /**
     * returns the chance a creature has to spawn.
     */
    public float getSpawningChance()
    {
        return 0.001F;
    }
}
