package com.xwm.magicmaid.network.skill;

import com.xwm.magicmaid.manager.ISkillManagerImpl;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CPacketSkillPoint implements IMessage
{
    private int dimension;
    private int entityID;

    public CPacketSkillPoint() {
    }

    public CPacketSkillPoint(int dimension, int entityID) {
        this.dimension = dimension;
        this.entityID = entityID;
    }

    public static class Handler implements IMessageHandler<CPacketSkillPoint, IMessage> {
        @Override
        public IMessage onMessage(CPacketSkillPoint message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        private void processMessage(CPacketSkillPoint message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            Entity entity = world.getEntityByID(message.entityID);
            try {
                ISkillManagerImpl.getInstance().addSkillPoint((EntityPlayer) entity);
            } catch (NullPointerException | ClassCastException e) {
                ;
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        entityID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(entityID);
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }
}
