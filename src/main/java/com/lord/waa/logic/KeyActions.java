package com.lord.waa.logic;

import com.lord.waa.registry.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;

public final class KeyActions {
 private KeyActions() {}
 public static final int BUG_STRIKE = 0, GM_ADVENTURE = 1, GM_CREATIVE = 2, GM_SURVIVAL = 3, GM_SPECTATOR = 4, DESTROYER_OF_ALL = 5,
   END = 6, ENTER_ATTACK = 7, INFINITY_KILLER = 8, TWOEIG_ATTACK = 9;

 private static boolean has(EntityPlayerMP p, String id) {
  Item item = ModItems.get(id);
  return item != null && p.inventory.hasItemStack(new ItemStack(item));
 }

 private static void volley(EntityPlayerMP p, int times) {
  for (int i = 0; i < times; i++) {
   Projectiles.shoot(p.world, p, Projectiles.ANNIHILATOR, 100.0F, Fx.MAXI, 1);
   Projectiles.shoot(p.world, p, Projectiles.ANNIHILATOR, 100.0F, 1.0E308D, 1);
   Projectiles.shoot(p.world, p, Projectiles.GRENADE, 100.0F, Double.POSITIVE_INFINITY, 1);
  }
 }

 public static void perform(EntityPlayerMP p, int key) {
  switch (key) {
   case BUG_STRIKE:
    if (has(p, "dominator")) for (int i = 0; i < 42; i++) Projectiles.shoot(p.world, p, Projectiles.ANNIHILATOR, 100.0F, Double.POSITIVE_INFINITY, 5);
    break;
   case GM_ADVENTURE: if (has(p, "dominator_infinity")) p.setGameType(GameType.ADVENTURE); break;
   case GM_CREATIVE: if (has(p, "dominator_infinity")) p.setGameType(GameType.CREATIVE); break;
   case GM_SURVIVAL: if (has(p, "dominator_infinity")) p.setGameType(GameType.SURVIVAL); break;
   case GM_SPECTATOR: if (has(p, "dominator_infinity")) p.setGameType(GameType.SPECTATOR); break;
   case DESTROYER_OF_ALL: if (has(p, "the_weapon_of_end_god")) volley(p, 10); break;
   case END: if (has(p, "the_sword_of_end")) volley(p, 8); break;
   case ENTER_ATTACK:
    if (has(p, "dominator_infinity")) for (int i = 0; i < 51; i++) Projectiles.shoot(p.world, p, Projectiles.ANNIHILATOR, 100.0F, Double.POSITIVE_INFINITY, 5);
    break;
   case INFINITY_KILLER:
    if (has(p, "dominator_infinity")) {
     Fx.sound(p.world, p.posX, p.posY, p.posZ, "entity.lightning.thunder", 10.0F, 0.0F);
     for (EntityLivingBase o : Fx.nearLiving(p.world, p.posX, p.posY, p.posZ, 2500, p, true)) { Fx.ext(o); Fx.void3(o); Fx.killReject(o); Fx.hp0(o); }
     for (net.minecraft.entity.Entity o : Fx.near(p.world, p.posX, p.posY, p.posZ, 2500)) if (o != p) Fx.void3(o);
    }
    break;
   case TWOEIG_ATTACK: if (has(p, "the_weapon_of_end_infinity_god")) volley(p, 12); break;
   default: break;
  }
 }
}
