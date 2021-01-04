package com.xwm.magicmaid.network.skill;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

// todo 逻辑有问题，不应该传递技能的数据，而应该传递技能的序号等来控制技能事宜
public class CPacketSkill implements IMessage
{
    private UUID playerUUID;
//    private ISkill iSkill;
    private String skillName;
    private int skillIndex;
    private int dimension;
    private BlockPos pos; // 释放技能需要的参数
    private int type; // 0-释放技能(玩家) 1-设置技能
    public PacketBuffer packetBuffer;

    public CPacketSkill() {

    }

    public CPacketSkill(UUID playerUUID, String skillName, int skillIndex, int dimension, int type) {
        this.playerUUID = playerUUID;
        this.skillName = skillName;
        this.skillIndex = skillIndex;
        this.dimension = dimension;
        this.type = type;
    }

    public CPacketSkill(UUID playerUUID, String skillName, int skillIndex, int dimension, BlockPos pos, int type) {
        this(playerUUID, skillName, skillIndex, dimension, type);
        this.pos = pos;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getiSkill() {
        return skillName;
    }

    public void setiSkill(String skillName) {
        this.skillName = skillName;
    }

    public int getSkillIndex() {
        return skillIndex;
    }

    public void setSkillIndex(int skillIndex) {
        this.skillIndex = skillIndex;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        packetBuffer = new PacketBuffer(buf);
        playerUUID = packetBuffer.readUniqueId();
        skillName = packetBuffer.readString(32767);
        skillIndex = packetBuffer.readInt();
        dimension = packetBuffer.readInt();
        type = packetBuffer.readInt();

        if (type == 0)
            pos = packetBuffer.readBlockPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeUniqueId(playerUUID);
        packetBuffer.writeString(skillName);
        packetBuffer.writeInt(skillIndex);
        packetBuffer.writeInt(dimension);
        packetBuffer.writeInt(type);
        if (type == 0)
            packetBuffer.writeBlockPos(pos);
    }

    public static class Handler implements IMessageHandler<CPacketSkill, IMessage> {
        @Override
        public IMessage onMessage(CPacketSkill message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;

            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

            return null;
        }

        private void processMessage(CPacketSkill message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            EntityPlayer entityPlayer = world.getPlayerEntityByUUID(message.playerUUID);
            try {
                ISkillCapability skillCapability = entityPlayer.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                ISkill skill = null;
                if (message.skillName.startsWith("attribute"))
                    skill = skillCapability.getAttributeSkill(message.skillName);
                else if (message.skillName.startsWith("passive"))
                    skill = skillCapability.getPassiveSkill(message.skillName);
                else if (message.skillName.startsWith("perform"))
                    skill = skillCapability.getPerformSkill(message.skillName);

                switch (message.type){
                    case 0:
                        skillCapability.getActivePerformSkill(message.skillIndex).perform(entityPlayer, world, message.pos);
                        break;
                    case 1:
                        skillCapability.setActivePerformSkill(message.skillIndex, (IPerformSkill) skill);
                }

            } catch (NullPointerException e) {
                ;
            } catch (ClassCastException e) {
                Main.logger.warn("error type skill");
            }
        }
    }
}
