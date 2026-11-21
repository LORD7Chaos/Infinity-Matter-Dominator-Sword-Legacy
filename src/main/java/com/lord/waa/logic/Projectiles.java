package com.lord.waa.logic;

import com.lord.waa.entity.EntityWaaArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class Projectiles {
 private Projectiles() {}
 public static final int BULLET = 0, DESTROYER_OF_CHUNIBYO = 1, DIVINE_ARROW = 2, DIVINE_POWER = 3, EVOLVED_DIVINE_P = 4,
   ANNIHILATOR = 5, DESTROYER = 6, MATTER_ARROW = 7, GRENADE = 8;
 public static final String[] ITEM = {"chunibyo_bullet", "destroyer_of_chunibyo", "divine_arrow", "divinepower", "evolved_divine_p",
   "infinity_annihilator", "infinity_destroyer", "infinitymatterarrow", "infinitymattergrenade"};

 public static void shoot(World w, EntityLivingBase shooter, int kind, float power, double damage, int knockback) {
  if (w.isRemote) return;
  EntityWaaArrow a = new EntityWaaArrow(w, shooter, kind);
  Vec3d look = shooter.getLookVec();
  a.shoot(look.x, look.y, look.z, power * 2.0F, 0.0F);
  a.setDamage(damage);
  a.setKnockbackStrength(knockback);
  a.pickupStatus = net.minecraft.entity.projectile.EntityArrow.PickupStatus.DISALLOWED;
  w.spawnEntity(a);
 }

 public static void hitEntity(int kind, EntityWaaArrow arrow, Entity hit, EntityLivingBase shooter) {
  World w = arrow.world;
  if (w.isRemote) return;
  double x = hit.posX, y = hit.posY, z = hit.posZ;
  switch (kind) {
   case BULLET:
    Fx.void3(hit); Fx.hp0(hit); Fx.ext(hit); Fx.killReject(hit);
    for (EntityLivingBase o : Fx.nearLiving(w, x, y, z, 5000, null, false)) { Fx.void3(o); Fx.hp0(o); Fx.ext(o); Fx.killReject(o); }
    break;
   case DESTROYER_OF_CHUNIBYO:
    Fx.void2(hit); Fx.ext(hit); Fx.hp0(hit); Fx.killReject(hit);
    for (EntityLivingBase o : Fx.nearLiving(w, x, y, z, 40.5, null, false)) { Fx.void2(o); Fx.ext(o); Fx.hp0(o); Fx.killReject(o); }
    Fx.explode(w, x, y, z, 8.0F, true);
    break;
   case DIVINE_ARROW:
    Fx.void2(hit); Fx.ext(hit); Fx.hp0(hit);
    break;
   case DIVINE_POWER:
    divinePowerBlast(w, x, y, z);
    break;
   case EVOLVED_DIVINE_P:
    Fx.void2(hit);
    break;
   case ANNIHILATOR:
    Fx.void2(hit); Fx.ext(hit); Fx.hp0(hit); Fx.killReject(hit);
    Fx.explode(w, x, y, z, 40.0F, true);
    break;
   case DESTROYER:
    Fx.void2(hit); Fx.ext(hit); Fx.hp0(hit); Fx.killReject(hit);
    for (EntityLivingBase o : Fx.nearLiving(w, x, y, z, 121.5, null, false)) { Fx.void2(o); Fx.ext(o); Fx.hp0(o); Fx.killReject(o); }
    Fx.explode(w, x, y, z, 12.0F, true);
    break;
   case MATTER_ARROW:
    for (int i = 0; i < 5; i++) hit.attackEntityFrom(net.minecraft.util.DamageSource.GENERIC, Float.POSITIVE_INFINITY);
    for (int i = 0; i < 6; i++) hit.attackEntityFrom(net.minecraft.util.DamageSource.MAGIC, Float.POSITIVE_INFINITY);
    Fx.hp0(hit);
    break;
   case GRENADE:
    Fx.hp0(hit); Fx.eff(hit, com.lord.waa.registry.ModPotions.REJECT, Fx.LONG, 255);
    for (int i = 0; i < 10; i++) hit.attackEntityFrom(net.minecraft.util.DamageSource.GENERIC, Float.POSITIVE_INFINITY);
    if (hit instanceof EntityPlayer) Fx.explode(w, x, y, z, 30.0F, true);
    break;
   default: break;
  }
 }

 public static void hitBlock(int kind, EntityWaaArrow arrow, double x, double y, double z, EntityLivingBase shooter) {
  World w = arrow.world;
  if (w.isRemote) return;
  switch (kind) {
   case BULLET:
    for (EntityLivingBase o : Fx.nearLiving(w, x, y, z, 5000, shooter, true)) { Fx.void3(o); Fx.hp0(o); Fx.ext(o); Fx.killReject(o); }
    Fx.effectAll();
    break;
   case DESTROYER_OF_CHUNIBYO:
    for (EntityLivingBase o : Fx.nearLiving(w, x, y, z, 40.5, null, false)) { Fx.void3(o); Fx.hp0(o); Fx.void3(o); }
    Fx.explode(w, x, y, z, 8.0F, true);
    break;
   case DIVINE_ARROW:
    Fx.explode(w, x, y, z, 8.0F, false);
    Fx.lightning(w, x, y, z, 10);
    break;
   case DIVINE_POWER:
    divinePowerBlast(w, x, y, z);
    break;
   case ANNIHILATOR:
    for (EntityLivingBase o : Fx.nearLiving(w, x, y, z, 5000, null, false)) { Fx.void3(o); Fx.hp0(o); Fx.void3(o); Fx.killReject(o); }
    Fx.explode(w, x, y, z, 40.0F, true);
    Fx.lightning(w, x, y, z, 5);
    break;
   case DESTROYER:
    for (EntityLivingBase o : Fx.nearLiving(w, x, y, z, 121.5, null, false)) { Fx.void3(o); Fx.killReject(o); Fx.hp0(o); }
    Fx.explode(w, x, y, z, 12.0F, true);
    break;
   case GRENADE:
    Fx.explode(w, x, y, z, 40.0F, true);
    break;
   default: break;
  }
 }

 private static void divinePowerBlast(World w, double x, double y, double z) {
  Fx.explode(w, x, y, z, 40.0F, true);
  for (Entity o : Fx.near(w, x, y, z, 50)) o.attackEntityFrom(net.minecraft.util.DamageSource.GENERIC, Float.POSITIVE_INFINITY);
  Fx.lightning(w, x, y, z, 8);
 }

 public static void flightTick(int kind, EntityWaaArrow arrow) {
  if (arrow.world.isRemote) return;
  if (kind == MATTER_ARROW) arrow.setNoGravity(true);
  if (kind == GRENADE) {
   arrow.setNoGravity(true);
   if (com.lord.waa.config.WaaConfig.grenadeFlightExplosions && arrow.ticksExisted % com.lord.waa.config.WaaConfig.grenadeFlightInterval == 0) Fx.explode(arrow.world, arrow.posX, arrow.posY, arrow.posZ, 40.0F, true);
  }
 }
}
