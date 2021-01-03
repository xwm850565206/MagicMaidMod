package com.xwm.magicmaid.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SPacketVelocity implements IMessage {
    private boolean messageValid;
    private int id;
    private float motionX, motionY, motionZ;

    public SPacketVelocity() {
        this.messageValid = false;
    }

    public SPacketVelocity(int id, float motionX, float motionY, float motionZ) {
        this.id = id;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
        this.motionX = buf.readFloat();
        this.motionY = buf.readFloat();
        this.motionZ = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeFloat(this.motionX);
        buf.writeFloat(this.motionY);
        buf.writeFloat(this.motionZ);
    }

    public static class Handler implements IMessageHandler<SPacketVelocity, IMessage> {
        @Override
        public IMessage onMessage(SPacketVelocity message, MessageContext ctx) {
            if (message.messageValid && ctx.side != Side.CLIENT)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        private void processMessage(SPacketVelocity message, MessageContext ctx)
        {
            try {
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.id);
                entity.setVelocity(message.motionX, message.motionY, message.motionZ);
                Minecraft.getMinecraft().world.updateEntity(entity);

            } catch (NullPointerException e){
                ;
            }
        }
    }
}
