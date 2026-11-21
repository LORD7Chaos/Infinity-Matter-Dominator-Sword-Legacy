package com.lord.waa.command;

import com.lord.waa.logic.Procs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class UltraKillCommand extends CommandBase {
 @Override public String getName() { return "ultrakill"; }
 @Override public String getUsage(ICommandSender sender) { return "/ultrakill"; }
 @Override public int getRequiredPermissionLevel() { return 0; }
 @Override public boolean checkPermission(MinecraftServer server, ICommandSender sender) { return true; }
 @Override
 public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
  net.minecraft.util.math.BlockPos p = sender.getPosition();
  Procs.ultraKill(sender.getEntityWorld(), p.getX() + 0.5D, p.getY(), p.getZ() + 0.5D);
 }
}
