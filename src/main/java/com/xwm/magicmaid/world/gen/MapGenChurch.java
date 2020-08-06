package com.xwm.magicmaid.world.gen;

import com.xwm.magicmaid.world.dimension.ChunkGeneratorChurch;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureEndCityPieces;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nullable;
import java.util.Random;

public class MapGenChurch extends MapGenStructure
{
    private final ChunkGeneratorChurch churchProvider;
    public MapGenChurch(ChunkGeneratorChurch chunkGeneratorChurch)
    {
        this.churchProvider = chunkGeneratorChurch;
    }

    public String getStructureName()
    {
        return "RuinChurch";
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, 6, 5, 10387313, true, 100, findUnexplored);
        //todo
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        if (chunkX == 2 && chunkZ == 2)
            return true;
        else
            return false;
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenChurch.Start(this.world, this.churchProvider, this.rand, chunkX, chunkZ);
    }

    public static class Start extends StructureStart
    {
        private boolean isSizeable;

        public Start()
        {
        }

        public Start(World worldIn, ChunkGeneratorChurch chunkProvider, Random random, int chunkX, int chunkZ)
        {
            super(chunkX, chunkZ);
            this.create(worldIn, chunkProvider, random, chunkX, chunkZ);
        }

        private void create(World worldIn, ChunkGeneratorChurch chunkProvider, Random rnd, int chunkX, int chunkZ)
        {
            Random random = new Random((long)(chunkX + chunkZ * 10387313));
            Rotation rotation = Rotation.NONE;

            BlockPos blockpos = new BlockPos(chunkX * 16 + 8, 55, chunkZ * 16 + 8);
            StructureChurchPieces.startChurch(worldIn.getSaveHandler().getStructureTemplateManager(), blockpos, rotation, this.components, rnd);
            this.updateBoundingBox();
            this.isSizeable = true;

        }

        /**
         * currently only defined for Villages, returns true if Village has more than 2 non-road components
         */
        public boolean isSizeableStructure()
        {
            return this.isSizeable;
        }
    }
}
