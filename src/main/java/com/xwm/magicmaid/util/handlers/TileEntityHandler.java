package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.object.tileentity.TileEntityChurchPortal;
import com.xwm.magicmaid.object.tileentity.TileEntityMagicCircle;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler
{
    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityMagicCircle.class, new ResourceLocation(Reference.MODID, "magic_circle"));
        GameRegistry.registerTileEntity(TileEntityChurchPortal.class, new ResourceLocation(Reference.MODID, "church_portal"));
    }
}
