package com.lord.waa.item;

import com.google.common.collect.Multimap;
import com.lord.waa.Reference;
import com.lord.waa.logic.Procs;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class WaaArmor extends ItemArmor {
 public static final ItemArmor.ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("WAA_INFINITY", Reference.MOD_ID + ":infinity", 1024,
   new int[] {1024, 1024, 1024, 1024}, 20, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 10.0F);
 private static final UUID[] KB = {UUID.fromString("c1b4a4e0-1f0e-4a50-8d3b-0a0a0a0a0001"), UUID.fromString("c1b4a4e0-1f0e-4a50-8d3b-0a0a0a0a0002"),
   UUID.fromString("c1b4a4e0-1f0e-4a50-8d3b-0a0a0a0a0003"), UUID.fromString("c1b4a4e0-1f0e-4a50-8d3b-0a0a0a0a0004")};
 private final EntityEquipmentSlot slotType;

 public WaaArmor(String id, EntityEquipmentSlot slot) {
  super(MATERIAL, 0, slot);
  this.slotType = slot;
  setRegistryName(Reference.MOD_ID, id);
  setTranslationKey(Reference.MOD_ID + "." + id);
 }

 @Override
 public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
  Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
  if (slot == slotType) map.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(KB[slot.getIndex()], "Armor knockback resistance", 5.0D, 0));
  return map;
 }

 @Override
 public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
  if (!world.isRemote && slotType == EntityEquipmentSlot.HEAD) Procs.infinityBodyTick(player);
 }

 @Override public boolean hasCustomEntity(ItemStack stack) { return true; }
 @Override public Entity createEntity(World world, Entity location, ItemStack stack) {
  return new FireproofItemEntity(world, location, stack);
 }

 @Override
 public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
  return Reference.MOD_ID + ":textures/models/armor/paa_layer_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
 }
}
