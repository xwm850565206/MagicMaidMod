package com.xwm.magicmaid.network.skill;

import com.xwm.magicmaid.manager.ISkillManagerImpl;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.IAttributeSkill;
import com.xwm.magicmaid.player.skill.IPassiveSkill;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import com.xwm.magicmaid.registry.MagicSkillRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class CPacketLearnSkill implements IMessage
{

    private UUID playerUUID;
    private String skillName;

    public CPacketLearnSkill() {
    }

    public CPacketLearnSkill(UUID playerUUID, String skillName) {
        this.playerUUID = playerUUID;
        this.skillName = skillName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        this.playerUUID = packetBuffer.readUniqueId();
        this.skillName = packetBuffer.readString(1000);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeUniqueId(this.playerUUID);
        packetBuffer.writeString(this.skillName);
    }

    public static class Handler implements IMessageHandler<CPacketLearnSkill, IMessage> {
        @Override
        public IMessage onMessage(CPacketLearnSkill message, MessageContext ctx) {
            if (ctx.side != Side.SERVER)
                return null;

            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

            return null;
        }

        private void processMessage(CPacketLearnSkill message, MessageContext ctx) {
            EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.playerUUID);
            if (player != null) {
                ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
                ISkill skill = MagicSkillRegistry.getSkill(message.skillName);
                if (skill != null && skillCapability != null)
                {
                    if (message.skillName.startsWith("attribute"))
                        skillCapability.setAttributeSkill(message.skillName, (IAttributeSkill) skill);
                    else if (message.skillName.startsWith("passive"))
                        skillCapability.setPassiveSkill(message.skillName, (IPassiveSkill) skill);
                    else if (message.skillName.startsWith("perform"))
                        skillCapability.setPerformSkill(message.skillName, (IPerformSkill) skill);

                    ISkillManagerImpl.getInstance().updateToClient(player);
                }
            }
        }
    }
}
