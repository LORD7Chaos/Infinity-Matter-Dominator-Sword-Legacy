package com.lord.waa.logic;

import com.lord.waa.config.WaaConfig;
import com.lord.waa.registry.ModItems;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.oredict.OreDictionary;

/** Automatically harvests connected ores with the Crystal Pickaxe on the server. */
@Mod.EventBusSubscriber(modid = "waa")
public final class CrystalOreExcavation {
 private static final Map<UUID, Pending> PENDING = new HashMap<UUID, Pending>();
 private static final Map<UUID, Job> JOBS = new HashMap<UUID, Job>();
 private static final Set<UUID> PROCESSING = new HashSet<UUID>();
 private CrystalOreExcavation() {}

 private static final class Pending {
  final World world;
  final BlockPos origin;
  final IBlockState original;
  Pending(World world, BlockPos origin, IBlockState original) {
   this.world = world; this.origin = origin; this.original = original;
  }
 }

 private static final class Job {
  final World world;
  final BlockPos origin;
  final IBlockState original;
  final Set<String> oreNames;
  final ArrayDeque<BlockPos> queue = new ArrayDeque<BlockPos>();
  final Set<BlockPos> seen = new HashSet<BlockPos>();
  int mined;
  Job(Pending pending) {
   this.world = pending.world;
   this.origin = pending.origin;
   this.original = pending.original;
   this.oreNames = names(pending.original);
   this.seen.add(origin);
   for (EnumFacing facing : EnumFacing.values()) queue.add(origin.offset(facing));
  }
 }

 private static Set<String> names(IBlockState state) {
  Set<String> found = new HashSet<String>();
  Block block = state.getBlock();
  ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));
  if (stack.isEmpty()) return found;
  for (int id : OreDictionary.getOreIDs(stack)) {
   String name = OreDictionary.getOreName(id);
   if (name.startsWith("ore") && name.length() > 3) found.add(name);
  }
  return found;
 }

 private static boolean sameOre(Job job, IBlockState state) {
  if (state.getBlock().isAir(state, job.world, job.origin)) return false;
  if (state.getBlock() == job.original.getBlock() &&
      state.getBlock().getMetaFromState(state) == job.original.getBlock().getMetaFromState(job.original)) return true;
  if (job.oreNames.isEmpty()) return false;
  for (String n : names(state)) if (job.oreNames.contains(n)) return true;
  return false;
 }

 @SubscribeEvent(priority = EventPriority.LOWEST)
 public static void onBreak(BlockEvent.BreakEvent event) {
  if (event.getWorld().isRemote || event.isCanceled()) return;
  if (!(event.getPlayer() instanceof EntityPlayerMP)) return;
  EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
  UUID id = player.getUniqueID();
  if (player.capabilities.isCreativeMode || PROCESSING.contains(id) || JOBS.containsKey(id) || PENDING.containsKey(id)) return;
  if (!WaaConfig.crystalPickVeinEnabled) return;
  ItemStack held = player.getHeldItemMainhand();
  if (held.isEmpty() || held.getItem() != ModItems.get("crystal_pickaxe")) return;
  IBlockState state = event.getState();
  if (state.getBlockHardness(player.world, event.getPos()) < 0) return;
  if (names(state).isEmpty()) return;
  PENDING.put(id, new Pending(player.world, event.getPos().toImmutable(), state));
 }

 @SubscribeEvent
 public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
  if (event.phase != TickEvent.Phase.END || event.player.world.isRemote || !(event.player instanceof EntityPlayerMP)) return;
  EntityPlayerMP player = (EntityPlayerMP) event.player;
  UUID id = player.getUniqueID();
  Pending pending = PENDING.remove(id);
  if (pending != null && pending.world == player.world &&
      !sameBlock(player.world.getBlockState(pending.origin), pending.original)) {
   JOBS.put(id, new Job(pending));
  }
  Job job = JOBS.get(id);
  if (job == null) return;
  if (!WaaConfig.crystalPickVeinEnabled || job.world != player.world || player.capabilities.isCreativeMode) {
   JOBS.remove(id); return;
  }
  ItemStack held = player.getHeldItemMainhand();
  if (held.isEmpty() || held.getItem() != ModItems.get("crystal_pickaxe")) { JOBS.remove(id); return; }
  PROCESSING.add(id);
  try {
   int budget = WaaConfig.crystalPickBlocksPerTick;
   int searched = 0;
   // Bound scanning as well as breaking to avoid lag from enormous ore clusters.
   while (budget > 0 && searched++ < budget * 24 && !job.queue.isEmpty() && job.mined < WaaConfig.crystalPickMaxBlocks) {
    BlockPos pos = job.queue.removeFirst();
    if (!job.seen.add(pos)) continue;
    if (Math.abs(pos.getX() - job.origin.getX()) > WaaConfig.crystalPickMaxDistance ||
        Math.abs(pos.getY() - job.origin.getY()) > WaaConfig.crystalPickMaxDistance ||
        Math.abs(pos.getZ() - job.origin.getZ()) > WaaConfig.crystalPickMaxDistance) continue;
    if (!job.world.isBlockLoaded(pos)) continue;
    IBlockState state = job.world.getBlockState(pos);
    if (!sameOre(job, state)) continue;
    for (EnumFacing facing : EnumFacing.values()) job.queue.addLast(pos.offset(facing));
    if (state.getBlockHardness(job.world, pos) < 0 || !job.world.isBlockModifiable(player, pos) ||
        !player.canPlayerEdit(pos, EnumFacing.UP, held)) continue;
    if (held.isItemStackDamageable() && held.getItemDamage() >= held.getMaxDamage() - 1) {
     JOBS.remove(id); return;
    }
    // Vanilla server harvesting handles Forge break protection, Silk Touch, Fortune and tool damage.
    if (player.interactionManager.tryHarvestBlock(pos)) {
     job.mined++;
     budget--;
    }
    if (held.isEmpty() || held.getItem() != ModItems.get("crystal_pickaxe")) { JOBS.remove(id); return; }
   }
  } finally {
   PROCESSING.remove(id);
  }
  if (job.queue.isEmpty() || job.mined >= WaaConfig.crystalPickMaxBlocks) JOBS.remove(id);
 }

 private static boolean sameBlock(IBlockState a, IBlockState b) {
  return a.getBlock() == b.getBlock() && a.getBlock().getMetaFromState(a) == b.getBlock().getMetaFromState(b);
 }

 @SubscribeEvent
 public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
  UUID id = event.player.getUniqueID();
  PENDING.remove(id); JOBS.remove(id); PROCESSING.remove(id);
 }
}
