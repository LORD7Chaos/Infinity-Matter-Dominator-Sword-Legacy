package com.lord.waa.world;

import com.lord.waa.config.WaaConfig;
import com.lord.waa.registry.ModBlocks;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public final class FinalliumGen implements IWorldGenerator {
 @Override
 public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider) {
  if (!WaaConfig.debrisEnabled || world.provider.getDimension() != -1) return;
  Block ore = ModBlocks.get("finallium_debris");
  if (ore == null) return;
  int minY = Math.min(WaaConfig.debrisMinY, WaaConfig.debrisMaxY);
  int maxY = Math.max(WaaConfig.debrisMinY, WaaConfig.debrisMaxY);
  WorldGenMinable vein = new WorldGenMinable(ore.getDefaultState(), WaaConfig.debrisVeinSize, BlockMatcher.forBlock(Blocks.NETHERRACK));
  for (int i = 0; i < WaaConfig.debrisVeinsPerChunk; i++) {
   if (random.nextDouble() >= WaaConfig.debrisChance) continue;
   int x = chunkX * 16 + 8 + random.nextInt(16);
   int z = chunkZ * 16 + 8 + random.nextInt(16);
   int y = minY + random.nextInt(maxY - minY + 1);
   vein.generate(world, random, new BlockPos(x, y, z));
  }
 }
}
