package com.xwm.magicmaid.network;

import com.xwm.magicmaid.event.EventLoader;
import com.xwm.magicmaid.init.DimensionInit;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class AddBookPacket implements IMessage
{
    public int id;
    public PacketBuffer data;

    public AddBookPacket() {

    }

    public AddBookPacket(PacketBuffer data, int id) {
        this.data = data;
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
        int i = buf.readableBytes();
        if (i >= 0 && i <= 32767)
        {
            this.data = new PacketBuffer(buf.readBytes(i));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
        synchronized(this.data) { //This may be access multiple times, from multiple threads, lets be safe.
            this.data.markReaderIndex();
            buf.writeBytes((ByteBuf)this.data);
            this.data.resetReaderIndex();
        }
    }

    public static class Handler implements IMessageHandler<AddBookPacket, IMessage>
    {
        @Override
        public IMessage onMessage(AddBookPacket message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        private void processMessage(AddBookPacket message, MessageContext ctx)
        {
            try{
                ItemStack stack = message.data.readItemStack();
                World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(DimensionInit.DIMENSION_CHURCH);

                EntityPlayer player = (EntityPlayer)world.getEntityByID(message.id);
                if (player != null) {
                    if (player.getHeldItemOffhand().isEmpty())
                        player.setHeldItem(EnumHand.OFF_HAND, stack);
                    else {
                        EntityItem entityItem = new EntityItem(player.getEntityWorld(), player.posX, player.posY, player.posZ, stack);
                        world.spawnEntity(entityItem);
                    }
                }

            } catch (Exception e) {
                ;
            }
        }
    }
}