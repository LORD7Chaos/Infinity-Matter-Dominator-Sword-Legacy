package com.lord.waa.logic;

import com.lord.waa.registry.ModItems;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.oredict.OreDictionary;

/** Server-side connected tree felling for the Crystal Axe. */
@Mod.EventBusSubscriber(modid = "waa")
public final class CrystalTreeFelling {
 private static final int MAX_LOGS = 256;
 private static final int MAX_LEAVES = 512;
 private static final Map<UUID, Pending> PENDING = new HashMap<UUID, Pending>();
 private static final Set<UUID> PROCESSING = new HashSet<UUID>();
 private CrystalTreeFelling() {}

 private static final class Pending {
  final World world;
  final BlockPos origin;
  Pending(World w, BlockPos pos) { world = w; origin = pos; }
 }

 private static boolean log(IBlockState state) {
  Block b = state.getBlock();
  if (b instanceof BlockLog) return true;
  ItemStack stack = new ItemStack(b, 1, b.getMetaFromState(state));
  if (stack.isEmpty()) return false;
  for (int id : OreDictionary.getOreIDs(stack)) {
   if ("logWood".equals(OreDictionary.getOreName(id))) return true;
  }
  return false;
 }

 @SubscribeEvent(priority = EventPriority.LOWEST)
 public static void onBreak(BlockEvent.BreakEvent event) {
  if (event.getWorld().isRemote || event.isCanceled()) return;
  if (!(event.getPlayer() instanceof EntityPlayerMP)) return;
  EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
  if (player.capabilities.isCreativeMode || PROCESSING.contains(player.getUniqueID())) return;
  ItemStack held = player.getHeldItemMainhand();
  if (held.isEmpty() || held.getItem() != ModItems.get("crystal_axe")) return;
  if (!log(event.getState())) return;
  PENDING.put(player.getUniqueID(), new Pending(player.world, event.getPos().toImmutable()));
 }

 @SubscribeEvent
 public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
  if (event.phase != TickEvent.Phase.END || event.player.world.isRemote) return;
  Pending pending = PENDING.remove(event.player.getUniqueID());
  if (pending == null || pending.world != event.player.world || !(event.player instanceof EntityPlayerMP)) return;
  EntityPlayerMP player = (EntityPlayerMP) event.player;
  if (log(player.world.getBlockState(pending.origin))) return; // Original log was not actually removed.
  ItemStack held = player.getHeldItemMainhand();
  if (held.isEmpty() || held.getItem() != ModItems.get("crystal_axe")) return;
  PROCESSING.add(player.getUniqueID());
  try { fell(player, pending.origin, held); }
  finally { PROCESSING.remove(player.getUniqueID()); }
 }

 private static void fell(EntityPlayerMP player, BlockPos origin, ItemStack axe) {
  World world = player.world;
  ArrayDeque<BlockPos> queue = new ArrayDeque<BlockPos>();
  Set<BlockPos> found = new HashSet<BlockPos>();
  List<BlockPos> logs = new ArrayList<BlockPos>();
  // The player's original log is already air: start with its 26 neighbors.
  for (BlockPos p : BlockPos.getAllInBox(origin.add(-1,-1,-1), origin.add(1,1,1))) {
   if (log(world.getBlockState(p))) queue.add(p.toImmutable());
  }
  while (!queue.isEmpty() && logs.size() < MAX_LOGS) {
   BlockPos pos = queue.poll();
   if (!found.add(pos) || !world.isBlockLoaded(pos) || !log(world.getBlockState(pos))) continue;
   logs.add(pos);
   for (int x = -1; x <= 1; x++) for (int y = -1; y <= 1; y++) for (int z = -1; z <= 1; z++) {
    if (x == 0 && y == 0 && z == 0) continue;
    BlockPos next = pos.add(x,y,z);
    if (!found.contains(next) && world.isBlockLoaded(next) && log(world.getBlockState(next))) queue.add(next);
   }
  }
  if (logs.isEmpty()) return;
  Set<BlockPos> leaves = new HashSet<BlockPos>();
  // Only remove trees with a canopy, not isolated logs or ordinary wooden structures.
  for (BlockPos logPos : logs) {
   for (BlockPos pos : BlockPos.getAllInBox(logPos.add(-2,-2,-2), logPos.add(2,2,2))) {
    if (leaves.size() >= MAX_LEAVES) break;
    if (world.isBlockLoaded(pos) && world.getBlockState(pos).getBlock().isLeaves(world.getBlockState(pos), world, pos)) {
     leaves.add(pos.toImmutable());
    }
   }
  }
  if (leaves.isEmpty()) return;
  // Bottom-up logs, then leaves. Each block drops normally and consumes durability.
  logs.sort((a,b) -> Integer.compare(a.getY(), b.getY()));
  for (BlockPos pos : logs) {
   if (!breakOne(player, axe, pos)) return;
  }
  for (BlockPos pos : leaves) {
   if (!breakOne(player, axe, pos)) return;
  }
 }

 private static boolean breakOne(EntityPlayerMP player, ItemStack axe, BlockPos pos) {
  if (axe.isEmpty() || axe.getItem() != ModItems.get("crystal_axe")) return false;
  if (axe.isItemStackDamageable() && axe.getItemDamage() >= axe.getMaxDamage() - 1) return false;
  World world = player.world;
  if (!world.isBlockLoaded(pos)) return true;
  IBlockState state = world.getBlockState(pos);
  if (!log(state) && !state.getBlock().isLeaves(state, world, pos)) return true;
  if (state.getBlockHardness(world, pos) < 0 || !world.isBlockModifiable(player, pos) || !player.canPlayerEdit(pos, net.minecraft.util.EnumFacing.UP, axe)) return true;
  if (!MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, state, player))) {
   if (world.destroyBlock(pos, true)) axe.damageItem(1, player);
  }
  return true;
 }
}
