package com.xwm.magicmaid.command;

import com.xwm.magicmaid.store.WorldDifficultyData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandAdjustDifficulty extends CommandBase
{
    @Override
    public String getName() {
        return "magic_maid_diff";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.magic_maid_diff.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString("参数错误，格式为:magic_maid_diff <value>"));
            return;
        }

        try {
            int factor = Integer.valueOf(args[0]);
            if (factor < 0 || factor > 2){
                sender.sendMessage(new TextComponentString("参数错误，只能是0，1，2"));
                return;
            }
            WorldDifficultyData.get(server.getEntityWorld()).setWorldDifficulty(factor);
        } catch (NumberFormatException e){
            sender.sendMessage(new TextComponentString("参数错误，值为一个数字"));
        }

        sender.sendMessage(new TextComponentString("设置成功"));
    }

}
