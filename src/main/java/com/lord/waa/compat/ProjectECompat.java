package com.lord.waa.compat;

import com.lord.waa.registry.ModBlocks;
import com.lord.waa.registry.ModItems;
import java.lang.reflect.Method;
import com.lord.waa.logic.Vouchers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;

public final class ProjectECompat {
 private ProjectECompat() {}
 public static final int DEBRIS = 2048;
 public static final int SCRAP = 2048;
 public static final int INGOT = 10240;
 public static final int BLOCK = 9 * INGOT;

 public static void register() {
  if (!Loader.isModLoaded("projecte")) return;
  try {
   Object proxy = Class.forName("moze_intel.projecte.api.ProjectEAPI").getMethod("getEMCProxy").invoke(null);
   Method m = find(proxy);
   if (m == null) { LogManager.getLogger("waa").warn("ProjectE EMC proxy has no registerCustomEMC(ItemStack, int/long)"); return; }
   emc(m, proxy, new ItemStack(ModBlocks.get("finallium_debris")), DEBRIS);
   emc(m, proxy, new ItemStack(ModItems.get("finallium_scrap")), SCRAP);
   emc(m, proxy, new ItemStack(ModItems.get("finallium_ingot")), INGOT);
   emc(m, proxy, new ItemStack(ModBlocks.get("finallium_block")), BLOCK);
  } catch (ReflectiveOperationException | RuntimeException ex) {
   LogManager.getLogger("waa").warn("ProjectE EMC registration unavailable: {}", ex.toString());
  }
 }
 private static Method find(Object proxy) {
  Class<?>[] holders = new Class<?>[2];
  try { holders[0] = Class.forName("moze_intel.projecte.api.proxy.IEMCProxy"); } catch (ClassNotFoundException ignored) { }
  holders[1] = proxy.getClass();
  for (Class<?> c : holders) {
   if (c == null) continue;
   for (Class<?> num : new Class<?>[] {int.class, long.class}) {
    try { return c.getMethod("registerCustomEMC", ItemStack.class, num); } catch (NoSuchMethodException ignored) { }
   }
  }
  return null;
 }
 private static void emc(Method m, Object proxy, ItemStack stack, int value) throws ReflectiveOperationException {
  if (stack.isEmpty()) return;
  if (m.getParameterTypes()[1] == long.class) m.invoke(proxy, stack, (long) value);
  else m.invoke(proxy, stack, value);
 }
 public static void registerConversions() {
  if (!Loader.isModLoaded("projecte")) return;
  try {
   Object proxy = Class.forName("moze_intel.projecte.api.ProjectEAPI").getMethod("getConversionProxy").invoke(null);
   Method add = Class.forName("moze_intel.projecte.api.proxy.IConversionProxy").getMethod("addConversion", int.class, Object.class, Map.class);
   int n = 0;
   for (String id : Vouchers.ids()) {
    Item get = ModItems.get(id);
    LinkedHashMap<Item, Integer> given = Vouchers.given(id);
    if (get == null || given.isEmpty() || given.containsKey(get)) continue;
    int units = 0;
    for (Integer c : given.values()) units += c;
    Map<Object, Integer> ingredients = new LinkedHashMap<Object, Integer>();
    for (Map.Entry<Item, Integer> e : given.entrySet()) ingredients.put(e.getKey(), e.getValue());
    add.invoke(proxy, 1, get, ingredients);
    for (Item item : given.keySet()) {
     Map<Object, Integer> one = new LinkedHashMap<Object, Integer>();
     one.put(get, 1);
     add.invoke(proxy, units, item, one);
    }
    n++;
   }
   LogManager.getLogger("waa").info("ProjectE: linked {} Get items with the items they give", n);
  } catch (ReflectiveOperationException | RuntimeException ex) {
   LogManager.getLogger("waa").warn("ProjectE conversions unavailable: {}", ex.toString());
  }
 }
}
