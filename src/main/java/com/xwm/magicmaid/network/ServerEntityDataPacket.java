package com.xwm.magicmaid.network;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityEquipmentCreature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class ServerEntityDataPacket implements IMessage {
    public int id;
    public int dimension;
    public int type;
    public String data;
    public int dataLength;
    public String name;
    public int nameLength;

    public NBTTagCompound compound;
    public PacketBuffer packetBuffer;

    public ServerEntityDataPacket() {

    }

    public ServerEntityDataPacket(int id, int dimension, int type, String data, String name) {
        this.id = id;
        this.dimension = dimension;
        this.type = type;
        this.data = data;
        this.name = name;
        this.packetBuffer = new PacketBuffer(Unpooled.buffer());
        this.compound = new NBTTagCompound();
    }

    public ServerEntityDataPacket(int id, int dimension, int type, NBTTagCompound compound)
    {
        this(id, dimension, type, "", "");
        this.compound = compound;
    }


    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {

        int i = buf.readableBytes();
        if (i >= 0 && i <= 32767)
        {
            this.packetBuffer = new PacketBuffer(buf.readBytes(i));
        }

        this.id = packetBuffer.readInt();
        this.dimension = packetBuffer.readInt();
        this.type = packetBuffer.readInt();
        this.data = packetBuffer.readString(32767);
        this.name = packetBuffer.readString(32767);
        try {
            this.compound = packetBuffer.readCompoundTag();
        } catch (IOException e) {
            ;
        }

//        this.dataLength = buf.readInt();
//        this.data = String.valueOf(buf.readCharSequence(dataLength, StandardCharsets.UTF_8));
//
//        this.nameLength = buf.readInt();
//        this.name = String.valueOf(buf.readCharSequence(nameLength, StandardCharsets.UTF_8));
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
//        buf.writeInt(this.id);
//        buf.writeInt(this.dimension);
//        buf.writeInt(this.type);
//        buf.writeInt(this.data.length());
//        buf.writeCharSequence(this.data, StandardCharsets.UTF_8);
//        buf.writeInt(this.name.length());
//        buf.writeCharSequence(this.name, StandardCharsets.UTF_8);
        this.packetBuffer.writeInt(this.id);
        this.packetBuffer.writeInt(this.dimension);
        this.packetBuffer.writeInt(this.type);
        this.packetBuffer.writeString(this.data);
        this.packetBuffer.writeString(this.name);
        this.packetBuffer.writeCompoundTag(this.compound);
        synchronized(this.data) { //This may be access multiple times, from multiple threads, lets be safe.
            this.packetBuffer.markReaderIndex();
            buf.writeBytes(this.packetBuffer);
            this.packetBuffer.resetReaderIndex();
        }
    }

    public static class Handler implements IMessageHandler<ServerEntityDataPacket, IMessage> {
        @Override
        public IMessage onMessage(ServerEntityDataPacket message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;

            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            Entity entity = world.getEntityByID(message.id);
            if (entity == null)
                return null;

            try {
                //更新nbt tag
                switch (message.type) {
                    case 0:
                        entity.getEntityData().setBoolean(message.name, Boolean.valueOf(message.data));
                        break;
                    case 1:
                        entity.getEntityData().setInteger(message.name, Integer.valueOf(message.data));
                        break;
                    case 2:
                        entity.getEntityData().setDouble(message.name, Double.valueOf(message.data));
                        break;
                    case 3:
                        entity.getEntityData().setString(message.name, String.valueOf(message.data));
                        break;
                }

                //更新其他的
                switch (message.type) {
                    case 4:
                        if (entity instanceof IEntityEquipmentCreature) {
                            NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(((IEntityEquipmentCreature) entity).getSizeInventory(), ItemStack.EMPTY);
                            ItemStackHelper.loadAllItems(message.compound, inventory);
                            ((IEntityEquipmentCreature) entity).setInventory(inventory);
                        }
                }
            } catch (Exception e)
            {
                System.out.println("entity data update failed, maybe formatter error");
            }

            return null;
        }
    }
}