package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.PunishPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Iterator;

public class PunishOperationHandler
{
    private static void kickPlayer(EntityPlayerMP entityPlayer)
    {
        PunishPacket packet = new PunishPacket(2);
        NetworkLoader.instance.sendTo(packet, entityPlayer);
    }

    private static void clearPlayerInv(EntityPlayerMP entityPlayer)
    {
        entityPlayer.inventory.clear();
    }

    //todo
    private static void changeGameMode(EntityPlayerMP entityPlayer){
        if (entityPlayer.world.isRemote)
            return;
//        PunishPacket packet = new PunishPacket(4);
//        NetworkLoader.instance.sendTo(packet, entityPlayer);
//        FMLCommonHandler.instance().getMinecraftServerInstance().setGameType(GameType.ADVENTURE);
//        FMLCommonHandler.instance().getMinecraftServerInstance().setForceGamemode(true);
    }

    public static void punishPlayer(EntityPlayerMP entityPlayer, int punishLevel){
        if ((punishLevel & 1) == 1)
            clearPlayerInv(entityPlayer);
        if ((punishLevel & 2) == 2)
            kickPlayer(entityPlayer);
        if ((punishLevel & 4) == 4)
            changeGameMode(entityPlayer);
    }
}
