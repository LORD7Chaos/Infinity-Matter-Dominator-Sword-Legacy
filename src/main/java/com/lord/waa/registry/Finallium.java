package com.lord.waa.registry;

import com.lord.waa.Reference;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public final class Finallium {
 private Finallium() {}
 private static final ToolMaterial TOOL = EnumHelper.addToolMaterial("WAA_FINALLIUM", 4, 2031, 9.0F, 4.0F, 15);
 private static final ItemArmor.ArmorMaterial ARMOR = EnumHelper.addArmorMaterial("WAA_FINALLIUM_ARMOR", Reference.MOD_ID + ":finallium_", 37,
   new int[] {3, 6, 8, 3}, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F);
 private static Item tag(Item i, String id, CreativeTabs tab) {
  i.setRegistryName(Reference.MOD_ID, id);
  i.setTranslationKey(Reference.MOD_ID + "." + id);
  i.setCreativeTab(tab);
  return i;
 }
 private static Item armor(String id, final EntityEquipmentSlot slot) {
  return tag(new ItemArmor(ARMOR, 0, slot) {
   @Override public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot s, String type) {
    return Reference.MOD_ID + ":textures/models/armor/finallium_layer_" + (s == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
   }
  }, id, CreativeTabs.COMBAT);
 }
 static void createItems(Map<String, Item> map) {
  map.put("finallium_scrap", tag(new Item(), "finallium_scrap", CreativeTabs.MATERIALS));
  map.put("finallium_ingot", tag(new Item(), "finallium_ingot", CreativeTabs.MATERIALS));
  map.put("finallium_sword", tag(new ItemSword(TOOL), "finallium_sword", CreativeTabs.COMBAT));
  map.put("finallium_pickaxe", tag(new ItemPickaxe(TOOL) {}, "finallium_pickaxe", CreativeTabs.TOOLS));
  map.put("finallium_axe", tag(new ItemAxe(TOOL, 10.0F, -3.0F) {}, "finallium_axe", CreativeTabs.TOOLS));
  map.put("finallium_shovel", tag(new ItemSpade(TOOL), "finallium_shovel", CreativeTabs.TOOLS));
  map.put("finallium_hoe", tag(new ItemHoe(TOOL), "finallium_hoe", CreativeTabs.TOOLS));
  map.put("finallium_helmet", armor("finallium_helmet", EntityEquipmentSlot.HEAD));
  map.put("finallium_chestplate", armor("finallium_chestplate", EntityEquipmentSlot.CHEST));
  map.put("finallium_leggings", armor("finallium_leggings", EntityEquipmentSlot.LEGS));
  map.put("finallium_boots", armor("finallium_boots", EntityEquipmentSlot.FEET));
 }
 static void createBlocks(Map<String, Block> map) {
  map.put("finallium_debris", block("finallium_debris", 30.0F, 1200.0F, MapColor.BROWN));
  map.put("finallium_block", block("finallium_block", 50.0F, 1200.0F, MapColor.GRAY));
 }
 private static Block block(String id, float hardness, float resistance, MapColor color) {
  Block b = new Block(Material.IRON, color) {
   { setSoundType(SoundType.METAL); }
  };
  b.setRegistryName(Reference.MOD_ID, id);
  b.setTranslationKey(Reference.MOD_ID + "." + id);
  b.setHardness(hardness);
  b.setResistance(resistance);
  b.setHarvestLevel("pickaxe", 3);
  b.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  return b;
 }
}
