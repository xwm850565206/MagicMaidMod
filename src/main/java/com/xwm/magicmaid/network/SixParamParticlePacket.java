package com.xwm.magicmaid.network;

import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SixParamParticlePacket implements IMessage
{
    private boolean messageValid;

    public double x, y, z;
    public double tx, ty, tz;
    public EnumCustomParticles particle;


    public SixParamParticlePacket(){
        this.messageValid = false;
    }

    public SixParamParticlePacket(double x, double y, double z, double tx, double ty, double tz, EnumCustomParticles particle)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
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
            this.tx = buf.readDouble();
            this.ty = buf.readDouble();
            this.tz = buf.readDouble();
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
        buf.writeDouble(tx);
        buf.writeDouble(ty);
        buf.writeDouble(tz);
        buf.writeInt(particle.getParticleID());
    }

    public static class Handler implements IMessageHandler<SixParamParticlePacket, IMessage>
    {
        @Override
        public IMessage onMessage(SixParamParticlePacket message, MessageContext ctx)
        {
            if (message.messageValid && ctx.side != Side.CLIENT)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

            return null;
        }

        private void processMessage(SixParamParticlePacket message, MessageContext ctx){

            ParticleSpawner.spawnParticle(message.particle, message.x, message.y, message.z, message.tx, message.ty, message.tz);
        }
    }
}
