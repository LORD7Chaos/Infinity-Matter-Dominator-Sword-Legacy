package com.lord.waa.registry;

import com.lord.waa.Reference;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public final class ModEnchantments {
 private ModEnchantments() {}

 public static final Enchantment IMMEDIATE_MINING = new Enchantment(Enchantment.Rarity.COMMON, EnumEnchantmentType.BREAKABLE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}) {
  @Override public int getMaxLevel() { return 1; }
  @Override public int getMinEnchantability(int level) { return 1; }
  @Override public int getMaxEnchantability(int level) { return 100; }
  @Override public boolean isTreasureEnchantment() { return true; }
  @Override public boolean canApplyAtEnchantingTable(net.minecraft.item.ItemStack stack) { return false; }
 }.setName(Reference.MOD_ID + ".immediatemining");
 static { IMMEDIATE_MINING.setRegistryName(Reference.MOD_ID, "immediatemining"); }
}
