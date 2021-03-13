package com.xwm.magicmaid.world.gen;

import net.minecraft.block.BlockGrass;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenHut implements IWorldGenerator
{
    private static WorldGenerator plantSecretHut = new WorldGeneratorSkillHut("hut/plant_secret_hut", "perform", "secret");
    private static WorldGenerator forestSecretHut = new WorldGeneratorSkillHut("hut/forest_secret_hut", "perform", "rare");
    private static WorldGenerator desertSecretHutBotton = new WorldGeneratorSkillHut("hut/desert_secret_hut_bottom", "perform", "rare");
    private static WorldGenerator desertSecretHutTop = new WorldGeneratorSkillHut("hut/desert_secret_hut_top", "perform", "rare");
    private static WorldGenerator smallSecretHut = new WorldGeneratorSkillHut("hut/small_secret_hut", "perform", "normal");

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch(world.provider.getDimension())
        {
            case 1:
                break;
            case 0:
//                mapGenSkillHut = new MapGenSkillHut(chunkGenerator);
//                mapGenSkillHut.generate(world, chunkX, chunkZ, new ChunkPrimer());
//                mapGenSkillHut.generateStructure(world, random, new ChunkPos(chunkX, chunkZ));
                generatePlantSecretHut(random, chunkX, chunkZ, world);
                generateForestSecretHut(random, chunkX, chunkZ, world);
                generateDesertSecretHut(random, chunkX, chunkZ, world);
                generateSmallSecretHut(random, chunkX, chunkZ, world);
                break;
            case -1:
                break;
        }
    }

    public void generatePlantSecretHut(Random random, int chunkX, int chunkZ, World world)
    {
        int minHeight = 30;
        int maxHeight = 80;
        boolean flag = false;
        BlockPos position = new BlockPos(chunkX * 16 + 8, minHeight + random.nextInt(maxHeight - minHeight), chunkZ * 16 + 8);
        if (random.nextFloat() < 0.01 && chunkX % 2 == 0 && chunkZ % 2 == 0){
            for (int i = 0; i < 16 && !flag; i ++) {
                for (int j = 0; j < 16 && !flag; j++) {
                     BlockPos pos = position.add(i, 0, j);
                    if (
                            (world.getBiome(pos) == Biomes.PLAINS ||
                            world.getBiome(pos) == Biomes.MESA_ROCK ||
                            world.getBiome(pos) == Biomes.MESA_CLEAR_ROCK ||
                            world.getBiome(pos) == Biomes.STONE_BEACH) &&
                            (world.getBlockState(pos).getBlock() == Blocks.STONE ||
                                    world.getBlockState(pos).getBlock() == Blocks.GRASS)) {
                        plantSecretHut.generate(world, random, pos);
                        flag = true;
                    }
                }
            }
        }
    }

    public void generateForestSecretHut(Random random, int chunkX, int chunkZ, World world)
    {
        int minHeight = 60;
        int maxHeight = 90;
        boolean flag = false;
        BlockPos position = new BlockPos(chunkX * 16, minHeight + random.nextInt(maxHeight - minHeight), chunkZ * 16);
        if (random.nextFloat() < 0.01){
            for (int i = 0; i < 16 && !flag; i ++) {
                for (int j = 0; j < 16 && !flag; j++) {
                    BlockPos pos = position.add(i, 0, j);
                    if (
                            (world.getBiome(pos) == Biomes.FOREST ||
                            world.getBiome(pos) == Biomes.FOREST_HILLS ||
                            world.getBiome(pos) == Biomes.SWAMPLAND ||
                            world.getBiome(pos) == Biomes.TAIGA ||
                            world.getBiome(pos) == Biomes.TAIGA_HILLS ||
                            world.getBiome(pos) == Biomes.EXTREME_HILLS ||
                            world.getBiome(pos) == Biomes.ROOFED_FOREST) &&
                            world.getBlockState(pos).getBlock() instanceof BlockGrass && world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
                        forestSecretHut.generate(world, random, pos);
                        flag = true;
                    }
                }
            }
        }
    }

    public void generateDesertSecretHut(Random random, int chunkX, int chunkZ, World world)
    {
        int minHeight = 50;
        int maxHeight = 80;
        boolean flag = false;
        BlockPos position = new BlockPos(chunkX * 16, minHeight + random.nextInt(maxHeight - minHeight), chunkZ * 16 );
        if (random.nextFloat() < 0.01 && chunkX % 2 == 0 && chunkZ % 2 == 0) {
            for (int i = 0; i < 16 && !flag; i++) {
                for (int j = 0; j < 16 && !flag; j++) {
                    BlockPos pos = position.add(i, 0, j);
                    if (
                            (world.getBiome(pos) == Biomes.DESERT || world.getBiome(pos) == Biomes.DESERT_HILLS) &&
                            (world.getBlockState(pos).getBlock() == Blocks.SAND || world.getBlockState(pos).getBlock() == Blocks.SANDSTONE) &&
                            world.getBlockState(pos.up()).getBlock() == Blocks.AIR ) {
                        desertSecretHutBotton.generate(world, random, pos);
                        desertSecretHutTop.generate(world, random, pos.up(32));
                        flag = true;
                    }
                }
            }
        }
    }

    public void generateSmallSecretHut(Random random, int chunkX, int chunkZ, World world)
    {
        int minHeight = 60;
        int maxHeight = 80;
        boolean flag = false;
        BlockPos position = new BlockPos(chunkX * 16, minHeight + random.nextInt(maxHeight - minHeight), chunkZ * 16);
        if (random.nextFloat() <= 1 && chunkX % 2 == 0 && chunkZ % 2 == 0) {
            for (int i = 0; i < 8 && !flag; i++) {
                for (int j = 0; j < 8 && !flag; j++) {
                    BlockPos pos = position.add(i, 0, j);
                    if ((world.getBlockState(pos).getBlock() == Blocks.STONE ||
                            world.getBlockState(pos).getBlock() == Blocks.GRASS ||
                            world.getBlockState(pos).getBlock() == Blocks.SAND ||
                            world.getBlockState(pos).getBlock() == Blocks.STONE ||
                            world.getBlockState(pos).getBlock() == Blocks.DIRT) && world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
                        smallSecretHut.generate(world, random, pos.up());
                        flag = true;
                    }
                }
            }
        }
    }

}
