package com.xwm.magicmaid.world.biome;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
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
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityBat.class, 1, 2, 3));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityVillager.class, 10, 10, 20));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPigZombie.class, 10, 10, 20));

//        this.spawnableMonsterList.add(new SpawnListEntry(EntityWither.class, 10, 1, 5));
    }
}
