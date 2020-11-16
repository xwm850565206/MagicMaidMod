package com.xwm.magicmaid.world.gen;

import com.xwm.magicmaid.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenFlowers implements IWorldGenerator
{
    private static int[][] next = new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    /**
     * Instantiates a new world gen flowers cloud.
     */
    public WorldGenFlowers()
    {
    }

    /**
     * Generate some world
     *
     * @param random         the chunk specific {@link Random}.
     * @param chunkX         the chunk X coordinate of this chunk.
     * @param chunkZ         the chunk Z coordinate of this chunk.
     * @param world          : additionalData[0] The minecraft {@link World} we're generating for.
     * @param chunkGenerator : additionalData[1] The {@link IChunkProvider} that is generating.
     * @param chunkProvider  : additionalData[2] {@link IChunkProvider} that is requesting the world generation.
     */
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        switch(world.provider.getDimension())
        {
            case 1:
                break;
            case 0:
                break;
            case -1:
                generateInNether(random, chunkX, chunkZ, world);
        }
    }

    private void generateInNether(Random random, int chunkX, int chunkZ, World world)
    {
        Block flower = BlockInit.BLOCK_BIAN_FLOWER;
        BlockPos position = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; ++j)
            {
                BlockPos blockpos = position.add(random.nextInt(15), 20 + random.nextInt(30), random.nextInt(15));

                if (world.isAirBlock(blockpos))
                {
                    for (int k = 0; k < 4; k++) {
                        int x = next[k][0];
                        int z = next[k][1];
                        blockpos = blockpos.add(x, 0, z);
                        if (world.getBlockState(blockpos.down()).isSideSolid(world, blockpos, EnumFacing.UP) || world.getBlockState(blockpos.down()).getBlock() instanceof BlockLiquid)
                            world.setBlockState(blockpos, flower.getDefaultState(), 3);
                    }
                }
//                else
//                {
//                    blockpos = world.getHeight(blockpos);
//                    if (world.isAirBlock(blockpos))
//                    {
//                        if (world.getBlockState(blockpos.down()).isSideSolid(world, blockpos, EnumFacing.UP) || world.getBlockState(blockpos.down()).getBlock() instanceof BlockLiquid)
//                            world.setBlockState(blockpos, flower.getDefaultState(), 3);
//                    }
//                }
            }
        }
    }
}
