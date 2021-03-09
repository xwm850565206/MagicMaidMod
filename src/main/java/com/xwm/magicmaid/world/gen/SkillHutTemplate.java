package com.xwm.magicmaid.world.gen;

import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.object.item.ItemSkillBook;
import com.xwm.magicmaid.player.skill.ISkill;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.Random;

public class SkillHutTemplate extends StructureComponentTemplate
{
    private List<ISkill> skillList;

    public SkillHutTemplate(List<ISkill> skillList, Template template, BlockPos pos, PlacementSettings settings) {
        this(0, skillList, template, pos, settings);

    }

    public SkillHutTemplate(int type,  List<ISkill> skillList, Template template, BlockPos pos, PlacementSettings settings) {
        super(type);
        this.skillList = skillList;
        this.setup(template, pos, settings);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb) {
        if (function.startsWith("skill_chest")) {
            String facing = function.replace("skill_chest_", "");
            worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, this.placeSettings.getRotation().rotate(EnumFacing.valueOf(facing.toUpperCase()))));
            TileEntityChest chest = (TileEntityChest) worldIn.getTileEntity(pos);
            IItemHandler handler = chest.getSingleChestHandler();
            ISkill skill = skillList.get(rand.nextInt(skillList.size()));;
            ItemStack skillBook = new ItemStack(ItemInit.SKILL_BOOK);
            ItemSkillBook.setSkill(skillBook, skill);
            handler.insertItem(rand.nextInt(27), skillBook, worldIn.isRemote);
        }
    }
}
