package com.xwm.magicmaid.player.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CapabilityLoader
{
    @CapabilityInject(ICreatureCapability.class)
    public static Capability<ICreatureCapability> CREATURE_CAPABILITY;

    @CapabilityInject(ISkillCapability.class)
    public static Capability<ISkillCapability> SKILL_CAPABILITY;

    public CapabilityLoader(FMLPreInitializationEvent event)
    {
        CapabilityManager.INSTANCE.register(ICreatureCapability.class, new CapabilityMagicCreature.Storage(),
                new CapabilityMagicCreature.Factory());

        CapabilityManager.INSTANCE.register(ISkillCapability.class, new CapabilitySkill.Storage(),
                new CapabilitySkill.Factory());
    }
}
