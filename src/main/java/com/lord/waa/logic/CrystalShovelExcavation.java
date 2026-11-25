package com.lord.waa.logic;

import com.lord.waa.config.WaaConfig;
import com.lord.waa.registry.ModItems;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/** Server-authoritative area mining for the Waa Crystal Shovel. */
@Mod.EventBusSubscriber(modid = "waa")
public final class CrystalShovelExcavation {
 private static final Map<UUID, Integer> SIZES = new HashMap<UUID, Integer>();
 private static final Map<UUID, Pending> PENDING = new HashMap<UUID, Pending>();
 private static final Map<UUID, Job> JOBS = new HashMap<UUID, Job>();
 private static final Set<UUID> PROCESSING = new HashSet<UUID>();
 private CrystalShovelExcavation() {}

 private static final class Pending {
  final World world;
  final BlockPos origin;
  final IBlockState state;
  final EnumFacing side;
  final int size;
  Pending(World world, BlockPos origin, IBlockState state, EnumFacing side, int size) {
   this.world = world; this.origin = origin; this.state = state; this.side = side; this.size = size;
  }
 }

 private static final class Job {
  final World world;
  final ArrayDeque<BlockPos> positions = new ArrayDeque<BlockPos>();
  Job(Pending pending) {
   world = pending.world;
   int radius = pending.size / 2;
   for (int a = -radius; a <= radius; a++) {
    for (int b = -radius; b <= radius; b++) {
     BlockPos p;
     if (pending.side.getAxis() == EnumFacing.Axis.Y) p = pending.origin.add(a, 0, b);
     else if (pending.side.getAxis() == EnumFacing.Axis.X) p = pending.origin.add(0, a, b);
     else p = pending.origin.add(a, b, 0);
     if (!p.equals(pending.origin)) positions.addLast(p);
    }
   }
  }
 }

 public static boolean isShovelBlock(IBlockState state) {
  Material m = state.getMaterial();
  return m == Material.GROUND || m == Material.GRASS || m == Material.SAND ||
      m == Material.CLAY || m == Material.SNOW || m == Material.CRAFTED_SNOW;
 }

 private static boolean sameBlock(IBlockState a, IBlockState b) {
  Block block = a.getBlock();
  return block == b.getBlock() && block.getMetaFromState(a) == block.getMetaFromState(b);
 }

 /** Invoked on the main server thread by the scroll packet handler. */
 public static void changeSize(EntityPlayerMP player, int direction) {
  if (!WaaConfig.crystalShovelEnabled || direction == 0 ||
      player.getHeldItemMainhand().getItem() != ModItems.get("crystal_shovel") || !player.isSneaking()) return;
  UUID id = player.getUniqueID();
  int current = SIZES.containsKey(id) ? SIZES.get(id) : 3;
  int[] options = {3, 5, 9};
  int index = current == 9 ? 2 : current == 5 ? 1 : 0;
  index = (index + (direction > 0 ? 1 : 2)) % 3;
  SIZES.put(id, options[index]);
  player.sendStatusMessage(new TextComponentString("Crystal Shovel: " + options[index] + "x" + options[index]), true);
 }

 @SubscribeEvent(priority = EventPriority.LOWEST)
 public static void onBreak(BlockEvent.BreakEvent event) {
  if (event.getWorld().isRemote || event.isCanceled() || !WaaConfig.crystalShovelEnabled) return;
  if (!(event.getPlayer() instanceof EntityPlayerMP)) return;
  EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
  UUID id = player.getUniqueID();
  if (player.capabilities.isCreativeMode || PROCESSING.contains(id) || PENDING.containsKey(id) || JOBS.containsKey(id)) return;
  ItemStack held = player.getHeldItemMainhand();
  if (held.isEmpty() || held.getItem() != ModItems.get("crystal_shovel")) return;
  if (!isShovelBlock(event.getState()) || event.getState().getBlockHardness(player.world, event.getPos()) < 0) return;
  RayTraceResult hit = player.rayTrace(6.0D, 1.0F);
  EnumFacing side = hit != null && event.getPos().equals(hit.getBlockPos()) && hit.sideHit != null
      ? hit.sideHit : (Math.abs(player.rotationPitch) > 60 ? EnumFacing.UP : player.getHorizontalFacing());
  int size = SIZES.containsKey(id) ? SIZES.get(id) : 3;
  PENDING.put(id, new Pending(player.world, event.getPos().toImmutable(), event.getState(), side, size));
 }

 @SubscribeEvent
 public static void onTick(TickEvent.PlayerTickEvent event) {
  if (event.phase != TickEvent.Phase.END || event.player.world.isRemote || !(event.player instanceof EntityPlayerMP)) return;
  EntityPlayerMP player = (EntityPlayerMP) event.player;
  UUID id = player.getUniqueID();
  Pending pending = PENDING.remove(id);
  if (pending != null && pending.world == player.world &&
      !sameBlock(player.world.getBlockState(pending.origin), pending.state)) JOBS.put(id, new Job(pending));
  Job job = JOBS.get(id);
  if (job == null) return;
  ItemStack held = player.getHeldItemMainhand();
  if (!WaaConfig.crystalShovelEnabled || job.world != player.world || held.isEmpty() ||
      held.getItem() != ModItems.get("crystal_shovel") || player.capabilities.isCreativeMode) {
   JOBS.remove(id); return;
  }
  PROCESSING.add(id);
  try {
   int budget = WaaConfig.crystalShovelBlocksPerTick;
   while (budget-- > 0 && !job.positions.isEmpty()) {
    BlockPos pos = job.positions.removeFirst();
    if (!job.world.isBlockLoaded(pos)) continue;
    IBlockState state = job.world.getBlockState(pos);
    if (!isShovelBlock(state) || state.getBlockHardness(job.world, pos) < 0 ||
        !job.world.isBlockModifiable(player, pos) || !player.canPlayerEdit(pos, EnumFacing.UP, held)) continue;
    if (held.isItemStackDamageable() && held.getItemDamage() >= held.getMaxDamage() - 1) {
     JOBS.remove(id); return;
    }
    // Uses vanilla/Forge harvesting for protection, drops, enchantments and durability.
    player.interactionManager.tryHarvestBlock(pos);
    if (held.isEmpty() || held.getItem() != ModItems.get("crystal_shovel")) {
     JOBS.remove(id); return;
    }
   }
  } finally {
   PROCESSING.remove(id);
  }
  if (job.positions.isEmpty()) JOBS.remove(id);
 }

 @SubscribeEvent
 public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
  UUID id = event.player.getUniqueID();
  SIZES.remove(id); PENDING.remove(id); JOBS.remove(id); PROCESSING.remove(id);
 }
}
