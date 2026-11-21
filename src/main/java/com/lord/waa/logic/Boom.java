package com.lord.waa.logic;

import com.lord.waa.config.WaaConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = "waa")
public final class Boom {
 private Boom() {}
 private static final class Job {
  final int dim, cx, cy, cz, limit;
  final float power;
  int next;
  Job(int dim, int cx, int cy, int cz, int limit, float power) { this.dim = dim; this.cx = cx; this.cy = cy; this.cz = cz; this.limit = limit; this.power = power; }
 }
 private static final Map<Integer, List<Job>> JOBS = new HashMap<Integer, List<Job>>();
 private static final Random RAND = new Random();
 private static long[] table;
 private static int tableRadius;
 private static synchronized long[] table(int radius) {
  if (table != null && tableRadius >= radius) return table;
  int r2 = radius * radius;
  long[] tmp = new long[(int) (4.2D * radius * radius * radius) + 64];
  int n = 0;
  for (int dx = -radius; dx <= radius; dx++) for (int dy = -radius; dy <= radius; dy++) for (int dz = -radius; dz <= radius; dz++) {
   int d2 = dx * dx + dy * dy + dz * dz;
   if (d2 > r2) continue;
   tmp[n++] = ((long) d2 << 24) | ((long) (dx + 128) << 16) | ((long) (dy + 128) << 8) | (long) (dz + 128);
  }
  long[] out = Arrays.copyOf(tmp, n);
  Arrays.sort(out);
  table = out;
  tableRadius = radius;
  return out;
 }
 private static int count(long[] t, double r) {
  long key = ((long) Math.floor(r * r) + 1L) << 24;
  int lo = 0, hi = t.length;
  while (lo < hi) { int mid = (lo + hi) >>> 1; if (t[mid] < key) lo = mid + 1; else hi = mid; }
  return lo;
 }

 public static void explode(World w, double x, double y, double z, float power) {
  if (!(w instanceof WorldServer) || power <= 0.0F) return;
  int maxR = WaaConfig.explosionMaxRadius;
  double radius = Math.max(3.0D, Math.min(power * 0.9D, maxR));
  long[] t = table(maxR);
  int dim = w.provider.getDimension();
  int cx = (int) Math.floor(x), cy = (int) Math.floor(y), cz = (int) Math.floor(z);
  List<Job> list = JOBS.get(dim);
  if (list == null) { list = new ArrayList<Job>(); JOBS.put(dim, list); }
  for (Job j : list) {
   if (j.power >= power) {
    double dx = j.cx - cx, dy = j.cy - cy, dz = j.cz - cz;
    double lim = radius * 0.5D;
    if (dx * dx + dy * dy + dz * dz <= lim * lim) return;
   }
  }
  if (list.size() >= WaaConfig.explosionMaxJobs) return;
  list.add(new Job(dim, cx, cy, cz, count(t, radius), power));
  effects(w, x, y, z, power, radius);
 }
 private static void effects(World w, double x, double y, double z, float power, double radius) {
  Fx.sound(w, x, y, z, "entity.generic.explode", 4.0F, 0.7F);
  Fx.particle(w, EnumParticleTypes.EXPLOSION_HUGE, x, y, z, 1, 0, 0, 0, 0.0);
  AxisAlignedBB box = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
  for (EntityLivingBase e : w.getEntitiesWithinAABB(EntityLivingBase.class, box)) {
   double d = Math.sqrt(e.getDistanceSq(x, y, z));
   if (d > radius) continue;
   e.attackEntityFrom(DamageSource.causeExplosionDamage((net.minecraft.world.Explosion) null), (float) (power * 7.0D * (1.0D - d / radius)) + 1.0F);
  }
 }
 @SubscribeEvent
 public static void onWorldTick(TickEvent.WorldTickEvent e) {
  if (e.phase != TickEvent.Phase.END || e.world.isRemote) return;
  List<Job> list = JOBS.get(e.world.provider.getDimension());
  if (list == null || list.isEmpty()) return;
  int scanBudget = WaaConfig.explosionScanPerTick, breakBudget = WaaConfig.explosionBlocksPerTick;
  long[] t = table;
  boolean drops = WaaConfig.explosionDrops && e.world.getGameRules().getBoolean("doTileDrops");
  BlockPos.MutableBlockPos mp = new BlockPos.MutableBlockPos();
  Iterator<Job> it = list.iterator();
  while (it.hasNext()) {
   Job j = it.next();
   while (j.next < j.limit && scanBudget > 0 && breakBudget > 0) {
    long k = t[j.next++];
    scanBudget--;
    int y = j.cy + (int) ((k >> 8) & 255L) - 128;
    if (y < 0 || y > 255) continue;
    mp.setPos(j.cx + (int) ((k >> 16) & 255L) - 128, y, j.cz + (int) (k & 255L) - 128);
    if (!e.world.isBlockLoaded(mp)) continue;
    IBlockState st = e.world.getBlockState(mp);
    Block b = st.getBlock();
    if (b == Blocks.AIR || st.getMaterial() == Material.AIR) continue;
    if (st.getBlockHardness(e.world, mp) < 0.0F) continue;
    if (b.getExplosionResistance(null) > j.power * 10.0F) continue;
    if (drops && RAND.nextFloat() < 1.0F / j.power) b.dropBlockAsItemWithChance(e.world, mp.toImmutable(), st, 1.0F, 0);
    e.world.setBlockState(mp.toImmutable(), Blocks.AIR.getDefaultState(), 2 | 16);
    breakBudget--;
   }
   if (j.next >= j.limit) it.remove();
   if (scanBudget <= 0 || breakBudget <= 0) break;
  }
 }
 @SubscribeEvent
 public static void onWorldUnload(net.minecraftforge.event.world.WorldEvent.Unload e) {
  if (!e.getWorld().isRemote) JOBS.remove(e.getWorld().provider.getDimension());
 }
 public static void clear() { JOBS.clear(); }
}
