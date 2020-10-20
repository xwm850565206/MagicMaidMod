package com.xwm.magicmaid.proxy;

import com.xwm.magicmaid.event.EventLoader;
import com.xwm.magicmaid.event.EventRenderLoader;
import com.xwm.magicmaid.network.NetworkLoader;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{

    public void preInit(FMLPreInitializationEvent event)
    {
       new NetworkLoader(event);
    }

    public void init(FMLInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new EventLoader());
        MinecraftForge.EVENT_BUS.register(new EventRenderLoader());
    }
    public void postInit(FMLPostInitializationEvent event){}

    public void registerItemRenderer(Item item, int meta, String id)
    {

    }
}
