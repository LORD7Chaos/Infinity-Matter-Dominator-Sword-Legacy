package com.lord.waa.logic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class ItemHooks {
 private ItemHooks() {}

 public static void rightClick(String id, ItemStack stack, World w, EntityPlayer p) {
  switch (id) {
   case "annihilator": Procs.annihilatorRC(w, p); break;
   case "chaoticsword": Procs.chaoticRC(w, p); break;
   case "chunibyo_rifle":
   case "infinity_final_antimatter_annihilator_rifle": Procs.rifleRC(w, p); break;
   case "chunibyo_weapon_infinity":
   case "chunibyo_weapon": Procs.chunibyoRC(w, p); break;
   case "dominator_infinity": Procs.erc(w, p, stack); break;
   case "i_m_divine_evolved": Procs.evolvedDivineRC(w, p); break;
   case "extendedchaoticsword": Procs.ec(w, p); break;
   case "dominator":
   case "the_sword_of_end":
   case "the_weapon_of_end_god":
   case "the_weapon_of_end":
   case "the_weapon_of_end_infinity_god": Procs.endRC(w, p); break;
   case "i_m_final": Procs.finalRC(w, p); break;
   case "i_m_destruction":
   case "i_m_ultimate":
   case "i_m_dominator":
   case "charged_i_m_dominator": Procs.waaSwordRC(w, p); break;
   case "i_m_divine": Procs.divineRC(w, p); break;
   case "nameless":
   case "the_obliterator": Procs.lastRC(w, p); break;
   default: break;
  }
 }

 public static void hit(String id, ItemStack stack, EntityLivingBase t, EntityLivingBase a) {
  switch (id) {
   case "annihilator":
   case "chunibyo_rifle":
   case "chunibyo_weapon_infinity": Procs.ahi(t); break;
   case "chunibyo_weapon": Procs.chunibyoHit(t); break;
   case "dominator_infinity": Procs.eh(t); Procs.esw(a); break;
   case "i_m_divine_evolved": Procs.evolvedDivineHit(t); break;
   case "infinity_final_antimatter_annihilator_rifle": Procs.ifmarh(t); break;
   case "dominator": Procs.imbsh(t); Procs.imbssw(a); break;
   case "i_m_final": Procs.finalHit(t, a); break;
   case "i_m_divine": Procs.divineHit(t); break;
   case "nameless":
   case "the_obliterator": Procs.lastHit(t, a); break;
   case "the_sword_of_end": Procs.evifmah(t); break;
   case "the_weapon_of_end_god":
   case "the_weapon_of_end":
   case "the_weapon_of_end_infinity_god": Procs.twofhit(t); break;
   default: break;
  }
 }

 public static void tick(String id, ItemStack stack, World w, EntityLivingBase e, boolean selected) {
  if (id.startsWith("get") || id.equals("gcwi") || id.equals("gimbs") || id.equals("gtwoeg") || id.equals("gtwoeig") || id.equals("gtwofe") || id.equals("geifudaedsi") || id.equals("right_click_me")) {
   if (e instanceof EntityPlayer) Vouchers.tick(id, stack, (EntityPlayer) e);
   return;
  }
  switch (id) {
   case "annihilator": if (selected) Procs.handBasic(e); GodMode.AINV.apply(e, stack); break;
   case "chunibyo_rifle": if (selected) Procs.handBasic(e); GodMode.CHRINV.apply(e, stack); break;
   case "chunibyo_weapon_infinity":
   case "chunibyo_weapon": if (selected) Procs.handBasic(e); GodMode.CHWI.apply(e, stack); break;
   case "destroyer_of_matter": Procs.destroyerOfMatterTick(e); break;
   case "dominator_infinity": if (selected) Procs.eha(e); GodMode.EIFUDAEDSI.apply(e, stack); break;
   case "i_m_divine_evolved":
   case "i_m_divine": if (selected) Procs.handBasic(e); GodMode.FINAL.apply(e, stack); break;
   case "extendedchaoticsword": Procs.extendedChaotic(stack); break;
   case "infinity_final_antimatter_annihilator_rifle": if (selected) Procs.handBasic(e); GodMode.IFMARINV.apply(e, stack); break;
   case "dominator": if (selected) Procs.imbsha(e); GodMode.IMBSINV.apply(e, stack); break;
   case "i_m_final": if (selected) Procs.handBasic(e); GodMode.FINAL.apply(e, stack); break;
   case "i_m_destruction": if (selected) Procs.waaSwordHandTick2(e); Procs.waaSwordHandTick(e); break;
   case "i_m_ultimate": if (selected) Procs.ultimateHandParticles(e); Procs.waaSwordHandTick(e); break;
   case "charged_i_m_dominator":
    Procs.chargedDominatorEnchantments(stack);
    if (selected) Procs.waaSwordHandTick2(e);
    Procs.waaSwordHandTick(e); Procs.waaSwordParticles(e);
    break;
   case "i_m_dominator":
    if (selected) Procs.waaSwordHandTick2(e);
    Procs.waaSwordHandTick(e); Procs.waaSwordParticles(e);
    break;
   case "nameless":
   case "the_obliterator": if (selected) Procs.handBasic(e); GodMode.LAST.apply(e, stack); break;
   case "the_sword_of_end":
   case "the_weapon_of_end_god":
   case "the_weapon_of_end": if (selected) Procs.handBasic(e); GodMode.EVIFAARINV.apply(e, stack); break;
   case "the_weapon_of_end_infinity_god": if (selected) Procs.twoegha(e); GodMode.TWOEIGINVVV.apply(e, stack); break;
   default: break;
  }
 }

 public static void blockDestroyed(String id, ItemStack stack, World w, BlockPos pos, EntityLivingBase e) {
  if (id.equals("dominator_infinity")) Procs.dominatorBreak3(w, pos);
 }

 public static void swing(String id, ItemStack stack, EntityLivingBase e) {
  if (id.equals("the_weapon_of_end_infinity_god")) Procs.twoegSwing(e);
 }
}
