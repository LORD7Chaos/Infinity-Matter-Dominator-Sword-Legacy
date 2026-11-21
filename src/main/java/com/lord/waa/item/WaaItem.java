package com.lord.waa.item;

import com.google.common.collect.Multimap;
import com.lord.waa.Reference;
import com.lord.waa.logic.ItemHooks;
import com.lord.waa.registry.ModTabs;
import com.lord.waa.registry.Specs.ItemSpec;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaaItem extends Item {
 private final ItemSpec spec;
 private final EnumRarity rarity;
 public WaaItem(ItemSpec spec) {
  this.spec = spec;
  this.rarity = EnumRarity.valueOf(spec.rarity);
  setRegistryName(Reference.MOD_ID, spec.id);
  setTranslationKey(Reference.MOD_ID + "." + spec.id);
  setMaxStackSize(spec.stack);
  if (spec.tab >= 0) setCreativeTab(ModTabs.TABS[spec.tab]);
 }
 public String id() { return spec.id; }
 @Override public EnumRarity getRarity(ItemStack stack) { return rarity; }
 @Override public boolean hasEffect(ItemStack stack) { return spec.glint || super.hasEffect(stack); }

 @Override
 public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
  Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
  if (slot == EntityEquipmentSlot.MAINHAND) {
   if (spec.damage != 0) map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", spec.damage, 0));
   if (spec.speed != 0) map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", spec.speed, 0));
  }
  return map;
 }
 @Override public float getDestroySpeed(ItemStack stack, IBlockState state) { return spec.tool ? 128000.0F : super.getDestroySpeed(stack, state); }
 @Override public boolean canHarvestBlock(IBlockState state) { return spec.tool || super.canHarvestBlock(state); }
 @Override public boolean hasCustomEntity(ItemStack stack) { return spec.fireproof; }
 @Override public Entity createEntity(World world, Entity location, ItemStack stack) {
  if (!spec.fireproof) return null;
  return new FireproofItemEntity(world, location, stack);
 }
 @Override
 public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
  ItemStack stack = player.getHeldItem(hand);
  if (!world.isRemote) ItemHooks.rightClick(spec.id, stack, world, player);
  return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
 }
 @Override
 public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
  if (!attacker.world.isRemote) ItemHooks.hit(spec.id, stack, target, attacker);
  return true;
 }
 @Override
 public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
  super.onUpdate(stack, world, entity, slot, selected);
  if (!world.isRemote && entity instanceof EntityLivingBase) ItemHooks.tick(spec.id, stack, world, (EntityLivingBase) entity, selected);
 }
 @Override
 public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
  if (!world.isRemote) ItemHooks.blockDestroyed(spec.id, stack, world, pos, entity);
  return true;
 }
 @Override
 public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack) {
  if (!entity.world.isRemote) ItemHooks.swing(spec.id, stack, entity);
  return false;
 }
}
