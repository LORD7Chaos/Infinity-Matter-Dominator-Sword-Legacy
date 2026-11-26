package com.lord.waa.logic;

import com.lord.waa.registry.ModEnchantments;
import com.lord.waa.registry.ModItems;
import com.lord.waa.registry.ModPotions;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.GameType;

public final class Procs {
 private Procs() {}

 public static void potionStart(String id, EntityLivingBase e) { potion(id, e, true); }
 public static void potionTick(String id, EntityLivingBase e) { potion(id, e, false); }
 private static void potion(String id, EntityLivingBase e, boolean start) {
  if (e.world.isRemote) return;
  switch (id) {
   case "chaotic_wither": e.attackEntityFrom(net.minecraft.util.DamageSource.OUT_OF_WORLD, start ? 1000.0F : 100.0F); break;
   case "reject": Fx.generic(e, 5); Fx.magic(e, 5); e.setHealth(0.0F); break;
   case "kill": Fx.void2(e); e.setHealth(0.0F); break;
   case "divine_power":
    e.setHealth(1000.0F);
    if (e instanceof EntityPlayer) Fx.abilities((EntityPlayer) e, true, true, false);
    break;
   case "evolved_infinity_final_ultimate_divine_end_dominator_power": GodMode.AAA.apply(e, ItemStack.EMPTY); break;
   default: break;
  }
 }

 public static void infinityBodyTick(EntityLivingBase e) {
  if (e instanceof EntityPlayer) GodAttributes.apply((EntityPlayer) e, false);
  e.setHealth(1000.0F);
 }

 public static void evolvedPowerAttacked(EntityLivingBase victim) {
  World w = victim.world;
  for (EntityLivingBase t : Fx.nearLiving(w, victim.posX, victim.posY, victim.posZ, 2500, victim, true)) { Fx.void3(t); Fx.killReject(t); Fx.ext(t); Fx.fall(t); Fx.hp0(t); }
 }

 public static void handBasic(EntityLivingBase e) {
  e.setHealth(1000.0F);
  Fx.particle(e.world, EnumParticleTypes.END_ROD, e.posX, e.posY, e.posZ, 5, 4, 4, 4, 0.4);
 }
 public static void ultimateHandParticles(EntityLivingBase e) {
  Fx.particle(e.world, EnumParticleTypes.PORTAL, e.posX, e.posY, e.posZ, 5, 3, 3, 3, 1.0);
 }
 public static void waaSwordHandTick(EntityLivingBase e) {
  Fx.effAmbient(e, MobEffects.STRENGTH, Fx.LONG, 255);
  Fx.effAmbient(e, MobEffects.REGENERATION, Fx.LONG, 255);
  Fx.effAmbient(e, MobEffects.RESISTANCE, Fx.LONG, 255);
  Fx.effAmbient(e, MobEffects.FIRE_RESISTANCE, Fx.LONG, 255);
  Fx.effAmbient(e, MobEffects.WATER_BREATHING, Fx.LONG, 255);
  Fx.effAmbient(e, MobEffects.NIGHT_VISION, Fx.LONG, 255);
  Fx.effAmbient(e, MobEffects.SPEED, Fx.LONG, 6);
  Fx.effAmbient(e, MobEffects.JUMP_BOOST, Fx.LONG, 6);
  Fx.effAmbient(e, MobEffects.SATURATION, Fx.LONG, 255);
  if (e instanceof EntityPlayer) {
   EntityPlayer p = (EntityPlayer) e;
   GodAttributes.apply(p, true);
   Fx.abilities(p, true, false, true);
  }
  e.setHealth(1000.0F);
  e.setNoGravity(false);
 }
 public static void waaSwordParticles(EntityLivingBase e) {
  Fx.particle(e.world, EnumParticleTypes.END_ROD, e.posX, e.posY, e.posZ, 4, 2, 2, 2, 0.0);
  Fx.particle(e.world, EnumParticleTypes.SPELL_WITCH, e.posX, e.posY, e.posZ, 6, 1, 1, 1, 1.0);
  Fx.particle(e.world, EnumParticleTypes.PORTAL, e.posX, e.posY, e.posZ, 6, 1, 1, 1, 0.3);
 }
 public static void waaSwordHandTick2(EntityLivingBase e) {
  Fx.unEff(e, ModPotions.REJECT); Fx.unEff(e, MobEffects.SLOWNESS); Fx.unEff(e, MobEffects.MINING_FATIGUE); Fx.unEff(e, MobEffects.NAUSEA);
  Fx.unEff(e, MobEffects.BLINDNESS); Fx.unEff(e, MobEffects.WEAKNESS); Fx.unEff(e, MobEffects.WITHER); Fx.unEff(e, MobEffects.POISON);
  Fx.unEff(e, MobEffects.HUNGER); Fx.unEff(e, MobEffects.UNLUCK);
  e.setHealth(1000.0F);
  e.setNoGravity(false);
 }
 public static void eha(EntityLivingBase e) {
  World w = e.world; e.setHealth(1000.0F);
  for (EntityLivingBase t : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 3, e, true)) { Fx.void3(t); Fx.hp0(t); }
  for (EntityLivingBase t : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 500, e, true)) {
   t.setNoGravity(true);
   Fx.eff(t, MobEffects.SLOWNESS, Fx.LONG, 100); Fx.eff(t, MobEffects.MINING_FATIGUE, Fx.LONG, 100); Fx.eff(t, MobEffects.WEAKNESS, Fx.LONG, 100);
   Fx.eff(t, ModPotions.CHAOTIC_WITHER, Fx.LONG, 1);
   dominatorDrain(t);
  }
  GodMode.AAA.apply(e, ItemStack.EMPTY);
 }
 public static void imbsha(EntityLivingBase e) {
  World w = e.world; e.setHealth(1000.0F);
  for (EntityLivingBase t : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 3.5, e, true)) Fx.void3(t);
  for (EntityLivingBase t : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 500, e, true)) {
   if (t.getMaxHealth() >= 1000000.0F) { Fx.ext(t); Fx.hp0(t); Fx.void3(t); }
   if (t.getMaxHealth() >= 100.0F && t.getMaxHealth() < 1000000.0F) t.setHealth(t.getHealth() - 5.0F);
   if (t.getHealth() != 1.0F) t.setHealth(t.getHealth() - 1.0F);
   if (t.getHealth() == 1.0F) { Fx.ext(t); Fx.void3(t); Fx.hp0(t); }
  }
 }
 private static void dominatorDrain(EntityLivingBase t) {
  if (t.getMaxHealth() >= 1000000.0F) { Fx.ext(t); Fx.hp0(t); Fx.void3(t); }
  if (t.getMaxHealth() >= 100.0F && t.getMaxHealth() < 1000000.0F) t.setHealth(t.getHealth() - 10.0F);
  if (t.getMaxHealth() < 100.0F) t.setHealth(t.getHealth() - 2.0F);
  if (t.getHealth() == 1.0F) { Fx.ext(t); Fx.void3(t); Fx.hp0(t); Fx.eff(t, ModPotions.KILL, Fx.LONG, 100); Fx.eff(t, ModPotions.REJECT, Fx.LONG, 100); t.fallDistance = 2.14748365E9F; t.setFire(1000000); }
 }
 public static void twoegha(EntityLivingBase e) {
  World w = e.world; e.setHealth(1000.0F);
  for (EntityLivingBase t : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 3.5, e, true)) Fx.void3(t);
  for (EntityLivingBase t : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 500, e, true)) {
   Fx.eff(t, MobEffects.WITHER, 2000, 10); Fx.eff(t, MobEffects.POISON, 2000, 10); t.setFire(100);
  }
 }
 public static void extendedChaotic(ItemStack s) {
  if (s.isItemEnchanted()) return;
  s.addEnchantment(Enchantments.SHARPNESS, 127); s.addEnchantment(Enchantments.SMITE, 127); s.addEnchantment(Enchantments.BANE_OF_ARTHROPODS, 127);
  s.addEnchantment(Enchantments.FIRE_ASPECT, 127); s.addEnchantment(Enchantments.LOOTING, 10); s.addEnchantment(Enchantments.SWEEPING, 3);
 }
 public static void chargedDominatorEnchantments(ItemStack stack) {
  if (stack.isEmpty()) return;
  java.util.Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
  Enchantment[] required = {
    Enchantments.SHARPNESS,
    Enchantments.SMITE,
    Enchantments.BANE_OF_ARTHROPODS,
    Enchantments.LOOTING,
    Enchantments.SWEEPING,
    Enchantments.FORTUNE,
    Enchantments.THORNS,
    Enchantments.FIRE_ASPECT,
    Enchantments.POWER
  };
  int[] levels = {256, 256, 256, 100, 256, 100, 100, 256, 256};
  boolean changed = false;
  for (int i = 0; i < required.length; i++) {
   Integer current = enchants.get(required[i]);
   if (current == null || current.intValue() != levels[i]) {
    enchants.put(required[i], levels[i]);
    changed = true;
   }
  }
  if (changed) EnchantmentHelper.setEnchantments(enchants, stack);
 }

 public static void destroyerOfMatterTick(EntityLivingBase e) {
  Fx.eff(e, MobEffects.HASTE, Fx.LONG, 255); Fx.eff(e, MobEffects.STRENGTH, Fx.LONG, 255);
 }

 public static void ahi(EntityLivingBase t) {
  Fx.void2(t); Fx.hp0(t); Fx.killReject(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, t.posX, t.posY, t.posZ, 121.5, null, false)) { Fx.void2(o); Fx.killReject(o); }
 }
 public static void chunibyoHit(EntityLivingBase t) {
  Fx.void2(t); Fx.hp0(t); Fx.killReject(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, t.posX, t.posY, t.posZ, 40.5, null, false)) Fx.void2(o);
 }
 public static void eh(EntityLivingBase t) {
  Fx.sound(t.world, t.posX, t.posY, t.posZ, "block.enchantment_table.use", 1.0F, 0.0F);
  Fx.ext(t); Fx.void2(t); Fx.hp0(t); Fx.killReject(t); Fx.hp0(t); Fx.fall(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, t.posX, t.posY, t.posZ, 2500, null, false)) { Fx.void3(o); Fx.killReject(o); Fx.ext(o); Fx.fall(o); Fx.hp0(o); }
 }
 public static void esw(EntityLivingBase attacker) {
  for (EntityLivingBase o : Fx.nearLiving(attacker.world, attacker.posX, attacker.posY, attacker.posZ, 5000, attacker, true)) { Fx.void3(o); Fx.ext(o); Fx.killReject(o); Fx.hp0(o); }
  ultraKill(attacker.world, attacker.posX, attacker.posY, attacker.posZ);
 }
 public static void evifmah(EntityLivingBase t) {
  Fx.ext(t); Fx.void2(t); Fx.hp0(t); Fx.killReject(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, t.posX, t.posY, t.posZ, 364.5, null, false)) { Fx.void3(o); Fx.killReject(o); Fx.ext(o); }
 }
 public static void ifmarh(EntityLivingBase t) {
  Fx.void2(t); Fx.hp0(t); Fx.killReject(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, t.posX, t.posY, t.posZ, 364.5, null, false)) { Fx.void3(o); Fx.killReject(o); Fx.ext(o); o.setDead(); }
 }
 public static void twofhit(EntityLivingBase t) {
  Fx.ext(t); Fx.void2(t); Fx.hp0(t); Fx.killReject(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, t.posX, t.posY, t.posZ, 2500, null, false)) { Fx.void3(o); Fx.killReject(o); Fx.ext(o); Fx.fall(o); Fx.hp0(o); }
 }
 public static void imbsh(EntityLivingBase t) {
  Fx.ext(t); Fx.void2(t); Fx.hp0(t); Fx.killReject(t); Fx.hp0(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, t.posX, t.posY, t.posZ, 2500, null, false)) { Fx.void3(o); Fx.killReject(o); Fx.ext(o); Fx.fall(o); Fx.hp0(o); }
 }
 public static void imbssw(EntityLivingBase a) {
  for (EntityLivingBase o : Fx.nearLiving(a.world, a.posX, a.posY, a.posZ, 5000, a, true)) { Fx.void3(o); Fx.ext(o); }
 }
 public static void finalHit(EntityLivingBase t, EntityLivingBase a) {
  Fx.void2(t); Fx.hp0(t); Fx.killReject(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, a.posX, a.posY, a.posZ, 3.5, a, true)) Fx.generic(o, 1);
 }
 public static void evolvedDivineHit(EntityLivingBase t) { Fx.void2(t); Fx.killReject(t); }
 public static void divineHit(EntityLivingBase t) {
  for (int i = 0; i < 20; i++) {
   Fx.hp0(t); Fx.ext(t); Fx.generic(t, 1); t.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
   t.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
   Fx.eff(t, ModPotions.REJECT, Fx.LONG, 255); t.fallDistance = 2.14748365E9F; t.setFire(1000000); t.setAir(0);
  }
 }
 public static void lastHit(EntityLivingBase t, EntityLivingBase a) {
  Fx.void2(t); Fx.hp0(t); Fx.killReject(t);
  for (EntityLivingBase o : Fx.nearLiving(t.world, a.posX, a.posY, a.posZ, 13.5, a, true)) Fx.void2(o);
 }
 public static void ultraKill(World w, double x, double y, double z) {
  for (EntityLivingBase o : Fx.nearLiving(w, x, y, z, 2500, null, false)) { Fx.ext(o); Fx.void3(o); Fx.killReject(o); Fx.hp0(o); }
 }

 public static void chunibyoRC(World w, EntityLivingBase e) {
  Fx.sound(w, e.posX, e.posY, e.posZ, "ambient.cave", 6.0F, 0.0F);
  Fx.sound(w, e.posX, e.posY, e.posZ, "entity.wither.death", 7.0F, 0.0F);
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 2500, e, true)) { Fx.void2(o); Fx.hp0(o); Fx.killReject(o); }
  Fx.ext(e);
  Fx.effectAll();
  if (e instanceof EntityPlayer) Fx.xpMax((EntityPlayer) e);
 }
 public static void rifleRC(World w, EntityLivingBase e) {
  for (int i = 0; i < 3; i++) {
   Projectiles.shoot(w, e, Projectiles.BULLET, 1.0F, Fx.MAXI, 1);
   Projectiles.shoot(w, e, Projectiles.BULLET, 1.0F, 1.0E308D, 1);
   Projectiles.shoot(w, e, Projectiles.BULLET, 1.0F, Double.POSITIVE_INFINITY, 1);
  }
  chunibyoRC(w, e);
 }
 public static void annihilatorRC(World w, EntityLivingBase e) {
  Projectiles.shoot(w, e, Projectiles.GRENADE, 1.0F, Double.POSITIVE_INFINITY, 5);
  for (int i = 0; i < 5; i++) {
   Projectiles.shoot(w, e, Projectiles.BULLET, 1.0F, Fx.MAXI, 1);
   Projectiles.shoot(w, e, Projectiles.BULLET, 1.0F, 1.0E308D, 1);
   Projectiles.shoot(w, e, Projectiles.BULLET, 1.0F, Double.POSITIVE_INFINITY, 1);
  }
  chunibyoRC(w, e);
 }
 public static void chaoticRC(World w, EntityLivingBase e) {
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 4.5, e, true)) o.attackEntityFrom(net.minecraft.util.DamageSource.GENERIC, 10000.0F);
 }
 public static void ec(World w, EntityLivingBase e) {
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 5.5, e, true)) { o.attackEntityFrom(net.minecraft.util.DamageSource.GENERIC, 100000.0F); Fx.eff(o, ModPotions.CHAOTIC_WITHER, Fx.LONG, 1); }
 }
 public static void endRC(World w, EntityLivingBase e) {
  killDragons();
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 5000, e, true)) {
   Fx.particle(w, EnumParticleTypes.EXPLOSION_NORMAL, o.posX, o.posY, o.posZ, 5, 1, 1, 1, 0.1);
   o.setDead();
  }
 }

 private static void killDragons() {
  for (World ww : net.minecraftforge.common.DimensionManager.getWorlds())
   for (Entity d : new java.util.ArrayList<Entity>(ww.loadedEntityList)) if (d instanceof EntityDragon) d.onKillCommand();
 }
 public static void erc(World w, EntityLivingBase e, ItemStack stack) {
  killDragons();
  Projectiles.shoot(w, e, Projectiles.GRENADE, 1.0F, Fx.MAXI, 0);
  Projectiles.shoot(w, e, Projectiles.GRENADE, 1.0F, 1.0E308D, 0);
  Projectiles.shoot(w, e, Projectiles.GRENADE, 1.0F, Double.POSITIVE_INFINITY, 0);
  if (!e.isSneaking()) {
   Fx.sound(w, e.posX, e.posY, e.posZ, "entity.lightning.thunder", 10.0F, 0.0F);
   for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 5000, e, true)) {
    o.setDead();
    Fx.particle(w, EnumParticleTypes.EXPLOSION_NORMAL, o.posX, o.posY, o.posZ, 5, 1, 1, 1, 0.1);
   }
  } else {
   Fx.sound(w, e.posX, e.posY, e.posZ, "block.enchantment_table.use", 1.0F, 1.0F);
   java.util.Map<Enchantment, Integer> m = EnchantmentHelper.getEnchantments(stack);
   if (m.containsKey(ModEnchantments.IMMEDIATE_MINING)) {
    m.remove(ModEnchantments.IMMEDIATE_MINING);
    EnchantmentHelper.setEnchantments(m, stack);
    if (e instanceof EntityPlayer) ((EntityPlayer) e).sendStatusMessage(new net.minecraft.util.text.TextComponentTranslation("message.waa.immediate_mining.off"), true);
   } else {
    stack.addEnchantment(ModEnchantments.IMMEDIATE_MINING, 1);
    if (e instanceof EntityPlayer) ((EntityPlayer) e).sendStatusMessage(new net.minecraft.util.text.TextComponentTranslation("message.waa.immediate_mining.on"), true);
    for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 2500, e, false)) { Fx.void3(o); Fx.killReject(o); Fx.ext(o); Fx.fall(o); Fx.hp0(o); }
   }
  }
 }
 public static void evolvedDivineRC(World w, EntityLivingBase e) {
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 514, e, true)) {
   o.attackEntityFrom(net.minecraft.util.DamageSource.OUT_OF_WORLD, Float.POSITIVE_INFINITY); Fx.hp0(o); Fx.void2(o);
  }
  Fx.effectAll();
  if (e instanceof EntityPlayer) Fx.xpMax((EntityPlayer) e);
  Fx.sound(w, e.posX, e.posY, e.posZ, "entity.lightning.thunder", 10.0F, 0.0F);
  Fx.sound(w, e.posX, e.posY, e.posZ, "entity.wither.death", 10.0F, 0.0F);
 }
 public static void finalRC(World w, EntityLivingBase e) {
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 1024, e, true)) {
   Fx.particle(w, EnumParticleTypes.END_ROD, o.posX, o.posY, o.posZ, 5, 1, 1, 1, 0.1);
   o.attackEntityFrom(net.minecraft.util.DamageSource.OUT_OF_WORLD, Float.POSITIVE_INFINITY); Fx.hp0(o);
   o.attackEntityFrom(net.minecraft.util.DamageSource.OUT_OF_WORLD, Float.POSITIVE_INFINITY); Fx.killReject(o);
  }
  Fx.effectAll();
  if (e instanceof EntityPlayer) Fx.xpMax((EntityPlayer) e);
 }
 public static void divineRC(World w, EntityLivingBase e) {
  Fx.effectAll();
  if (e instanceof EntityPlayer) Fx.xpMax((EntityPlayer) e);
  for (Entity o : Fx.near(w, e.posX, e.posY, e.posZ, 256)) Fx.generic(o, 1);
 }
 public static void lastRC(World w, EntityLivingBase e) {
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 4999.5, e, true)) {
   Fx.particle(w, EnumParticleTypes.END_ROD, o.posX, o.posY, o.posZ, 5, 1, 1, 1, 0.1);
   Fx.void2(o); Fx.hp0(o); Fx.killReject(o);
  }
  Fx.effectAll();
  if (e instanceof EntityPlayer) Fx.xpMax((EntityPlayer) e);
 }
 public static void waaSwordRC(World w, EntityLivingBase e) {
  if (e instanceof EntityPlayer) Fx.xpMax((EntityPlayer) e);
  Fx.sound(w, e.posX, e.posY, e.posZ, "entity.lightning.thunder", 10.0F, 0.0F);
  Fx.effectAll();
  for (int i = 0; i < 10; i++) for (Entity o : Fx.near(w, e.posX, e.posY, e.posZ, 256)) Fx.generic(o, 1);
 }
 public static void obliteratorRC(World w, EntityLivingBase e) {
  for (Entity o : Fx.near(w, e.posX, e.posY, e.posZ, 50000)) if (!(o instanceof EntityPlayer)) { o.setDead(); Fx.hp0(o); }
  lastRC(w, e); evolvedDivineRC(w, e); divineRC(w, e); waaSwordRC(w, e);
 }
 public static void twoegSwing(EntityLivingBase e) {
  World w = e.world;
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 50, e, true)) {
   Fx.eff(o, MobEffects.SLOWNESS, 100000, 255); Fx.eff(o, MobEffects.WITHER, 100000, 255); Fx.eff(o, MobEffects.MINING_FATIGUE, 100000, 255);
   Fx.eff(o, MobEffects.NAUSEA, 100000, 255); Fx.eff(o, MobEffects.BLINDNESS, 100000, 255); Fx.eff(o, MobEffects.HUNGER, 100000, 255);
   Fx.eff(o, MobEffects.WEAKNESS, 100000, 255); Fx.eff(o, MobEffects.POISON, 100000, 255);
  }
  for (EntityLivingBase o : Fx.nearLiving(w, e.posX, e.posY, e.posZ, 15, e, true)) { o.setFire(100000000); Fx.void3(o); Fx.fall(o); }
 }
 public static void dominatorBreak3(World w, BlockPos pos) {
  for (int dx = -1; dx <= 1; dx++) for (int dy = -1; dy <= 1; dy++) for (int dz = -1; dz <= 1; dz++) {
   BlockPos p = pos.add(dx, dy, dz);
   if (!w.isAirBlock(p)) w.destroyBlock(p, true);
  }
 }
}
