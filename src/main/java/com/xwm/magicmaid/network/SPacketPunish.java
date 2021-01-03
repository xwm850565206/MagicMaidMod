package com.xwm.magicmaid.network;

import com.xwm.magicmaid.gui.KickOutWindow;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketPunish implements IMessage
{
    public int punishLevel;
    private boolean messageValid;
    public SPacketPunish() {
        this.messageValid = false;
    }

    public SPacketPunish(int punishLevel) {
        this.punishLevel = punishLevel;
        this.messageValid = true;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        this.punishLevel = buf.readInt();
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.punishLevel);
    }

    public static class Handler implements IMessageHandler<SPacketPunish, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(SPacketPunish message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT || !message.messageValid)
                return null;

            if ((message.punishLevel & 2) == 2)
                Minecraft.getMinecraft().displayGuiScreen(
                        new KickOutWindow("尝试将你移出游戏,方法为崩溃你的客户端", "检测到你使用过强mod进攻女仆", message.punishLevel));
            if ((message.punishLevel & 4) == 4)
                Minecraft.getMinecraft().displayGuiScreen(
                        new KickOutWindow("尝试将你的游戏模式永久修改为生存模式", "检测到你使用过强mod进攻女仆", message.punishLevel));

            return null;
        }
    }
}
