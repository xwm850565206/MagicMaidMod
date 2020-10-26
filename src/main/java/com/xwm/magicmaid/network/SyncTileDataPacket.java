package com.xwm.magicmaid.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;

public class SyncTileDataPacket implements IMessage
{
    private BlockPos pos;
    private NBTTagCompound compound;

    public SyncTileDataPacket(BlockPos pos, NBTTagCompound compound)
    {
        this.pos = pos;
        this.compound = compound;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        try {
            this.pos = buffer.readBlockPos();
            this.compound = buffer.readCompoundTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void toBytes(ByteBuf buf) {
//        PacketBuffer buffer = new PacketBuffer(buf);
//        buffer.wr
    }
}
