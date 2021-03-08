package com.xwm.magicmaid.network;

import com.xwm.magicmaid.store.WorldDifficultyData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CPacketChangeDifficulty implements IMessage
{
    private int difficulty;

    public CPacketChangeDifficulty() {
    }

    public CPacketChangeDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.difficulty = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(difficulty);
    }

    public static class Handler implements IMessageHandler<CPacketChangeDifficulty, IMessage>
    {
        @Override
        public IMessage onMessage(CPacketChangeDifficulty message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        private void processMessage(CPacketChangeDifficulty message, MessageContext ctx)
        {
            try{
                WorldDifficultyData data = WorldDifficultyData.get(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
                data.setWorldDifficulty(message.difficulty);
            } catch (Exception e) {
                ;
            }
        }
    }
}
