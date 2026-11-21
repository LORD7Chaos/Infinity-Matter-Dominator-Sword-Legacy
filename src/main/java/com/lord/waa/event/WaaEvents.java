package com.lord.waa.event;

import com.lord.waa.logic.GodAttributes;
import com.lord.waa.logic.Procs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.lord.waa.registry.ModItems;
import com.lord.waa.registry.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "waa")
public final class WaaEvents {
 private WaaEvents() {}

 @SubscribeEvent
 public static void onAttacked(LivingAttackEvent event) {
  EntityLivingBase v = event.getEntityLiving();
  if (v.world.isRemote) return;
  if (v.isPotionActive(ModPotions.EVOLVED_POWER)) Procs.evolvedPowerAttacked(v);
 }

 @SubscribeEvent
 public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
  if (event.getWorld().isRemote) return;
  ItemStack held = event.getEntityPlayer().getHeldItemMainhand();
  if (held.isEmpty() || held.getItem() != ModItems.get("dominator_infinity")) return;
  BlockPos pos = event.getPos();
  event.getWorld().destroyBlock(pos, false);
  Procs.dominatorBreak3(event.getWorld(), pos);
 }

 @SubscribeEvent
 public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
  if (e.phase == TickEvent.Phase.END && e.side.isServer()) GodAttributes.maintain(e.player);
 }

 @SubscribeEvent
 public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) { GodAttributes.cleanLegacy(e.player); }

 @SubscribeEvent
 public static void onRespawn(PlayerEvent.PlayerRespawnEvent e) { GodAttributes.forget(e.player); GodAttributes.cleanLegacy(e.player); }

 @SubscribeEvent
 public static void onLogout(PlayerEvent.PlayerLoggedOutEvent e) { GodAttributes.forget(e.player); }
}
