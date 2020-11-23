package com.xwm.magicmaid.world.gen;

import com.xwm.magicmaid.init.BlockInit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenHolyStone implements IWorldGenerator
{
    private WorldGenerator holyStoneGenerator;

    public WorldGenHolyStone() {
        this.holyStoneGenerator = new WorldGenMinable(BlockInit.BLOCK_HOLY_STONE.getDefaultState(), 5);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        int dimension = world.provider.getDimension();
        if (dimension == -1 || dimension == 1)
            return; //不在地狱和末地生成

        int minHeight = 3;
        int maxHeight = 60;
        int heightDiff = maxHeight - minHeight;
        for (int i = 0; i < 10; i++)
        {
            int x = chunkX * 16 + random.nextInt(16);
            int y = minHeight + random.nextInt(heightDiff);
            int z = chunkZ * 16 + random.nextInt(16);

            holyStoneGenerator.generate(world, random, new BlockPos(x, y, z));
        }
    }
}
