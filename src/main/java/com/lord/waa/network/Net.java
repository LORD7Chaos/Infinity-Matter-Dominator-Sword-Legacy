package com.lord.waa.network;

import com.lord.waa.Reference;
import com.lord.waa.logic.KeyActions;
import com.lord.waa.logic.CrystalShovelExcavation;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class Net {
 private Net() {}
 public static SimpleNetworkWrapper CHANNEL;

 public static void init() {
  CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
  CHANNEL.registerMessage(Handler.class, KeyMessage.class, 0, Side.SERVER);
  CHANNEL.registerMessage(ShovelModeHandler.class, ShovelModeMessage.class, 1, Side.SERVER);
 }

 public static class KeyMessage implements IMessage {
  public int key;
  public KeyMessage() {}
  public KeyMessage(int key) { this.key = key; }
  @Override public void fromBytes(ByteBuf buf) { key = buf.readInt(); }
  @Override public void toBytes(ByteBuf buf) { buf.writeInt(key); }
 }

 public static class Handler implements IMessageHandler<KeyMessage, IMessage> {
  @Override
  public IMessage onMessage(final KeyMessage msg, final MessageContext ctx) {
   final EntityPlayerMP player = ctx.getServerHandler().player;
   player.getServerWorld().addScheduledTask(new Runnable() {
    @Override public void run() { KeyActions.perform(player, msg.key); }
   });
   return null;
  }
 }

 public static class ShovelModeMessage implements IMessage {
  public int direction;
  public ShovelModeMessage() {}
  public ShovelModeMessage(int direction) { this.direction = direction; }
  @Override public void fromBytes(ByteBuf buf) { direction = buf.readInt(); }
  @Override public void toBytes(ByteBuf buf) { buf.writeInt(direction); }
 }

 public static class ShovelModeHandler implements IMessageHandler<ShovelModeMessage, IMessage> {
  @Override public IMessage onMessage(final ShovelModeMessage msg, MessageContext ctx) {
   final EntityPlayerMP player = ctx.getServerHandler().player;
   player.getServerWorld().addScheduledTask(new Runnable() {
    @Override public void run() { CrystalShovelExcavation.changeSize(player, msg.direction); }
   });
   return null;
  }
 }
}
