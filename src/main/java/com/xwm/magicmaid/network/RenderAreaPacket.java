package com.xwm.magicmaid.network;

import com.xwm.magicmaid.registry.MagicRenderRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RenderAreaPacket implements IMessage
{
    public int id;
    public int type;
    public AxisAlignedBB bb;

    public RenderAreaPacket() {

    }

    public RenderAreaPacket(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public RenderAreaPacket(int id, int type, AxisAlignedBB bb) {
        this.id = id;
        this.type = type;
        this.bb = bb;
    }


    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
        this.type = buf.readInt();
        if (type == 0) //0-添加区域 1-删除区域
            this.bb = new AxisAlignedBB(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeInt(this.type);
        if(this.type == 0) {
            buf.writeDouble(this.bb.minX);
            buf.writeDouble(this.bb.minY);
            buf.writeDouble(this.bb.minZ);
            buf.writeDouble(this.bb.maxX);
            buf.writeDouble(this.bb.maxY);
            buf.writeDouble(this.bb.maxZ);
        }

    }

    public static class Handler implements IMessageHandler<RenderAreaPacket, IMessage> {
        @Override
        public IMessage onMessage(RenderAreaPacket message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT)
                return null;

            if (message.type == 0) {
                MagicRenderRegistry.addRenderBox(message.id, message.bb);
            }
            else
                MagicRenderRegistry.removeRenderBox(message.id);

            return null;
        }
    }
}
