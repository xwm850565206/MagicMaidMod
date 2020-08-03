package com.xwm.magicmaid.world.gen;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class WorldGenStructure extends WorldGenerator
{
    public static String structureName;

    public WorldGenStructure(String name)
    {
        this.structureName = name;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        generateStructure(worldIn, position);
        return true;
    }

    public static void generateStructure(World world, BlockPos pos)
    {
        WorldServer worldServer = (WorldServer) world;
        TemplateManager manager = worldServer.getStructureTemplateManager();
        ResourceLocation location = new ResourceLocation(Reference.MODID , structureName);
        Template template = manager.get(worldServer.getMinecraftServer(), location);

        if (template != null)
        {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);

            PlacementSettings settings = new PlacementSettings().setMirror(Mirror.NONE);
            template.addBlocksToWorldChunk(world, pos, settings);
        }

    }
}
