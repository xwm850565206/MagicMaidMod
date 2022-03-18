package com.xwm.magicmaid.network;

import com.xwm.magicmaid.store.WorldDifficultyData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SPacketUpdateDifficult implements IMessage
{
    private int difficulty;

    public SPacketUpdateDifficult() {
    }

    public SPacketUpdateDifficult(int difficulty) {
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

    public static class Handler implements IMessageHandler<SPacketUpdateDifficult, IMessage>
    {
        @Override
        public IMessage onMessage(SPacketUpdateDifficult message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT)
                return null;

            processMessage(message, ctx);
            return null;
        }

        private void processMessage(SPacketUpdateDifficult message, MessageContext ctx)
        {
            WorldDifficultyData data = WorldDifficultyData.get(null);
            data.setWorldDifficulty(message.difficulty);
        }
    }
}
