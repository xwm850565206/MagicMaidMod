package com.xwm.magicmaid.network;

import com.xwm.magicmaid.event.EventLoader;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class InfoLogginPacket implements IMessage
{
    private boolean messageValid;
    private int id;

    public InfoLogginPacket() {
        this.messageValid = false;
    }

    public InfoLogginPacket(int id) {
        this.id = id;
        this.messageValid = true;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
    }

    public static class Handler implements IMessageHandler<InfoLogginPacket, IMessage>
    {
        @Override
        public IMessage onMessage(InfoLogginPacket message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT)
                return null;

            processMessage(message, ctx);
            return null;
        }

        private void processMessage(InfoLogginPacket message, MessageContext ctx)
        {

        }
    }
}
