package com.lord.waa.logic;

import com.lord.waa.config.WaaConfig;
import com.lord.waa.registry.ModPotions;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public final class Fx {
 private Fx() {}
 public static final float MAXI = (float) Integer.MAX_VALUE;
 public static final int LONG = 1000000;

 public static void void2(Entity e) {
  e.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.POSITIVE_INFINITY);
  e.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.POSITIVE_INFINITY);
 }
 public static void void3(Entity e) {
  e.attackEntityFrom(DamageSource.OUT_OF_WORLD, MAXI);
  void2(e);
 }
 public static void generic(Entity e, int times) { for (int i = 0; i < times; i++) e.attackEntityFrom(DamageSource.GENERIC, Float.POSITIVE_INFINITY); }
 public static void magic(Entity e, int times) { for (int i = 0; i < times; i++) e.attackEntityFrom(DamageSource.MAGIC, Float.POSITIVE_INFINITY); }

 public static void hp0(Entity e) { if (e instanceof EntityLivingBase) ((EntityLivingBase) e).setHealth(0.0F); }
 public static void ext(Entity e) { e.extinguish(); }
 public static void fall(Entity e) { e.fallDistance = MAXI; }

 public static void eff(Entity e, Potion p, int duration, int amp) {
  if (e instanceof EntityLivingBase) ((EntityLivingBase) e).addPotionEffect(new PotionEffect(p, duration, amp, false, false));
 }
 public static void effAmbient(Entity e, Potion p, int duration, int amp) {
  if (e instanceof EntityLivingBase) ((EntityLivingBase) e).addPotionEffect(new PotionEffect(p, duration, amp, true, false));
 }
 public static void unEff(Entity e, Potion p) { if (e instanceof EntityLivingBase && p != null) ((EntityLivingBase) e).removePotionEffect(p); }
 public static void killReject(Entity e) {
  eff(e, ModPotions.KILL, LONG, 255);
  eff(e, ModPotions.REJECT, LONG, 255);
 }
 public static void fullKill(Entity e) { ext(e); void2(e); hp0(e); killReject(e); }

 public static List<Entity> near(World w, double x, double y, double z, double r) {
  r = WaaConfig.radius(r);
  List<Entity> out = new ArrayList<Entity>();
  for (Entity e : new ArrayList<Entity>(w.loadedEntityList)) {
   if (Math.abs(e.posX - x) <= r && Math.abs(e.posY - y) <= r && Math.abs(e.posZ - z) <= r) out.add(e);
  }
  return out;
 }
 public static List<EntityLivingBase> nearLiving(World w, double x, double y, double z, double r, Entity except, boolean includePlayers) {
  List<EntityLivingBase> out = new ArrayList<EntityLivingBase>();
  for (Entity e : near(w, x, y, z, r)) {
   if (!(e instanceof EntityLivingBase) || e == except) continue;
   if (!includePlayers && e instanceof EntityPlayer) continue;
   out.add((EntityLivingBase) e);
  }
  return out;
 }
 public static List<Entity> allNonPlayers() {
  List<Entity> out = new ArrayList<Entity>();
  for (World w : DimensionManager.getWorlds()) for (Entity e : new ArrayList<Entity>(w.loadedEntityList)) if (!(e instanceof EntityPlayer)) out.add(e);
  return out;
 }
 public static void effectAll() {
  for (Entity e : allNonPlayers()) if (e instanceof EntityLivingBase) killReject(e);
 }
 public static EntityPlayer nearestPlayer(World w, double x, double y, double z) { return w.getClosestPlayer(x, y, z, -1.0D, false); }

 public static void lightning(World w, double x, double y, double z, int n) {
  for (int i = 0; i < n; i++) w.addWeatherEffect(new EntityLightningBolt(w, x, y, z, true));
 }
 public static void explode(World w, double x, double y, double z, float power, boolean destroy) {
  if (w.isRemote) return;
  if (destroy) Boom.explode(w, x, y, z, power);
  else w.newExplosion(null, x, y, z, power, false, false);
 }
 public static void particle(World w, EnumParticleTypes t, double x, double y, double z, int n, double dx, double dy, double dz, double speed) {
  if (w instanceof WorldServer) ((WorldServer) w).spawnParticle(t, x, y, z, n, dx, dy, dz, speed);
 }
 public static void sound(World w, double x, double y, double z, String name, float vol, float pitch) {
  SoundEvent s = SoundEvent.REGISTRY.getObject(new ResourceLocation(name));
  if (s != null) w.playSound(null, x, y, z, s, SoundCategory.NEUTRAL, vol, pitch);
 }

 public static void xpMax(EntityPlayer p) {
  if (p.experienceLevel != Integer.MAX_VALUE) {
   p.experienceLevel = Integer.MAX_VALUE;
   p.experienceTotal = Integer.MAX_VALUE;
   p.experience = 0.0F;
  }
 }
 public static void abilities(EntityPlayer p, boolean allowFlying, boolean allowEdit, boolean disableDamage) {
  boolean changed = false;
  if (allowFlying && !p.capabilities.allowFlying) { p.capabilities.allowFlying = true; changed = true; }
  if (allowEdit && !p.capabilities.allowEdit) { p.capabilities.allowEdit = true; changed = true; }
  if (disableDamage && !p.capabilities.disableDamage) { p.capabilities.disableDamage = true; changed = true; }
  if (changed) p.sendPlayerAbilities();
 }
 public static void setBase(EntityLivingBase e, net.minecraft.entity.ai.attributes.IAttribute a, double v) {
  net.minecraft.entity.ai.attributes.IAttributeInstance inst = e.getEntityAttribute(a);
  if (inst != null && inst.getBaseValue() != v) inst.setBaseValue(v);
 }
 public static void setBaseByName(EntityLivingBase e, String name, double v) {
  net.minecraft.entity.ai.attributes.IAttributeInstance inst = e.getAttributeMap().getAttributeInstanceByName(name);
  if (inst != null && inst.getBaseValue() != v) inst.setBaseValue(v);
 }
}
