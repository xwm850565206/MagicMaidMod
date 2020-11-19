package com.xwm.magicmaid.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.StandardCharsets;

public class SyncEntityDataPacket implements IMessage {
    public int id;
    public int dimension;
    public int type;
    public int value;
    public int length;
    public String name;

    public SyncEntityDataPacket() {

    }

    public SyncEntityDataPacket(int id, int dimension, int type, int value, String name) {
        this.id = id;
        this.dimension = dimension;
        this.type = type;
        this.value = value;
        this.name = name;
    }


    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
        this.dimension = buf.readInt();
        this.type = buf.readInt();
        this.value = buf.readInt();
        this.length = buf.readInt();
        this.name = String.valueOf(buf.readCharSequence(length, StandardCharsets.UTF_8));
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeInt(this.dimension);
        buf.writeInt(this.type);
        buf.writeInt(this.value);
        buf.writeInt(this.name.length());
        buf.writeCharSequence(this.name, StandardCharsets.UTF_8);
    }

    public static class Handler implements IMessageHandler<SyncEntityDataPacket, IMessage> {
        @Override
        public IMessage onMessage(SyncEntityDataPacket message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;

            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            Entity entity = world.getEntityByID(message.id);
            if (entity == null)
                return null;

            switch (message.type) {
                case 0:
                    entity.getEntityData().setBoolean(message.name, message.value == 1);
                    break;
            }

            return null;
        }
    }
}