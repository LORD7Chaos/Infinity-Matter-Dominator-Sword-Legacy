package com.lord.waa.logic;

import com.lord.waa.config.WaaConfig;
import com.lord.waa.registry.ModEnchantments;
import com.lord.waa.registry.ModPotions;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public final class GodMode {
 public final boolean attributes, haste, soulParticles, removes, chain, xp, strip, unlock, particlesB;
 public final int absorption;
 public final boolean evolved;
 private GodMode(boolean attributes, boolean haste, boolean soulParticles, int absorption, boolean removes, boolean chain, boolean xp, boolean strip, boolean unlock, boolean particlesB, boolean evolved) {
  this.attributes = attributes; this.haste = haste; this.soulParticles = soulParticles; this.absorption = absorption; this.removes = removes;
  this.chain = chain; this.xp = xp; this.strip = strip; this.unlock = unlock; this.particlesB = particlesB; this.evolved = evolved;
 }
 public static final GodMode AINV         = new GodMode(true, true,  true,  19,  true,  true,  true,  true,  false, false, false);
 public static final GodMode CHRINV       = new GodMode(true, false, true,  19,  true,  true,  true,  true,  false, false, false);
 public static final GodMode CHWI         = new GodMode(true, false, true,  19,  true,  true,  true,  false, false, false, false);
 public static final GodMode IFMARINV     = new GodMode(true, true,  true,  19,  true,  true,  true,  true,  true,  false, false);
 public static final GodMode EVIFAARINV   = new GodMode(true, true,  true,  19,  true,  true,  true,  true,  true,  false, false);
 public static final GodMode TWOEIGINVVV  = new GodMode(true, true,  false, 19,  true,  true,  true,  false, true,  false, false);
 public static final GodMode EIFUDAEDSI   = new GodMode(true, true,  false, 255, true,  false, true,  false, true,  true,  true);
 public static final GodMode IMBSINV      = new GodMode(true, true,  false, 255, true,  false, true,  false, true,  true,  false);
 public static final GodMode AAA          = new GodMode(true, true,  false, 255, true,  false, true,  false, true,  false, false);
 public static final GodMode FINAL        = new GodMode(true, false, true,  19,  false, false, false, false, false, false, false);
 public static final GodMode LAST         = new GodMode(true, false, true,  19,  true,  false, false, false, false, false, false);
 public void apply(EntityLivingBase e, ItemStack stack) {
  World w = e.world;
  if (w.isRemote) return;
  double x = e.posX, y = e.posY, z = e.posZ;
  EntityPlayer p = e instanceof EntityPlayer ? (EntityPlayer) e : null;
  if (attributes && p != null) GodAttributes.apply(p, true);
  e.setHealth(1000.0F);
  if (soulParticles) {
   Fx.particle(w, EnumParticleTypes.END_ROD, x, y, z, 5, 3, 3, 3, 0.0);
   Fx.particle(w, EnumParticleTypes.PORTAL, x, y, z, 3, 3, 3, 3, 0.0);
  }
  if (particlesB) {
   Fx.particle(w, EnumParticleTypes.END_ROD, x, y, z, 6, 1, 1, 1, 0.3);
   Fx.particle(w, EnumParticleTypes.ENCHANTMENT_TABLE, x, y, z, 6, 1, 1, 1, 1.0);
  }
  Fx.eff(e, MobEffects.SPEED, Fx.LONG, 6);
  Fx.eff(e, MobEffects.JUMP_BOOST, Fx.LONG, 6);
  Fx.eff(e, MobEffects.STRENGTH, Fx.LONG, 255);
  Fx.eff(e, MobEffects.REGENERATION, Fx.LONG, 255);
  Fx.eff(e, MobEffects.RESISTANCE, Fx.LONG, 255);
  Fx.eff(e, MobEffects.FIRE_RESISTANCE, Fx.LONG, 255);
  Fx.eff(e, MobEffects.WATER_BREATHING, Fx.LONG, 255);
  Fx.eff(e, MobEffects.NIGHT_VISION, Fx.LONG, 255);
  Fx.eff(e, MobEffects.ABSORPTION, Fx.LONG, absorption);
  Fx.eff(e, MobEffects.SATURATION, Fx.LONG, 255);
  Fx.eff(e, MobEffects.LUCK, Fx.LONG, 255);
  if (haste) Fx.eff(e, MobEffects.HASTE, Fx.LONG, 255);
  Fx.eff(e, ModPotions.DIVINE_POWER, Fx.LONG, 255);
  if (evolved) Fx.eff(e, ModPotions.EVOLVED_POWER, Fx.LONG, 255);
  if (p != null) {
   Fx.abilities(p, true, true, true);
   if (WaaConfig.godModeSetsKeepInventory && !w.getGameRules().getBoolean("keepInventory")) w.getGameRules().setOrCreateGameRule("keepInventory", "true");
  }
  if (removes) {
   Fx.unEff(e, ModPotions.REJECT); Fx.unEff(e, ModPotions.KILL);
   Fx.unEff(e, MobEffects.SLOWNESS); Fx.unEff(e, MobEffects.MINING_FATIGUE); Fx.unEff(e, MobEffects.NAUSEA);
   Fx.unEff(e, MobEffects.BLINDNESS); Fx.unEff(e, MobEffects.HUNGER); Fx.unEff(e, MobEffects.POISON);
   Fx.unEff(e, MobEffects.WEAKNESS); Fx.unEff(e, MobEffects.WITHER); Fx.unEff(e, MobEffects.LEVITATION); Fx.unEff(e, MobEffects.UNLUCK);
  }
  if (chain) {
   Procs.waaSwordHandTick(e);
   FINAL.apply(e, stack);
   LAST.apply(e, stack);
  }
  if (p != null) {
   if (xp) Fx.xpMax(p);
   if (unlock && p instanceof EntityPlayerMP && p.ticksExisted % WaaConfig.unlockIntervalTicks == 0) Unlocks.grantEverything((EntityPlayerMP) p);
  }
  if (strip && stack != null && !stack.isEmpty() && stack.isItemEnchanted()) stripEnchantments(stack);
 }
 private static void stripEnchantments(ItemStack stack) {
  Map<Enchantment, Integer> m = EnchantmentHelper.getEnchantments(stack);
  boolean changed = false;
  for (Enchantment en : new java.util.ArrayList<Enchantment>(m.keySet())) {
   if (en != ModEnchantments.IMMEDIATE_MINING) { m.remove(en); changed = true; }
  }
  if (changed) EnchantmentHelper.setEnchantments(m, stack);
 }
}
