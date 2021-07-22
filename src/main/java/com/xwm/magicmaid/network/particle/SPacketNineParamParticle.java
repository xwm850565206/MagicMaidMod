package com.xwm.magicmaid.network.particle;

import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SPacketNineParamParticle implements IMessage
{
    private boolean messageValid;

    public double x, y, z;
    public double tx, ty, tz;
    public double kx, ky, kz;
    public EnumCustomParticles particle;


    public SPacketNineParamParticle(){
        this.messageValid = false;
    }

    public SPacketNineParamParticle(double x, double y, double z, double tx, double ty, double tz, double kx, double ky, double kz, EnumCustomParticles particle)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        this.kx = kx;
        this.ky = ky;
        this.kz = kz;
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
            this.kx = buf.readDouble();
            this.ky = buf.readDouble();
            this.kz = buf.readDouble();
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
        buf.writeDouble(kx);
        buf.writeDouble(ky);
        buf.writeDouble(kz);
        buf.writeInt(particle.getParticleID());
    }

    public static class Handler implements IMessageHandler<SPacketNineParamParticle, IMessage>
    {
        @Override
        public IMessage onMessage(SPacketNineParamParticle message, MessageContext ctx)
        {
            if (message.messageValid && ctx.side != Side.CLIENT)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

            return null;
        }

        private void processMessage(SPacketNineParamParticle message, MessageContext ctx){

            ParticleSpawner.spawnParticle(message.particle, message.x, message.y, message.z, message.tx, message.ty, message.tz, message.kx, message.ky, message.kz);
        }
    }
}
