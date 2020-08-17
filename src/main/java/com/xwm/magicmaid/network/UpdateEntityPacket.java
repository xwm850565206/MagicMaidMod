package com.xwm.magicmaid.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateEntityPacket implements IMessage {

    public UpdateEntityPacket() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<UpdateEntityPacket, IMessage> {
        @Override
        public IMessage onMessage(UpdateEntityPacket message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        private void processMessage(UpdateEntityPacket message, MessageContext ctx)
        {
            try {
                Minecraft.getMinecraft().world.updateEntities();
            } catch (NullPointerException e){
                ;
            }
        }
    }
}
