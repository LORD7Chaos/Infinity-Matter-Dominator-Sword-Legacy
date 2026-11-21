package com.lord.waa.logic;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public final class Unlocks {
 private Unlocks() {}
 public static void grantEverything(EntityPlayerMP p) {
  MinecraftServer server = p.getServer();
  if (server == null) return;
  Cmd.run(server, "recipe give " + p.getName() + " *");
  Cmd.run(server, "advancement grant " + p.getName() + " everything");
 }
}
