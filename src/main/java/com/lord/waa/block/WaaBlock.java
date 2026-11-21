package com.lord.waa.block;

import com.lord.waa.Reference;
import com.lord.waa.registry.ModTabs;
import com.lord.waa.registry.Specs.BlockSpec;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.EnumPushReaction;

public class WaaBlock extends Block {
 private static final Material FREE_IRON = new Material(MapColor.IRON);
 private static final Material FREE_ROCK = new Material(MapColor.STONE);

 private final BlockSpec spec;

 public WaaBlock(BlockSpec spec) {
  super(material(spec), spec.iron ? MapColor.IRON : MapColor.STONE);
  this.spec = spec;
  this.fullBlock = !(spec.transparent || spec.nonOpaque);
  setRegistryName(Reference.MOD_ID, spec.id);
  setTranslationKey(Reference.MOD_ID + "." + spec.id);
  setSoundType(spec.metal ? SoundType.METAL : SoundType.STONE);
  setHardness(spec.hardness);
  setResistance(spec.resistance);
  setLightLevel(spec.light / 15.0F);
  setLightOpacity(spec.transparent ? 0 : 15);
  if (spec.harvestLevel >= 0) setHarvestLevel("pickaxe", spec.harvestLevel);
  setCreativeTab(ModTabs.TABS[3]);
 }

 private static Material material(BlockSpec s) {
  if (s.requiresTool) return s.iron ? Material.IRON : Material.ROCK;
  return s.iron ? FREE_IRON : FREE_ROCK;
 }

 @Override public boolean isOpaqueCube(IBlockState state) { return spec == null || !(spec.transparent || spec.nonOpaque); }
 @Override public boolean isFullCube(IBlockState state) { return spec == null || !(spec.transparent || spec.nonOpaque); }
 @Override public BlockRenderLayer getRenderLayer() { return (spec != null && (spec.transparent || spec.nonOpaque)) ? BlockRenderLayer.CUTOUT : BlockRenderLayer.SOLID; }
 @Override public boolean canProvidePower(IBlockState state) { return spec != null && spec.powered; }
 @Override public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) { return (spec != null && spec.powered) ? 15 : 0; }
 @Override public EnumPushReaction getPushReaction(IBlockState state) { return (spec != null && spec.noPush) ? EnumPushReaction.BLOCK : super.getPushReaction(state); }
}
