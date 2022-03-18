package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.SPacketPunish;
import com.xwm.magicmaid.store.WorldDifficultyData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Random;

public class PunishOperationHandler
{
    private static final Random RANDOM = new Random();

    private static void kickPlayer(EntityPlayerMP entityPlayer)
    {
        int difficulty = WorldDifficultyData.get(entityPlayer.getEntityWorld()).getWorldDifficulty();
        if (difficulty >= 2) { // 难度2
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
        if (difficulty >= 2) {
            entityPlayer.inventory.clear();
        }
        else if (difficulty == 1) {
            dropInventoryItems(entityPlayer.getEntityWorld(), entityPlayer.getPosition(), entityPlayer.inventory);
            entityPlayer.inventory.clear();
        }
        else {

        }
    }

    //todo
    private static void changeGameMode(EntityPlayerMP entityPlayer) {
        if (entityPlayer.world.isRemote)
            return;
//        FMLCommonHandler.instance().getMinecraftServerInstance().setForceGamemode(true);
//        SPacketPunish packet = new SPacketPunish(4);
//        NetworkLoader.instance.sendTo(packet, entityPlayer);
//        FMLCommonHandler.instance().getMinecraftServerInstance().setGameType(GameType.ADVENTURE);
//        FMLCommonHandler.instance().getMinecraftServerInstance().setForceGamemode(true);
    }

    private static void killPlayer(EntityPlayerMP entityPlayer) {
        int difficulty = WorldDifficultyData.get(entityPlayer.getEntityWorld()).getWorldDifficulty();
        if (difficulty >= 2) {// 难度2击杀玩家
            entityPlayer.setHealth(0);
        }
        else if (difficulty == 1) { // 难度1 掉一滴血
            entityPlayer.setHealth(entityPlayer.getHealth() - 1);
        }
    }

    private static void dropArmor(EntityPlayerMP entityPlayer) {

        for (int i = 0; i < entityPlayer.inventory.armorInventory.size(); ++i)
        {
            ItemStack itemstack = entityPlayer.inventory.armorInventory.get(i);

            if (!itemstack.isEmpty())
            {
                spawnItemStack(entityPlayer.getEntityWorld(), entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, itemstack);
            }
        }

        entityPlayer.inventory.armorInventory.clear();
    }

    private static void dropInv(EntityPlayerMP entityPlayer)
    {
        dropInventoryItems(entityPlayer.getEntityWorld(), entityPlayer.getPosition(), entityPlayer.inventory);
        entityPlayer.inventory.clear();
    }

    public static void punishPlayer(EntityPlayerMP entityPlayer, int punishLevel, String message){
        if ((punishLevel & 1) == 1)
            clearPlayerInv(entityPlayer);
        if ((punishLevel & 2) == 2)
            kickPlayer(entityPlayer);
        if ((punishLevel & 4) == 4)
            changeGameMode(entityPlayer);
        if ((punishLevel & 8) == 8)
            killPlayer(entityPlayer);
        if ((punishLevel & 16) == 16)
            dropArmor(entityPlayer);
        if ((punishLevel & 32) == 32)
            dropInv(entityPlayer);

        int difficulty = WorldDifficultyData.get(entityPlayer.getEntityWorld()).getWorldDifficulty();
        if (message != null && !message.equals(""))
            entityPlayer.sendMessage(new TextComponentString("当前难度:" + difficulty + " 触发惩罚: " + message + ",惩罚将根据难度变动。"));
    }

    public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory inventory)
    {
        dropInventoryItems(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), inventory);
    }

    public static void dropInventoryItems(World worldIn, Entity entityAt, IInventory inventory)
    {
        dropInventoryItems(worldIn, entityAt.posX, entityAt.posY, entityAt.posZ, inventory);
    }

    private static void dropInventoryItems(World worldIn, double x, double y, double z, IInventory inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }

    public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
    {
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (!stack.isEmpty())
        {
            EntityItem entityitem = new EntityItem(worldIn, x + (double)f, y + (double)f1, z + (double)f2, stack.splitStack(RANDOM.nextInt(21) + 10));
            float f3 = 0.05F;
            entityitem.motionX = RANDOM.nextGaussian() * 0.05000000074505806D;
            entityitem.motionY = RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
            entityitem.motionZ = RANDOM.nextGaussian() * 0.05000000074505806D;
            entityitem.setPickupDelay(40);
            worldIn.spawnEntity(entityitem);
        }
    }
}
