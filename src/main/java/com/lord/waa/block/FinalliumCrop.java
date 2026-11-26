package com.lord.waa.block;

import com.lord.waa.registry.ModItems;
import net.minecraft.block.BlockCrops;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class FinalliumCrop extends BlockCrops {
 public FinalliumCrop() {
  setRegistryName("waa", "finallium_crop");
  setTranslationKey("waa.finallium_crop");
  setHardness(0.0F);
 }
 @Override protected Item getSeed() { return ModItems.get("finallium_seeds"); }
 @Override protected Item getCrop() { return ModItems.get("finallium_essence"); }
 @Override public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
  if (getAge(state) >= getMaxAge()) {
   drops.add(new ItemStack(getSeed()));
   drops.add(new ItemStack(getCrop(), 1 + RANDOM.nextInt(2)));
   if (RANDOM.nextInt(8) == 0) drops.add(new ItemStack(getSeed()));
   if (Loader.isModLoaded("mysticalagriculture") && RANDOM.nextInt(20) == 0) {
    Item fertilizer = ForgeRegistries.ITEMS.getValue(new ResourceLocation("mysticalagriculture", "fertilized_essence"));
    if (fertilizer != null) drops.add(new ItemStack(fertilizer));
   }
  } else {
   drops.add(new ItemStack(getSeed()));
  }
 }
}
