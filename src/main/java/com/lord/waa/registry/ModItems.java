package com.lord.waa.registry;

import com.lord.waa.item.WaaArmor;
import com.lord.waa.item.WaaFood;
import com.lord.waa.item.WaaItem;
import com.lord.waa.registry.Specs.ItemSpec;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public final class ModItems {
 private ModItems() {}
 private static final Map<String, Item> ITEMS = new LinkedHashMap<String, Item>();

 public static Item get(String id) { return ITEMS.get(id); }
 public static Map<String, Item> all() { return ITEMS; }

 public static void create() {
  if (!ITEMS.isEmpty()) return;
  ModTabs.create();
  for (ItemSpec s : Specs.ITEMS) {
   Item item;
   switch (s.id) {
    case "crystal_pickaxe": item = CrystalTools.pickaxe(s); break;
    case "crystal_axe": item = CrystalTools.axe(s); break;
    case "crystal_shovel": item = CrystalTools.shovel(s); break;
    case "crystal_hoe": item = CrystalTools.hoe(s); break;
    default: item = new WaaItem(s);
   }
   ITEMS.put(s.id, item);
  }
  ITEMS.put("infinity_helmet", new WaaArmor("infinity_helmet", EntityEquipmentSlot.HEAD));
  ITEMS.put("infinity_chestplate", new WaaArmor("infinity_chestplate", EntityEquipmentSlot.CHEST));
  ITEMS.put("infinity_leggings", new WaaArmor("infinity_leggings", EntityEquipmentSlot.LEGS));
  ITEMS.put("infinity_boots", new WaaArmor("infinity_boots", EntityEquipmentSlot.FEET));
  ITEMS.put("infinity_matter_apple", new WaaFood());
  Finallium.createItems(ITEMS);
 }
}
