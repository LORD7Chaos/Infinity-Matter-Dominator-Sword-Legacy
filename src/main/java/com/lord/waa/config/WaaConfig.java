package com.lord.waa.config;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public final class WaaConfig {
 private WaaConfig() {}

 public static int maxAreaRadius = 0;
 public static boolean grenadeFlightExplosions = true;
 public static int unlockIntervalTicks = 20;
 public static boolean godModeSetsKeepInventory = true;

 public static int explosionMaxRadius = 24;
 public static int explosionBlocksPerTick = 4000;
 public static int explosionScanPerTick = 30000;
 public static int explosionMaxJobs = 16;
 public static boolean explosionDrops = false;
 public static int grenadeFlightInterval = 5;

 public static boolean crystalShovelEnabled = true;
 public static int crystalShovelBlocksPerTick = 8;
 public static boolean crystalPickVeinEnabled = true;
 public static int crystalPickMaxBlocks = 64;
 public static int crystalPickBlocksPerTick = 8;
 public static int crystalPickMaxDistance = 12;

 public static boolean debrisEnabled = true;
 public static int debrisVeinsPerChunk = 2;
 public static int debrisVeinSize = 3;
 public static int debrisMinY = 8;
 public static int debrisMaxY = 119;
 public static double debrisChance = 0.5D;

 public static void load(File file) {
  Configuration c = new Configuration(file);
  try {
   c.load();
   maxAreaRadius = c.getInt("maxAreaRadius", "safety", 0, 0, 100000, "0 = original radii (no cap).");
   grenadeFlightExplosions = c.getBoolean("grenadeFlightExplosions", "safety", true, "Original behaviour: grenade explodes every flight tick.");
   unlockIntervalTicks = c.getInt("unlockIntervalTicks", "performance", 20, 1, 1200, "Original = 1 (every tick). 20 is visually identical and far cheaper.");
   explosionMaxRadius = c.getInt("explosionMaxRadius", "explosions", 24, 3, 48, "Max radius (blocks) of weapon explosions. Radius = power * 0.9, capped by this.");
   explosionBlocksPerTick = c.getInt("explosionBlocksPerTick", "explosions", 4000, 100, 200000, "Blocks removed per tick per world (the blast spreads over several ticks).");
   explosionScanPerTick = c.getInt("explosionScanPerTick", "explosions", 30000, 1000, 1000000, "Block positions checked per tick per world.");
   explosionMaxJobs = c.getInt("explosionMaxJobs", "explosions", 16, 1, 200, "Max simultaneous weapon explosions per dimension; extra ones are ignored.");
   explosionDrops = c.getBoolean("explosionDrops", "explosions", false, "Blocks destroyed by weapon explosions drop items (many item entities = lag).");
   grenadeFlightInterval = c.getInt("grenadeFlightInterval", "explosions", 5, 1, 100, "Flying grenade explodes every N ticks (1 = original).");
   crystalShovelEnabled = c.getBoolean("enabled", "crystal_shovel_excavation", true, "Excavate a 3x3, 5x5 or 9x9 area with the Crystal Shovel.");
   crystalShovelBlocksPerTick = c.getInt("blocksPerTick", "crystal_shovel_excavation", 8, 1, 81, "Extra blocks harvested per player per tick.");
   crystalPickVeinEnabled = c.getBoolean("enabled", "crystal_pick_vein", true, "Break connected ore veins with the Crystal Pickaxe.");
   crystalPickMaxBlocks = c.getInt("maxBlocks", "crystal_pick_vein", 64, 1, 1024, "Maximum extra ore blocks per vein.");
   crystalPickBlocksPerTick = c.getInt("blocksPerTick", "crystal_pick_vein", 8, 1, 64, "Extra blocks harvested per player per tick.");
   crystalPickMaxDistance = c.getInt("maxDistance", "crystal_pick_vein", 12, 1, 32, "Maximum reach of a vein from the first ore block (in each axis).");
   debrisEnabled = c.getBoolean("enabled", "finallium_debris_worldgen", true, "Generate Finallium Debris in the Nether.");
   debrisVeinsPerChunk = c.getInt("veinsPerChunk", "finallium_debris_worldgen", 2, 0, 64, "Spawn attempts per chunk.");
   debrisVeinSize = c.getInt("veinSize", "finallium_debris_worldgen", 3, 1, 64, "Max blocks in one vein.");
   debrisMinY = c.getInt("minY", "finallium_debris_worldgen", 8, 1, 127, "Lowest Y of a vein.");
   debrisMaxY = c.getInt("maxY", "finallium_debris_worldgen", 119, 1, 127, "Highest Y of a vein.");
   debrisChance = c.get("chance", "finallium_debris_worldgen", 0.5D, "Chance (0.0 - 1.0) that a spawn attempt actually places a vein.", 0.0D, 1.0D).getDouble();
   godModeSetsKeepInventory = c.getBoolean("godModeSetsKeepInventory", "gamerules", true, "Original behaviour: keepInventory is forced on.");
  } finally {
   if (c.hasChanged()) c.save();
  }
 }

 public static double radius(double r) {
  return (maxAreaRadius > 0 && r > maxAreaRadius) ? maxAreaRadius : r;
 }
}
