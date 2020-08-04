package com.xwm.magicmaid.world.gen;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.*;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.List;
import java.util.Random;

public class StructureChurchPieces
{
    private static final PlacementSettings OVERWRITE = (new PlacementSettings()).setIgnoreEntities(true);
    private static final PlacementSettings INSERT = (new PlacementSettings()).setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);

    public static void registerPieces()
    {
        MapGenStructureIO.registerStructureComponent(StructureChurchPieces.ChurchTemplate.class, "RuinChurch");
    }

    public static void startChurch(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructureComponent> componentList, Random random)
    {

    }

    private static boolean recursiveChildren(TemplateManager templateManager, StructureChurchPieces.IGenerator generator, int prePiece, StructureChurchPieces.ChurchTemplate churchTemplate, BlockPos pos, List<StructureComponent> componentList, Random random)
    {
        return false;
    }



    public static class ChurchTemplate extends StructureComponentTemplate
    {

        private String pieceName;
        private Rotation rotation;
        /** Whether this template should overwrite existing blocks. Replaces only air if false. */
        private boolean overwrite;

        public ChurchTemplate()
        {
        }

        public ChurchTemplate(TemplateManager templateManager, String pieceName, BlockPos blockPos, Rotation rotation, boolean overwriteIn)
        {
            super(0);
            this.pieceName = pieceName;
            this.templatePosition = blockPos;
            this.rotation = rotation;
            this.overwrite = overwriteIn;
            this.loadTemplate(templateManager);
        }

        private void loadTemplate(TemplateManager templateManager)
        {
            Template template = templateManager.getTemplate((MinecraftServer)null, new ResourceLocation("church/" + this.pieceName));
            PlacementSettings placementsettings = (this.overwrite ? StructureChurchPieces.OVERWRITE : StructureChurchPieces.INSERT).copy().setRotation(this.rotation);
            this.setup(template, this.templatePosition, placementsettings);
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setString("Template", this.pieceName);
            tagCompound.setString("Rot", this.rotation.name());
            tagCompound.setBoolean("OW", this.overwrite);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager)
        {
            super.readStructureFromNBT(tagCompound, templateManager);
            this.pieceName = tagCompound.getString("Template");
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.overwrite = tagCompound.getBoolean("OW");
            this.loadTemplate(templateManager);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb) {
            if (function.startsWith("Chest"))
            {
                BlockPos blockpos = pos.down();

                if (sbb.isVecInside(blockpos))
                {
                    TileEntity tileentity = worldIn.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityChest)
                    {
                        ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, rand.nextLong());
                    }
                }
            }
            else if (function.startsWith("Sentry"))
            {
                EntityShulker entityshulker = new EntityShulker(worldIn);
                entityshulker.setPosition((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D);
                entityshulker.setAttachmentPos(pos);
                worldIn.spawnEntity(entityshulker);
            }
            else if (function.startsWith("Elytra"))
            {
                EntityItemFrame entityitemframe = new EntityItemFrame(worldIn, pos, this.rotation.rotate(EnumFacing.SOUTH));
                entityitemframe.setDisplayedItem(new ItemStack(Items.ELYTRA));
                worldIn.spawnEntity(entityitemframe);
            }
        }
    }

    interface IGenerator
    {
        void init();

        boolean generate(TemplateManager templateManager, int prePiece, StructureChurchPieces.ChurchTemplate churchTemplate, BlockPos pos, List<StructureComponent> componentList, Random random);
    }
}
