package com.lord.waa.registry;

import com.lord.waa.Reference;
import com.lord.waa.registry.Specs.ItemSpec;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraftforge.common.util.EnumHelper;

final class CrystalTools {
 private CrystalTools() {}
 private static final ToolMaterial PICK = EnumHelper.addToolMaterial("WAA_CRYSTAL_PICK", 20, 5000, 100.0F, 5.0F, 15);
 private static final ToolMaterial AXE = EnumHelper.addToolMaterial("WAA_CRYSTAL_AXE", 20, 5000, 100.0F, 28.0F, 20);
 private static final ToolMaterial SHOVEL = EnumHelper.addToolMaterial("WAA_CRYSTAL_SHOVEL", 20, 5000, 100.0F, 5.0F, 20);
 private static final ToolMaterial HOE = EnumHelper.addToolMaterial("WAA_CRYSTAL_HOE", 20, 50000, 100.0F, 20.0F, 20);

 private static Item finish(Item i, ItemSpec s) {
  i.setRegistryName(Reference.MOD_ID, s.id);
  i.setTranslationKey(Reference.MOD_ID + "." + s.id);
  i.setCreativeTab(ModTabs.TABS[s.tab]);
  return i;
 }
 static Item pickaxe(ItemSpec s) { return finish(new ItemPickaxe(PICK) {}, s); }
 static Item axe(ItemSpec s) { return finish(new ItemAxe(AXE, 29.0F, -3.0F) {}, s); }
 static Item shovel(ItemSpec s) { return finish(new ItemSpade(SHOVEL), s); }
 static Item hoe(ItemSpec s) { return finish(new ItemHoe(HOE) {
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
   if (!player.isSneaking()) return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
   IBlockState state = world.getBlockState(pos);
   Block block = state.getBlock();
   // Never turn grass into plants or use the vanilla bonemeal grass behavior.
   if (block == Blocks.GRASS || block == Blocks.GRASS_PATH || block == Blocks.DIRT) return EnumActionResult.PASS;
   if (block instanceof BlockNetherWart) {
    if (state.getValue(BlockNetherWart.AGE) >= 3) return EnumActionResult.PASS;
    if (!world.isRemote) {
     world.setBlockState(pos, state.withProperty(BlockNetherWart.AGE, 3), 3);
     world.playEvent(2005, pos, 0);
    }
    return EnumActionResult.SUCCESS;
   }
   if (!(block instanceof IGrowable)) return EnumActionResult.PASS;
   IGrowable growable = (IGrowable) block;
   if (!growable.canGrow(world, pos, state, world.isRemote)) return EnumActionResult.PASS;
   if (!world.isRemote) {
    for (int step = 0; step < 64; step++) {
     IBlockState current = world.getBlockState(pos);
     Block currentBlock = current.getBlock();
     if (!(currentBlock instanceof IGrowable) || currentBlock == Blocks.GRASS) break;
     IGrowable currentGrowable = (IGrowable) currentBlock;
     if (!currentGrowable.canGrow(world, pos, current, false)) break;
     currentGrowable.grow(world, world.rand, pos, current);
     // Some modded blocks can grow without changing their state; prevent infinite attempts.
     if (world.getBlockState(pos) == current) break;
    }
    world.playEvent(2005, pos, 0);
   }
   return EnumActionResult.SUCCESS;
  }
 }, s); }
}
