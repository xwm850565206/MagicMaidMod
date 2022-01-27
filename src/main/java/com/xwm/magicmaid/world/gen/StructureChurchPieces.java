package com.xwm.magicmaid.world.gen;

import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
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
        MapGenStructureIO.registerStructureComponent(StructureChurchPieces.ChurchTemplate.class, "RuinChurchComponent");
        MapGenStructureIO.registerStructure(MapGenChurch.Start.class, "RuinChurch");
        // todo 暂时写在这

    }

    public static void startChurch(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructureComponent> componentList, Random random)
    {
        StructureChurchPieces.ChurchTemplate structurechurchpieces$churchtemplate =
                addHelper(componentList, new StructureChurchPieces.ChurchTemplate(templateManager, "churchlf", pos, Rotation.NONE, true));
        structurechurchpieces$churchtemplate =
                addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(32, 0, 0), "churchrf", Rotation.NONE, true));
        structurechurchpieces$churchtemplate =
                addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(0, 0, 32), "churchrb", Rotation.NONE, true));
        structurechurchpieces$churchtemplate =
                addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(-32, 0, 0), "churchlb", Rotation.NONE, true));
        structurechurchpieces$churchtemplate =
                addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(0, 32, -32), "churchtlf", Rotation.NONE, true));

        //        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(-12, 0, 0), "churchmf", Rotation.NONE, true));
//        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(0, 32, 0), "churchmt", Rotation.NONE, true));
//        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(-10, -32, 0), "churchrf", Rotation.NONE, true));
//        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(0, 0, 16), "churchrm", Rotation.NONE, true));
//        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(10, 0, 0), "churchmm", Rotation.NONE, true));
//        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(11, 0, 0), "churchlm", Rotation.NONE, true));
//        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(0, 0, 11), "churchlr", Rotation.NONE, true));
//        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(-11, 0, 0), "churchmr", Rotation.NONE, true));
//        structurechurchpieces$churchtemplate =
//                StructureChurchPieces.addHelper(componentList, StructureChurchPieces.addPiece(templateManager, structurechurchpieces$churchtemplate, new BlockPos(-10, 0, 0), "churchrr", Rotation.NONE, true));
    }

    private static StructureChurchPieces.ChurchTemplate addHelper(List<StructureComponent> componentList, StructureChurchPieces.ChurchTemplate template) {
        componentList.add(template);
        return template;
    }

    private static StructureChurchPieces.ChurchTemplate addPiece(TemplateManager templateManager, StructureChurchPieces.ChurchTemplate template, BlockPos pos, String name, Rotation rotation, boolean owerwrite) {
        StructureChurchPieces.ChurchTemplate structureechurchcitypieces$churchtemplate = new StructureChurchPieces.ChurchTemplate(templateManager, name, template.getTemplatePos(), rotation, owerwrite);
//        BlockPos blockpos = template.getTemplate().calculateConnectedPos(template.getPlacementSettings(), pos, structureechurchcitypieces$churchtemplate.getPlacementSettings(), BlockPos.ORIGIN);
        structureechurchcitypieces$churchtemplate.offset(pos.getX(), pos.getY(), pos.getZ());
        return structureechurchcitypieces$churchtemplate;
    }


    public static class ChurchTemplate extends StructureComponentTemplate
    {
        private String pieceName;
        private Rotation rotation;
        /** Whether this template should overwrite existing blocks. Replaces only air if false. */
        private boolean overwrite;

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
            try{
                Template template = templateManager.getTemplate(null, new ResourceLocation(Reference.MODID, "new_church/" + this.pieceName));
                PlacementSettings placementsettings = (this.overwrite ? StructureChurchPieces.OVERWRITE : StructureChurchPieces.INSERT).copy().setRotation(this.rotation);
                this.setup(template, this.templatePosition, placementsettings);
            } catch (NullPointerException e){
                e.printStackTrace();
            }

        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);

            if (pieceName == null) pieceName = "undefined";
            if (rotation == null) rotation = Rotation.NONE;

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
            if (pieceName == null) pieceName = "undefined";
            if  (rotation == null) rotation = Rotation.NONE;

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
//            else if (function.startsWith("Sentry"))
//            {
//                EntityShulker entityshulker = new EntityShulker(worldIn);
//                entityshulker.setPosition((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D);
//                entityshulker.setAttachmentPos(pos);
//                worldIn.spawnEntity(entityshulker);
//            }
//            else if (function.startsWith("Elytra"))
//            {
//                EntityItemFrame entityitemframe = new EntityItemFrame(worldIn, pos, this.rotation.rotate(EnumFacing.SOUTH));
//                entityitemframe.setDisplayedItem(new ItemStack(Items.ELYTRA));
//                worldIn.spawnEntity(entityitemframe);
//            }
            else if (function.startsWith("memoryclock"))
            {
                BlockPos blockpos = pos.down();
                worldIn.setBlockState(blockpos, BlockInit.BLOCK_MEMORY_CLOCK.getDefaultState(), 3);
            }
        }

        public Template getTemplate(){
            return this.template;
        }

        public PlacementSettings getPlacementSettings() {
            return this.placeSettings;
        }

        public BlockPos getTemplatePos(){
            return this.templatePosition;
        }

        public void setComponentType(int componentType){
            this.componentType = componentType;
        }

        public int getComponentType(){
            return this.componentType;
        }
    }
}
