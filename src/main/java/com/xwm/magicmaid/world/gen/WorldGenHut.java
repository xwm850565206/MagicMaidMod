package com.xwm.magicmaid.world.gen;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenHut implements IWorldGenerator
{
    private static WorldGenerator plantSecretHut = new WorldGeneratorSkillHut("hut/plant_secret_hut", "perform", "normal");
    private static WorldGenerator forestSecretHut = new WorldGeneratorSkillHut("hut/forest_secret_hut", "perform", "normal");

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch(world.provider.getDimension())
        {
            case 1:
                break;
            case 0:
//                generatePlantSecretHut(random, chunkX, chunkZ, world);
                generateForestSecretHut(random, chunkX, chunkZ, world);
                break;
            case -1:
                break;
        }
    }

    public void generatePlantSecretHut(Random random, int chunkX, int chunkZ, World world)
    {
        int minHeight = 0;
        int maxHeight = 60;
        boolean flag = false;
        BlockPos position = new BlockPos(chunkX * 16, minHeight + random.nextInt(maxHeight - minHeight), chunkZ * 16);

        for (int i = 0; i < 16 && !flag; i ++)
        {
            for (int j = 0; j < 16 && !flag; j++)
            {
                BlockPos pos = position.add(i, 0, j);
                if (world.getBiome(position) == Biomes.PLAINS && (world.getBlockState(pos).getBlock() instanceof BlockDirt || world.getBlockState(pos).getBlock() instanceof BlockStone)) {
                    plantSecretHut.generate(world, random, pos);
                    flag = true;
                }
            }
        }
    }

    public void generateForestSecretHut(Random random, int chunkX, int chunkZ, World world)
    {
        int minHeight = 0;
        int maxHeight = 60;
        boolean flag = false;
        BlockPos position = new BlockPos(chunkX * 16, minHeight + random.nextInt(maxHeight - minHeight), chunkZ * 16);
        for (int i = 0; i < 16 && !flag; i ++)
        {
            for (int j = 0; j < 16 && !flag; j++)
            {
                BlockPos pos = position.add(i, 0, j);
                if (world.getBlockState(pos).getBlock() instanceof BlockGrass) {
                    forestSecretHut.generate(world, random, pos);
                    flag = true;
                }
            }
        }
    }
}
