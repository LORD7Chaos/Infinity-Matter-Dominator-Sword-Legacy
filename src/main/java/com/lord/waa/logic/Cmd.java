package com.lord.waa.logic;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public final class Cmd {
 private Cmd() {}

 public static void run(final MinecraftServer server, String command) {
  ICommandSender sender = new ICommandSender() {
   public String getName() { return "waa"; }
   public void sendMessage(ITextComponent component) {}
   public boolean canUseCommand(int permLevel, String commandName) { return true; }
   public boolean sendCommandFeedback() { return false; }
   public World getEntityWorld() { return server.getWorld(0); }
   public MinecraftServer getServer() { return server; }
   public BlockPos getPosition() { return BlockPos.ORIGIN; }
   public net.minecraft.util.math.Vec3d getPositionVector() { return net.minecraft.util.math.Vec3d.ZERO; }
   public Entity getCommandSenderEntity() { return null; }
  };
  try {
   server.getCommandManager().executeCommand(sender, command);
  } catch (RuntimeException ignored) {
  }
 }
}
