package com.xwm.magicmaid.network;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MaidModePacket implements IMessage
{
    private boolean messageValid;
    private int maidId;
    private int dimension;

    public MaidModePacket(){
        this.messageValid = false;
    }

    public MaidModePacket(int maidId, int dimension) {
       this.maidId = maidId;
       this.dimension = dimension;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.maidId = buf.readInt();
        this.dimension = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.maidId);
        buf.writeInt(this.dimension);
    }

    public static class Handler implements IMessageHandler<MaidModePacket, IMessage>
    {
        @Override
        public IMessage onMessage(MaidModePacket message, MessageContext ctx) {
            if (message.messageValid && ctx.side != Side.SERVER)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }
        private void processMessage(MaidModePacket message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            EntityMagicMaid maid = (EntityMagicMaid) world.getEntityByID(message.maidId);

//            if (EnumMode.valueOf(maid.getMode()) == EnumMode.SERVE){
//                maid.setMode(EnumMode.toInt(EnumMode.FIGHT));
//            }
//            else if (EnumMode.valueOf(maid.getMode()) == EnumMode.FIGHT){
//                maid.setMode(EnumMode.toInt(EnumMode.SERVE));
//            }
            if (maid != null)
                maid.switchMode();
        }
    }
}
