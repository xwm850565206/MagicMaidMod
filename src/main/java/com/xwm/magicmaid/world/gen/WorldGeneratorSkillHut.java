package com.xwm.magicmaid.world.gen;

import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.registry.MagicSkillRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGeneratorSkillHut extends WorldGenerator
{
    private String structureName;
    private String catalog;
    private String rarity;

    public WorldGeneratorSkillHut(String name, String catalog, String rarity)
    {
        this.structureName = name;
        this.catalog = catalog;
        this.rarity = rarity;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        generateStructure(worldIn, rand, position);
        return true;
    }

    public void generateStructure(World world, Random random, BlockPos pos)
    {
        WorldServer worldServer = (WorldServer) world;
        TemplateManager manager = worldServer.getStructureTemplateManager();
        ResourceLocation location = new ResourceLocation(Reference.MODID , structureName);
        Template template = manager.get(worldServer.getMinecraftServer(), location);

        if (template != null)
        {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);

            PlacementSettings settings = new PlacementSettings().setMirror(Mirror.NONE).
                    setIgnoreEntities(false).
                    setRotation(Rotation.values()[random.nextInt(4)]).
                    setChunk(world.getChunkFromBlockCoords(pos).getPos());

            List<ISkill> skillList = getSkillList();

            SkillHutTemplate hutTemplate = new SkillHutTemplate(skillList, template, pos, settings);
            hutTemplate.addComponentParts(world, random, new StructureBoundingBox(pos.getX(), pos.getZ(), pos.getX() + template.getSize().getX(), pos.getZ() + template.getSize().getZ()));
        }
    }

    private List<ISkill> getSkillList()
    {
        List<ISkill> skillList = new ArrayList<ISkill>() {{
            for (Class<? extends ISkill> skillClazz : MagicSkillRegistry.SKILL_MAP.values())
            {
                try {
                    ISkill skill = skillClazz.newInstance();
                    String[] describeElements = skill.getName().split("\\.");
                    if (describeElements.length < 3)
                        continue;
                    if (describeElements[0].equals(catalog) && describeElements[1].equals(rarity))
                        this.add(skill);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }};

        return skillList;
    }
}
