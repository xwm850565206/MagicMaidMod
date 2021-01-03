package com.xwm.magicmaid.network.particle;

import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SPacketThreeParamParticle implements IMessage
{
    private boolean messageValid;

    public double x, y, z;
    public double tx, ty, tz;
    public EnumCustomParticles particle;


    public SPacketThreeParamParticle(){
        this.messageValid = false;
    }

    public SPacketThreeParamParticle(double x, double y, double z, EnumCustomParticles particle)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.particle = particle;
        this.messageValid = true;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        try
        {
            this.x = buf.readDouble();
            this.y = buf.readDouble();
            this.z = buf.readDouble();
            this.particle = EnumCustomParticles.getParticleFromId(buf.readInt());
        }
        catch (IndexOutOfBoundsException e)
        {
            return;
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        if(!this.messageValid)
            return;

        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(particle.getParticleID());
    }

    public static class Handler implements IMessageHandler<SPacketThreeParamParticle, IMessage>
    {
        @Override
        public IMessage onMessage(SPacketThreeParamParticle message, MessageContext ctx)
        {
            if (message.messageValid && ctx.side != Side.CLIENT)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

            return null;
        }

        private void processMessage(SPacketThreeParamParticle message, MessageContext ctx){

            //后3个参数是备选参数
            ParticleSpawner.spawnParticle(message.particle, message.x, message.y, message.z,0, 0, 0);
        }
    }
}
