package com.xwm.magicmaid.network;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.handlers.GuiHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class NetworkLoader
{
    public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    private static int nextID = 0;

    public NetworkLoader(FMLPreInitializationEvent event)
    {
        registerMessage(ParticlePacket.Handler.class, ParticlePacket.class, Side.CLIENT);
        registerMessage(CustomerParticlePacket.Handler.class, CustomerParticlePacket.class, Side.CLIENT);
        registerMessage(MaidModePacket.Handler.class, MaidModePacket.class, Side.SERVER);

        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, GuiHandler.maidWindowHandler);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
    {
        instance.registerMessage(messageHandler, requestMessageType, nextID++, side);
    }
}
