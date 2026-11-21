package com.lord.waa.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

public final class GodAttributes {
 private GodAttributes() {}
 private static final UUID HEALTH = UUID.fromString("5a7d1d10-7a11-4f0e-9a41-000000000001");
 private static final UUID ARMOR = UUID.fromString("5a7d1d10-7a11-4f0e-9a41-000000000002");
 private static final UUID TOUGHNESS = UUID.fromString("5a7d1d10-7a11-4f0e-9a41-000000000003");
 private static final UUID REACH = UUID.fromString("5a7d1d10-7a11-4f0e-9a41-000000000004");
 private static final String REACH_NAME = "generic.reachDistance";
 private static final int GRACE_TICKS = 10;

 private static final Map<UUID, Integer> LAST = new HashMap<UUID, Integer>();
 public static void apply(EntityPlayer p, boolean full) {
  put(p, SharedMonsterAttributes.MAX_HEALTH, HEALTH, 980.0D);
  if (full) {
   put(p, SharedMonsterAttributes.ARMOR, ARMOR, 1000.0D);
   put(p, SharedMonsterAttributes.ARMOR_TOUGHNESS, TOUGHNESS, 20.0D);
   IAttributeInstance reach = p.getAttributeMap().getAttributeInstanceByName(REACH_NAME);
   if (reach != null) put(reach, REACH, "Waa reach", 995.0D);
  }
  LAST.put(p.getUniqueID(), p.ticksExisted);
 }
 private static void put(EntityPlayer p, IAttribute a, UUID id, double amount) {
  IAttributeInstance inst = p.getEntityAttribute(a);
  if (inst != null) put(inst, id, "Waa god mode", amount);
 }
 private static void put(IAttributeInstance inst, UUID id, String name, double amount) {
  if (inst.getModifier(id) != null) return;
  inst.applyModifier(new AttributeModifier(id, name, amount, 0).setSaved(false));
 }
 public static void maintain(EntityPlayer p) {
  Integer last = LAST.get(p.getUniqueID());
  if (last == null) return;
  int now = p.ticksExisted;
  if (now >= last && now - last <= GRACE_TICKS) return;
  remove(p);
  LAST.remove(p.getUniqueID());
 }
 public static void remove(EntityPlayer p) {
  strip(p.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH), HEALTH);
  strip(p.getEntityAttribute(SharedMonsterAttributes.ARMOR), ARMOR);
  strip(p.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS), TOUGHNESS);
  strip(p.getAttributeMap().getAttributeInstanceByName(REACH_NAME), REACH);
  if (p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
 }

 public static void forget(EntityPlayer p) { LAST.remove(p.getUniqueID()); }
 private static void strip(IAttributeInstance inst, UUID id) {
  if (inst != null && inst.getModifier(id) != null) inst.removeModifier(id);
 }
 public static void cleanLegacy(EntityPlayer p) {
  reset(p.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED), 2147483647.0D, 4.0D);
  reset(p.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE), 2147483647.0D, 1.0D);
  reset(p.getEntityAttribute(SharedMonsterAttributes.ARMOR), 2147483647.0D, 0.0D);
  reset(p.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS), 2147483647.0D, 0.0D);
  reset(p.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH), 1000.0D, 20.0D);
  reset(p.getAttributeMap().getAttributeInstanceByName(REACH_NAME), 10000.0D, 5.0D);
  if (p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
 }
 private static void reset(IAttributeInstance inst, double bad, double normal) {
  if (inst != null && inst.getBaseValue() == bad) inst.setBaseValue(normal);
 }
}
