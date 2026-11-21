package com.lord.waa.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;

public final class Vouchers {
 private Vouchers() {}
 private static JsonObject data;
 private static final Map<String, String> PROCEDURE = new HashMap<String, String>();
 static {
  PROCEDURE.put("gcwi", "GCI"); PROCEDURE.put("gimbs", "GIMBSS"); PROCEDURE.put("gtwoeg", "GTWOEGG"); PROCEDURE.put("gtwoeig", "GTWOEIGG");
  PROCEDURE.put("gtwofe", "GTWOE"); PROCEDURE.put("geifudaedsi", "GGEIFUDAEDSI"); PROCEDURE.put("get_annihilator", "GCIR");
  PROCEDURE.put("get_chunibyo_rifle", "GCR"); PROCEDURE.put("get_chunibyo_weapon", "GetChunibyo"); PROCEDURE.put("get_the_sword_of_end", "GEIFAAR");
  PROCEDURE.put("get_evolved_infinity_matter_divine_sword", "GetEvolvedInfinityMatterDivineSwordItemInInventoryTick");
  PROCEDURE.put("get_im_ultimate", "Getinfinitymatterultimatesword"); PROCEDURE.put("get_i_mdestruction", "Getinfinitymatterdestructionsword");
  PROCEDURE.put("get_infinity_matter_final_sword", "GetInfinityMatterFinalSwordItemInInventoryTick"); PROCEDURE.put("get_nameless", "GetLastSword");
  PROCEDURE.put("get_the_obliterator", "GetNamelessObliterator"); PROCEDURE.put("getinfinityarmor", "GetinfinityarmorRightClickedInAir");
  PROCEDURE.put("getinfinitymatterdivinesword", "GetinfinitymatterdivineswordItemInInventoryTick");
  PROCEDURE.put("get_infinity_final_antimatter_annihilator_rifle", "GIFMAR"); PROCEDURE.put("right_click_me", "GetinfinitymatterdominatorswordRightClickedInAir");
 }
 private static synchronized JsonObject data() {
  if (data == null) {
   try {
    InputStream in = Vouchers.class.getResourceAsStream("/assets/waa/data/vouchers.json");
    data = new JsonParser().parse(new InputStreamReader(in, "UTF-8")).getAsJsonObject();
    in.close();
   } catch (Exception ex) {
    data = new JsonObject();
   }
  }
  return data;
 }
 public static java.util.Set<String> ids() { return PROCEDURE.keySet(); }

 public static java.util.LinkedHashMap<Item, Integer> given(String id) {
  java.util.LinkedHashMap<Item, Integer> out = new java.util.LinkedHashMap<Item, Integer>();
  String proc = PROCEDURE.get(id);
  JsonArray gives = proc != null && data().has(proc) ? data().getAsJsonArray(proc) : null;
  if (gives == null) return out;
  for (JsonElement el : gives) {
   JsonObject g = el.getAsJsonObject();
   Item item = Item.REGISTRY.getObject(new ResourceLocation(g.get("item").getAsString()));
   if (item == null) continue;
   Integer old = out.get(item);
   out.put(item, (old == null ? 0 : old) + g.get("count").getAsInt());
  }
  return out;
 }
 public static void tick(String id, ItemStack voucher, EntityPlayer p) {
  String proc = PROCEDURE.get(id);
  if (proc == null || voucher.isEmpty()) return;
  JsonArray gives = data().has(proc) ? data().getAsJsonArray(proc) : null;
  if (gives == null) return;
  for (JsonElement el : gives) {
   JsonObject g = el.getAsJsonObject();
   Item item = Item.REGISTRY.getObject(new ResourceLocation(g.get("item").getAsString()));
   if (item == null) continue;
   ItemStack st = new ItemStack(item, g.get("count").getAsInt());
   String nbt = g.get("nbt").getAsString();
   if (!nbt.isEmpty() && !nbt.equals("{}")) {
    try { st.setTagCompound(JsonToNBT.getTagFromJson(nbt)); } catch (NBTException ignored) { }
   }
   if (!p.inventory.addItemStackToInventory(st)) p.dropItem(st, false);
  }
  voucher.shrink(1);
 }
}
