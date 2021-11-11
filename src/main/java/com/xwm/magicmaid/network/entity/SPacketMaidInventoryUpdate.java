package com.xwm.magicmaid.network.entity;

import com.xwm.magicmaid.entity.mob.basic.EntityEquipmentCreature;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class SPacketMaidInventoryUpdate implements IMessage
{
    private int entityID;
    private int dimension;
    private int slot;
    private ItemStack stack;
    private PacketBuffer packetBuffer;

    public SPacketMaidInventoryUpdate() {
    }

    public SPacketMaidInventoryUpdate(int entityID, int dimension, int slot, ItemStack stack) {
        this.entityID = entityID;
        this.dimension = dimension;
        this.slot = slot;
        this.stack = stack;
    }

    public static class Handler implements IMessageHandler<SPacketMaidInventoryUpdate, IMessage> {

        @Override
        public IMessage onMessage(SPacketMaidInventoryUpdate message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT)
                return null;

            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            Entity entity = world.getEntityByID(message.entityID);
            if (entity instanceof EntityEquipmentCreature) {
                ((EntityEquipmentCreature) entity).setInventorySlotContents(message.slot, message.stack);
            }
            return null;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        packetBuffer = new PacketBuffer(buf);
        dimension = packetBuffer.readInt();
        entityID = packetBuffer.readInt();
        slot = packetBuffer.readInt();
        try {
            stack = packetBuffer.readItemStack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeInt(dimension);
        packetBuffer.writeInt(entityID);
        packetBuffer.writeInt(slot);
        packetBuffer.writeItemStack(stack);
    }

}
