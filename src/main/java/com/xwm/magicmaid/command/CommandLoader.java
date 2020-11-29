package com.xwm.magicmaid.command;

import net.minecraft.command.CommandBase;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Created by jin on 2020/2/4 12:53 AM
 */
public class CommandLoader
{
    public static CommandBase commandAdjustMultiFactor = new CommandAdjustDifficulty();

    public CommandLoader(FMLServerStartingEvent event)
    {
        event.registerServerCommand(commandAdjustMultiFactor);
    }
}
