package com.xwm.magicmaid.network;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.network.entity.*;
import com.xwm.magicmaid.network.gui.CPacketOpenGui;
import com.xwm.magicmaid.network.particle.SPacketNineParamParticle;
import com.xwm.magicmaid.network.particle.SPacketParticle;
import com.xwm.magicmaid.network.particle.SPacketSixParamParticle;
import com.xwm.magicmaid.network.particle.SPacketThreeParamParticle;
import com.xwm.magicmaid.network.skill.CPacketLearnSkill;
import com.xwm.magicmaid.network.skill.CPacketSkill;
import com.xwm.magicmaid.network.skill.CPacketSkillPoint;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.handlers.GuiHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


public class NetworkLoader
{
    public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    private static int nextID = 0;

    public NetworkLoader(FMLPreInitializationEvent event)
    {
        registerMessage(SPacketParticle.Handler.class, SPacketParticle.class, Side.CLIENT);
        registerMessage(SPacketThreeParamParticle.Handler.class, SPacketThreeParamParticle.class, Side.CLIENT);
        registerMessage(SPacketSixParamParticle.Handler.class, SPacketSixParamParticle.class, Side.CLIENT);
        registerMessage(SPacketNineParamParticle.Handler.class, SPacketNineParamParticle.class, Side.CLIENT);
        registerMessage(SPacketSound.Handler.class, SPacketSound.class, Side.CLIENT);
        registerMessage(SPacketVelocity.Handler.class, SPacketVelocity.class, Side.CLIENT);
        registerMessage(SPacketPunish.Handler.class, SPacketPunish.class, Side.CLIENT);
        registerMessage(InfoLogginPacket.Handler.class, InfoLogginPacket.class, Side.CLIENT);
        registerMessage(SPacketEntityData.Handler.class, SPacketEntityData.class, Side.CLIENT);
        registerMessage(RenderAreaPacket.Handler.class, RenderAreaPacket.class, Side.CLIENT);
        registerMessage(SPacketCapabilityUpdate.Handler.class, SPacketCapabilityUpdate.class, Side.CLIENT);
        registerMessage(SPacketMaidInventoryUpdate.Handler.class, SPacketMaidInventoryUpdate.class, Side.CLIENT);
        registerMessage(SPacketUpdateDifficult.Handler.class, SPacketUpdateDifficult.class, Side.CLIENT);


        registerMessage(CPacketEntityData.Handler.class, CPacketEntityData.class, Side.SERVER);
        registerMessage(AddBookPacket.Handler.class, AddBookPacket.class, Side.SERVER);
        registerMessage(CPacketMaidMode.Handler.class, CPacketMaidMode.class, Side.SERVER);
        registerMessage(CPacketOpenGui.Handler.class, CPacketOpenGui.class, Side.SERVER);
        registerMessage(CPacketSkill.Handler.class, CPacketSkill.class, Side.SERVER);
        registerMessage(CPacketSkillPoint.Handler.class, CPacketSkillPoint.class, Side.SERVER);
        registerMessage(CPacketCapabilityUpdate.Handler.class, CPacketCapabilityUpdate.class, Side.SERVER);
        registerMessage(CPacketChangeDifficulty.Handler.class, CPacketChangeDifficulty.class, Side.SERVER);
        registerMessage(CPacketLearnSkill.Handler.class, CPacketLearnSkill.class, Side.SERVER);

        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, GuiHandler.maidWindowHandler);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
    {
        instance.registerMessage(messageHandler, requestMessageType, nextID++, side);
    }
}
