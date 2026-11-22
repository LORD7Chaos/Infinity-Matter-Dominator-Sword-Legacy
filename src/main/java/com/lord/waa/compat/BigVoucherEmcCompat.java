package com.lord.waa.compat;

import com.lord.waa.logic.Vouchers;
import com.lord.waa.registry.ModItems;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;

public final class BigVoucherEmcCompat {
 private static int ticks;
 private static boolean installed;
 private static Map<String, BigInteger> lastAppliedValues = new HashMap<>();
 private BigVoucherEmcCompat() {}

 public static void install() {
  if (installed || !Loader.isModLoaded("projecte") || !Loader.isModLoaded("projecte_no_limits")) return;
  installed = true;
  MinecraftForge.EVENT_BUS.register(new BigVoucherEmcCompat());
 }

 @SubscribeEvent
 public void onServerTick(TickEvent.ServerTickEvent event) {
  if (event.phase != TickEvent.Phase.END || ++ticks % 200 != 0) return;
  try { update(); }
  catch (ReflectiveOperationException | RuntimeException ex) {
   if (ticks == 200) LogManager.getLogger("waa").warn("Optional big voucher EMC integration unavailable", ex);
  }
 }

 @SuppressWarnings("unchecked")
 private static void update() throws ReflectiveOperationException {
  Class<?> big = Class.forName("com.lord.projecte_no_limits.item.BigItemEmc");
  Method cost = big.getMethod("cost", ItemStack.class);
  Method key = big.getMethod("stackKey", ItemStack.class);
  Method manual = big.getMethod("manualValue", ItemStack.class);
  Method snapshot = big.getMethod("snapshot");
  Method setDerived = big.getMethod("setDerived", Map.class);
  Map<String, String> current = (Map<String, String>) snapshot.invoke(null);
  Map<String, BigInteger> known = new HashMap<>();
  for (Map.Entry<String, String> e : current.entrySet()) {
   try { known.put(e.getKey(), new BigInteger(e.getValue())); }
   catch (NumberFormatException ignored) { }
  }
  Map<String, BigInteger> additions = new HashMap<>();
  Set<String> voucherIds = new HashSet<>(Vouchers.ids());
  for (int pass = 0; pass < 64; pass++) {
   boolean changed = false;
   for (IRecipe recipe : CraftingManager.REGISTRY) {
    ItemStack output = recipe.getRecipeOutput();
    if (output == null || output.isEmpty() || output.getCount() <= 0) continue;
    String outputKey = (String) key.invoke(null, output);
    if (outputKey == null || manual.invoke(null, output) != null) continue;
    if (output.getItem().getRegistryName() == null ||
            !"waa".equals(output.getItem().getRegistryName().getNamespace())) continue;
    BigInteger sum = BigInteger.ZERO;
    boolean valid = true;
    for (Ingredient ingredient : recipe.getIngredients()) {
     if (ingredient == null || ingredient == Ingredient.EMPTY) continue;
     BigInteger cheapest = null;
     for (ItemStack option : ingredient.getMatchingStacks()) {
      if (option == null || option.isEmpty() || option.getMetadata() == OreDictionary.WILDCARD_VALUE) continue;
      String optionKey = (String) key.invoke(null, option);
      BigInteger price = known.get(optionKey);
      if (price == null) price = (BigInteger) cost.invoke(null, option);
      if (price == null || price.signum() <= 0) continue;
      if (option.getItem().hasContainerItem(option)) {
       ItemStack container = option.getItem().getContainerItem(option);
       if (container != null && !container.isEmpty()) {
        BigInteger refund = known.get((String) key.invoke(null, container));
        if (refund == null) refund = (BigInteger) cost.invoke(null, container);
        if (refund != null) price = price.subtract(refund);
       }
      }
      if (price.signum() < 0) continue;
      if (cheapest == null || price.compareTo(cheapest) < 0) cheapest = price;
     }
     if (cheapest == null) { valid = false; break; }
     sum = sum.add(cheapest);
    }
    if (!valid || sum.signum() <= 0) continue;
    BigInteger calculated = sum.divide(BigInteger.valueOf(output.getCount()));
    if (calculated.signum() <= 0) continue;
    BigInteger old = known.get(outputKey);
    if (old == null || calculated.compareTo(old) < 0) {
     known.put(outputKey, calculated);
     additions.put(outputKey, calculated);
     changed = true;
    }
   }
   for (String id : voucherIds) {
    Item voucher = ModItems.get(id);
    LinkedHashMap<Item, Integer> rewards = Vouchers.given(id);
    if (voucher == null || rewards.isEmpty()) continue;
    ItemStack source = new ItemStack(voucher);
    if (source.isEmpty()) continue;
    String sourceKey = (String) key.invoke(null, source);
    BigInteger sourceValue = known.get(sourceKey);
    if (sourceValue == null) sourceValue = (BigInteger) cost.invoke(null, source);
    if (sourceValue == null || sourceValue.signum() <= 0) continue;
    long units = 0L;
    for (Map.Entry<Item, Integer> reward : rewards.entrySet()) {
     if (reward.getKey() != null && reward.getValue() != null && reward.getValue() > 0)
      units += reward.getValue();
    }
    if (units <= 0L) continue;
    BigInteger perItem = sourceValue.divide(BigInteger.valueOf(units));
    if (perItem.signum() <= 0) continue;
    for (Map.Entry<Item, Integer> reward : rewards.entrySet()) {
     if (reward.getKey() == null || reward.getValue() == null || reward.getValue() <= 0) continue;
     ItemStack target = new ItemStack(reward.getKey());
     if (target.isEmpty() || manual.invoke(null, target) != null) continue;
     String targetKey = (String) key.invoke(null, target);
     if (targetKey == null) continue;
     BigInteger old = known.get(targetKey);
     if (old == null || perItem.compareTo(old) < 0) {
      known.put(targetKey, perItem);
      additions.put(targetKey, perItem);
      changed = true;
     }
    }
   }
   if (!changed) break;
  }
  Map<String, String> existing = current;
  Map<String, BigInteger> merged = new HashMap<>();
  for (Map.Entry<String, String> entry : existing.entrySet()) {
   try { merged.put(entry.getKey(), new BigInteger(entry.getValue())); }
   catch (NumberFormatException ignored) { }
  }
  Map<String, BigInteger> applied = new HashMap<>();
  for (Map.Entry<String, BigInteger> entry : additions.entrySet()) {
   BigInteger previous = merged.get(entry.getKey());
   if (previous == null || entry.getValue().compareTo(previous) < 0) {
    merged.put(entry.getKey(), entry.getValue());
   }
   applied.put(entry.getKey(), merged.get(entry.getKey()));
  }
  if (applied.equals(lastAppliedValues)) return;
  if (applied.isEmpty()) return;
  setDerived.invoke(null, merged);
  lastAppliedValues = applied;
  big.getMethod("refreshTomeCache").invoke(null);
  Class<?> mod = Class.forName("com.lord.projecte_no_limits.ProjectENoLimits");
  Field networkField = mod.getField("NETWORK");
  Object network = networkField.get(null);
  Class<?> packet = Class.forName("com.lord.projecte_no_limits.net.PacketBigItems");
  Constructor<?> ctor = packet.getConstructor(Map.class);
  Object message = ctor.newInstance(snapshot.invoke(null));
  for (Method method : network.getClass().getMethods()) {
   if (method.getName().equals("sendToAll") && method.getParameterTypes().length == 1) {
    method.invoke(network, message);
    break;
   }
  }
 }
}
