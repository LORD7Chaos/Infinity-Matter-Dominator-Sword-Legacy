package com.lord.waa.registry;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "waa")
public final class RegistryHandler {
 private RegistryHandler() {}

 @SubscribeEvent
 public static void blocks(RegistryEvent.Register<Block> e) {
  ModBlocks.create();
  for (Block b : ModBlocks.all().values()) e.getRegistry().register(b);
 }

 @SubscribeEvent
 public static void items(RegistryEvent.Register<Item> e) {
  ModBlocks.create();
  ModItems.create();
  for (Item i : ModItems.all().values()) e.getRegistry().register(i);
  for (Block b : ModBlocks.all().values()) e.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
 }

 @SubscribeEvent
 public static void potions(RegistryEvent.Register<Potion> e) {
  for (Potion p : ModPotions.all()) e.getRegistry().register(p);
 }

 @SubscribeEvent
 public static void enchantments(RegistryEvent.Register<Enchantment> e) {
  e.getRegistry().register(ModEnchantments.IMMEDIATE_MINING);
 }

 private static String migrated(ResourceLocation key) {
  if (!"waa".equals(key.getNamespace())) return null;
  if (key.getPath().equals("ancient_debris")) return "finallium_debris";
  if (!key.getPath().startsWith("netherite_")) return null;
  return "finallium_" + key.getPath().substring("netherite_".length());
 }

 @SubscribeEvent
 public static void remapItems(RegistryEvent.MissingMappings<Item> e) {
  for (RegistryEvent.MissingMappings.Mapping<Item> m : e.getMappings()) {
   String n = migrated(m.key);
   Item target = n == null ? null : ModItems.get(n);
   if (target == null && n != null && ModBlocks.get(n) != null) target = Item.getItemFromBlock(ModBlocks.get(n));
   if (target != null) m.remap(target);
  }
 }

 @SubscribeEvent
 public static void remapBlocks(RegistryEvent.MissingMappings<Block> e) {
  for (RegistryEvent.MissingMappings.Mapping<Block> m : e.getMappings()) {
   String n = migrated(m.key);
   Block target = n == null ? null : ModBlocks.get(n);
   if (target != null) m.remap(target);
  }
 }
}
