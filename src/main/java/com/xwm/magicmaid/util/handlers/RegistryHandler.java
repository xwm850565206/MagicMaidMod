package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.init.*;
import com.xwm.magicmaid.registry.MagicDimensionRegistry;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import com.xwm.magicmaid.registry.MagicFormulaRegistry;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.interfaces.IHasModel;
import com.xwm.magicmaid.world.gen.StructureChurchPieces;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@Mod.EventBusSubscriber(modid = Reference.MODID)
public class RegistryHandler
{
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
        MagicEquipmentRegistry.registerAllEquipment();
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
        TileEntityHandler.registerTileEntities();
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        OBJLoader.INSTANCE.addDomain(Reference.MODID);

        for (Block block : BlockInit.BLOCKS)
        {
            if (block instanceof IHasModel)
            {
                ((IHasModel)block).registerModels();
            }
        }

        for (Item item : ItemInit.ITEMS)
        {
            if (item instanceof IHasModel)
            {
                ((IHasModel)item).registerModels();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onTextureRegister(TextureStitchEvent.Pre event)
    {
        TextureInit.register(event.getMap());
    }


    public static void initRegisteries()
    {
        SoundsHandler.registerSounds();
        StructureChurchPieces.registerPieces();
        MagicDimensionRegistry.registerAll();
        MagicFormulaRegistry.registerAllFormula();
        OreDictionaryInit.register();
    }

    public static void preInitRegistries()
    {
//        EntityInit.registerEntities();
        BiomeInit.registerBiomes();
        DimensionInit.registerDimensions();
        DimensionInit.registerWorldGenerators();
        PotionInit.registerPotions();
    }

}
