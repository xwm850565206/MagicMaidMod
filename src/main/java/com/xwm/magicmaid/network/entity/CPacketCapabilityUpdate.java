package com.xwm.magicmaid.network.entity;

import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ICreatureCapability;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class CPacketCapabilityUpdate implements IMessage
{
    private NBTTagCompound compound;
    private int dimension;
    private int entityID;
    private int type; // 0-skillCapability 1-magicCreatureCapability
    private PacketBuffer packetBuffer;

    public CPacketCapabilityUpdate() {
    }

    public CPacketCapabilityUpdate(NBTTagCompound compound, int dimension, int entityID, int type) {
        this.compound = compound;
        this.dimension = dimension;
        this.entityID = entityID;
        this.type = type;
    }

    public static class Handler implements IMessageHandler<CPacketCapabilityUpdate, IMessage> {
        @Override
        public IMessage onMessage(CPacketCapabilityUpdate message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        private void processMessage(CPacketCapabilityUpdate message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            Entity entity = world.getEntityByID(message.entityID);
            try {
                switch (message.type) {
                    case 0:
                        ISkillCapability skillCapability = entity.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                        CapabilityLoader.SKILL_CAPABILITY.getStorage().readNBT(CapabilityLoader.SKILL_CAPABILITY, skillCapability, null, message.compound);
                        break;
                    case 1:
                        ICreatureCapability creatureCapability = entity.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
                        CapabilityLoader.CREATURE_CAPABILITY.getStorage().readNBT(CapabilityLoader.CREATURE_CAPABILITY, creatureCapability, null, message.compound);
                        break;
                }
            } catch (NullPointerException e) {
                ;
            }

        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        packetBuffer = new PacketBuffer(buf);
        try {
            compound = packetBuffer.readCompoundTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimension = packetBuffer.readInt();
        entityID = packetBuffer.readInt();
        type = packetBuffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeCompoundTag(compound);
        packetBuffer.writeInt(dimension);
        packetBuffer.writeInt(entityID);
        packetBuffer.writeInt(type);
    }


}
