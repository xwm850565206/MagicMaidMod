package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.SPacketPunish;
import com.xwm.magicmaid.store.WorldDifficultyData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.text.TextComponentString;

public class PunishOperationHandler
{
    private static void kickPlayer(EntityPlayerMP entityPlayer)
    {
        int difficulty = WorldDifficultyData.get(entityPlayer.getEntityWorld()).getWorldDifficulty();
        if (difficulty == 2) { // 难度2
            SPacketPunish packet = new SPacketPunish(2);
            NetworkLoader.instance.sendTo(packet, entityPlayer);
        }
        else if (difficulty == 1) { // 难度1
            entityPlayer.setDead();
        }
        else { // 难度0
            ;
        }
    }

    private static void clearPlayerInv(EntityPlayerMP entityPlayer)
    {
        int difficulty = WorldDifficultyData.get(entityPlayer.getEntityWorld()).getWorldDifficulty();
        if (difficulty == 2) {
            entityPlayer.inventory.clear();
        }
        else if (difficulty == 1) {
            InventoryHelper.dropInventoryItems(entityPlayer.getEntityWorld(), entityPlayer.getPosition(), entityPlayer.inventory);
        }
        else {

        }
    }

    //todo
    private static void changeGameMode(EntityPlayerMP entityPlayer){
        if (entityPlayer.world.isRemote)
            return;
//        FMLCommonHandler.instance().getMinecraftServerInstance().setForceGamemode(true);
//        SPacketPunish packet = new SPacketPunish(4);
//        NetworkLoader.instance.sendTo(packet, entityPlayer);
//        FMLCommonHandler.instance().getMinecraftServerInstance().setGameType(GameType.ADVENTURE);
//        FMLCommonHandler.instance().getMinecraftServerInstance().setForceGamemode(true);
    }

    public static void punishPlayer(EntityPlayerMP entityPlayer, int punishLevel, String message){
        if ((punishLevel & 1) == 1)
            clearPlayerInv(entityPlayer);
        if ((punishLevel & 2) == 2)
            kickPlayer(entityPlayer);
        if ((punishLevel & 4) == 4)
            changeGameMode(entityPlayer);

        int difficulty = WorldDifficultyData.get(entityPlayer.getEntityWorld()).getWorldDifficulty();
        if (message != null && !message.equals(""))
            entityPlayer.sendMessage(new TextComponentString("当前难度:" + difficulty + " 触发惩罚: " + message));
    }
}
