package com.xwm.magicmaid.proxy;

import com.xwm.magicmaid.achievement.AchievementLoader;
import com.xwm.magicmaid.command.CommandLoader;
import com.xwm.magicmaid.event.CommonEventLoader;
import com.xwm.magicmaid.network.NetworkLoader;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy
{

    public void preInit(FMLPreInitializationEvent event)
    {
       new NetworkLoader(event);
    }

    public void init(FMLInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new CommonEventLoader());
        new AchievementLoader();
    }
    public void postInit(FMLPostInitializationEvent event){}

    public void registerItemRenderer(Item item, int meta, String id) {

    }

    public void registerOBJRenderer(Item item, int meta, String id) {

    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        new CommandLoader(event);
    }
}
