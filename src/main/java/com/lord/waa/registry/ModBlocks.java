package com.lord.waa.registry;

import com.lord.waa.block.WaaBlock;
import com.lord.waa.compat.MACompat;
import com.lord.waa.registry.Specs.BlockSpec;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.block.Block;

public final class ModBlocks {
 private ModBlocks() {}
 private static final Map<String, Block> BLOCKS = new LinkedHashMap<String, Block>();
 public static Block get(String id) { return BLOCKS.get(id); }
 public static Map<String, Block> all() { return BLOCKS; }
 public static void create() {
  if (!BLOCKS.isEmpty()) return;
  ModTabs.create();
  for (BlockSpec s : Specs.BLOCKS) BLOCKS.put(s.id, new WaaBlock(s));
  Finallium.createBlocks(BLOCKS);
  MACompat.createCrop();
 }
}
