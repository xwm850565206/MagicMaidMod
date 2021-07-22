package com.xwm.magicmaid.proxy;

import com.xwm.magicmaid.event.loader.ClientEventLoader;
import com.xwm.magicmaid.key.KeyLoader;
import com.xwm.magicmaid.object.tileentity.TileEntityChurchPortal;
import com.xwm.magicmaid.registry.MagicMenuElementRegistry;
import com.xwm.magicmaid.render.portal.TileEntityChurchPortalRenderer;
import com.xwm.magicmaid.util.handlers.RenderHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
    private ClientEventLoader clientEventLoader;

    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        RenderHandler.registerEntityRenders();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChurchPortal.class, new TileEntityChurchPortalRenderer());
        MagicMenuElementRegistry.registerAll();
        new KeyLoader();
    }

    public void init(FMLInitializationEvent event){
        super.init(event);
        this.clientEventLoader = new ClientEventLoader();
        MinecraftForge.EVENT_BUS.register(clientEventLoader);
    }

    public void postInit(FMLPostInitializationEvent event){
        super.postInit(event);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void registerOBJRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void changeSkillSwitch(){
        if (clientEventLoader != null)
            clientEventLoader.showSkillWidget = !clientEventLoader.showSkillWidget;
    }
}
