package com.xwm.magicmaid.manager;

import com.xwm.magicmaid.event.SkillChangedEvent;
import com.xwm.magicmaid.event.SkillLevelUpEvent;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.entity.CPacketCapabilityUpdate;
import com.xwm.magicmaid.network.entity.SPacketCapabilityUpdate;
import com.xwm.magicmaid.object.item.interfaces.ICanGetSkillPoint;
import com.xwm.magicmaid.player.capability.CapabilityLoader;
import com.xwm.magicmaid.player.capability.ISkillCapability;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.ISkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

public class ISkillManagerImpl implements ISkillManager {

    private static ISkillManager instance = null;

    @Override
    public boolean addSkillPoint(EntityPlayer player) {
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null)
                return addSkillPoint(player, skillCapability.getSkillPointInventory().getStackInSlot(0));
        }
        return false;
    }

    @Override
    public boolean addSkillPoint(EntityPlayer player, ItemStack stack)
    {
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null) {
                if (stack.getItem() instanceof ICanGetSkillPoint) {
                    int skillPoint = ((ICanGetSkillPoint) stack.getItem()).getSkillPoint(stack, player) * stack.getCount();
                    if (!player.getEntityWorld().isRemote)
                        stack.shrink(stack.getCount());
                    skillCapability.setSkillPoint(skillCapability.getSkillPoint() + skillPoint);
                    updateToOtherSide(player);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addSkillPoint(EntityPlayer player, int skillPoint) {

        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null) {
                skillCapability.setSkillPoint(skillCapability.getSkillPoint() + skillPoint);
                updateToOtherSide(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void setActivePerformSkill(EntityPlayer player, int index, IPerformSkill performSkill) {
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null) {
                MinecraftForge.EVENT_BUS.post(new SkillChangedEvent(player, skillCapability.getActivePerformSkill(index), performSkill));
                skillCapability.setActivePerformSkill(index, performSkill);
                updateToOtherSide(player);
            }
        }
    }

    @Override
    public boolean upSkillLevel(EntityPlayer player, ISkill iSkill) {
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null) {
                int skillPoint = skillCapability.getSkillPoint();
                if (skillPoint >= iSkill.getRequirePoint() && iSkill.getLevel() < iSkill.getMaxLevel()) {
                    if (MinecraftForge.EVENT_BUS.post(new SkillLevelUpEvent.Pre(iSkill, player))) return false;
                    iSkill.setLevel(iSkill.getLevel() + 1);
                    skillCapability.setSkillPoint(skillPoint - iSkill.getRequirePoint());

                    MinecraftForge.EVENT_BUS.post(new SkillLevelUpEvent.Post(iSkill, player));
                    updateToOtherSide(player);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean upSkillLevel(EntityPlayer player, String skillName) {
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null) {
                ISkill iSkill = skillCapability.getAttributeSkill(skillName);
                if (iSkill == null) iSkill = skillCapability.getPassiveSkill(skillName);
                if (iSkill == null) iSkill = skillCapability.getPerformSkill(skillName);
                if (iSkill != null)
                    return upSkillLevel(player, iSkill);
            }
        }
        return false;
    }

    @Override
    public void updateToOtherSide(EntityPlayer player) {
        if (player.getEntityWorld().isRemote)
            updateToServer(player);
        else
            updateToClient(player);
    }

    @Override
    public void updateToServer(EntityPlayer player) {
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null))
        {
            updateToServer(player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null), player);
        }
    }

    @Override
    public void updateToClient(EntityPlayer player) {
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null))
        {
            updateToClient(player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null), player);
        }
    }

    @Override
    public void updateToServer(ISkillCapability instance, EntityPlayer player) {
        if (!player.world.isRemote) return;
        CPacketCapabilityUpdate packet = new CPacketCapabilityUpdate(getCompound(instance), player.getEntityWorld().provider.getDimension(), player.getEntityId(), 0);
        NetworkLoader.instance.sendToServer(packet);
    }

    @Override
    public void updateToClient(ISkillCapability instance, EntityPlayer player) {
        if (player.world.isRemote) return;
        SPacketCapabilityUpdate packet = new SPacketCapabilityUpdate(getCompound(instance), player.getEntityWorld().provider.getDimension(), player.getEntityId(), 0);
        NetworkLoader.instance.sendTo(packet, (EntityPlayerMP) player);
    }

    private NBTTagCompound getCompound(ISkillCapability instance)
    {
        return (NBTTagCompound) CapabilityLoader.SKILL_CAPABILITY.getStorage().writeNBT(CapabilityLoader.SKILL_CAPABILITY, instance, null);
    }

    /**
     * 单例模式，用于魔法生物的技能事宜
     * @return 返回实例
     */
    public static ISkillManager getInstance() {
        if (instance == null)
            instance = new ISkillManagerImpl();
        return instance;
    }
}
