package com.xwm.magicmaid.network.gui;

import com.xwm.magicmaid.Main;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CPacketOpenGui implements IMessage {
    private int playerID;
    private int worldID;
    private int guiID;
    private int x, y, z;

    public CPacketOpenGui()
    {

    }

    public CPacketOpenGui(int playerID, int guiID, int worldID, int x, int y, int z) {
        this.playerID = playerID;
        this.guiID = guiID;
        this.worldID = worldID;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        playerID = buf.readInt();
        worldID = buf.readInt();
        guiID = buf.readInt();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(playerID);
        buf.writeInt(worldID);
        buf.writeInt(guiID);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public static class Handler implements IMessageHandler<CPacketOpenGui, IMessage> {

        @Override
        public IMessage onMessage(CPacketOpenGui message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        private void processMessage(CPacketOpenGui message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.worldID);
            Entity entityPlayer = world.getEntityByID(message.playerID);
            if (entityPlayer instanceof EntityPlayer)
            {
                ((EntityPlayer) entityPlayer).openGui(Main.instance, message.guiID, world, message.getX(), message.getY(), message.getZ());
            }
        }
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getWorldID() {
        return worldID;
    }

    public void setWorldID(int worldID) {
        this.worldID = worldID;
    }

    public int getGuiID() {
        return guiID;
    }

    public void setGuiID(int guiID) {
        this.guiID = guiID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
