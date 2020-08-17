package com.xwm.magicmaid.network;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.util.handlers.SoundsHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SoundPacket implements IMessage {
    private boolean messageValid;
    private BlockPos pos;
    private int soundType;

    public SoundPacket() {
        this.messageValid = false;
    }

    public SoundPacket(int soundType, BlockPos pos) {
        this.soundType = soundType;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.soundType = buf.readInt();
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.soundType);
        buf.writeLong(this.pos.toLong());
    }

    public static class Handler implements IMessageHandler<SoundPacket, IMessage> {
        @Override
        public IMessage onMessage(SoundPacket message, MessageContext ctx) {
            if (message.messageValid && ctx.side != Side.CLIENT)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        private void processMessage(SoundPacket message, MessageContext ctx)
        {
            SoundEvent event;
            SoundCategory category;
            int volume;
            switch (message.soundType){
                case 0:
                    event = SoundsHandler.BELL;
                    volume = 3;
                    category = SoundCategory.BLOCKS;
                    break;
                case 1:
                    event = SoundsHandler.MAID_AMBIENT;
                    volume = 3;
                    category = SoundCategory.NEUTRAL;
                    break;
                default:
                    event = SoundsHandler.BELL;
                    volume = 1;
                    category = SoundCategory.BLOCKS;
                    break;
            }
            Minecraft.getMinecraft().world.playSound(message.pos.getX(), message.pos.getY(), message.pos.getZ(), event, category, volume, 1, true);
        }
    }
}