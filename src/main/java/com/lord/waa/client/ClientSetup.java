package com.lord.waa.client;

import com.lord.waa.Reference;
import com.lord.waa.entity.EntityWaaArrow;
import com.lord.waa.logic.KeyActions;
import com.lord.waa.network.Net;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import com.lord.waa.registry.ModBlocks;
import com.lord.waa.registry.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

@Mod.EventBusSubscriber(modid = "waa", value = Side.CLIENT)
public final class ClientSetup {
 private ClientSetup() {}

 private static final String[] NAMES = {"bug_strike", "change_gamemode_a", "change_gamemode_c", "change_gamemode_s", "change_gamemode_spectator",
   "destroyer_of_all", "end", "evolved_infinity_final_ultimate_divine_end_dominator_attack", "infinity_killer", "twoeigattack"};
 private static final int[] CODES = {Keyboard.KEY_B, Keyboard.KEY_I, Keyboard.KEY_K, Keyboard.KEY_L, Keyboard.KEY_O,
   Keyboard.KEY_U, Keyboard.KEY_Z, Keyboard.KEY_RETURN, Keyboard.KEY_N, Keyboard.KEY_V};
 private static final KeyBinding[] KEYS = new KeyBinding[10];
 private static final Pattern UNTRANSLATED_ENCHANT_LEVEL = Pattern.compile("enchantment\\.level\\.([A-Za-z0-9_-]+)");
 private static final char[] RAINBOW_COLORS = {'c', '6', 'e', 'a', 'b', '9', 'd', '5'};

 private static String fixEnchantLevels(String text) {
  Matcher match = UNTRANSLATED_ENCHANT_LEVEL.matcher(text);
  StringBuffer fixed = new StringBuffer();
  while (match.find()) {
   String level = match.group(1);
   String replacement = "INFINITY".equalsIgnoreCase(level)
       ? net.minecraft.client.resources.I18n.format("enchantment.level.INFINITY") : level;
   match.appendReplacement(fixed, Matcher.quoteReplacement(replacement));
  }
  match.appendTail(fixed);
  return fixed.toString();
 }

 private static String rainbow(String plain) {
  if (plain == null) return "";
  int shift = (int) ((System.currentTimeMillis() / 120L) % RAINBOW_COLORS.length);
  StringBuilder result = new StringBuilder(plain.length() * 3);
  int index = 0;
  for (int i = 0; i < plain.length(); i++) {
   char c = plain.charAt(i);
   if (c == ' ') { result.append(c); continue; }
   result.append('\u00A7').append(RAINBOW_COLORS[(index++ + shift) % RAINBOW_COLORS.length]).append(c);
  }
  return result.append(TextFormatting.RESET).toString();
 }

 @SubscribeEvent
 public static void onItemTooltip(ItemTooltipEvent event) {
  List<String> lines = event.getToolTip();
  if (lines == null || lines.isEmpty()) return;
  for (int i = 0; i < lines.size(); i++) lines.set(i, fixEnchantLevels(lines.get(i)));

  if (event.getItemStack().isEmpty() ||
      event.getItemStack().getItem() != ModItems.get("charged_i_m_dominator")) return;

  String title = net.minecraft.client.resources.I18n.format("item.waa.charged_i_m_dominator.name");
  lines.set(0, rainbow(title));
  String damage = net.minecraft.client.resources.I18n.format("attribute.name.generic.attackDamage");
  String speed = net.minecraft.client.resources.I18n.format("attribute.name.generic.attackSpeed");
  for (int i = 1; i < lines.size(); i++) {
   String unformatted = TextFormatting.getTextWithoutFormattingCodes(lines.get(i));
   if (unformatted == null) continue;
   if (unformatted.contains(damage)) {
    lines.set(i, rainbow("Infinity") + " " + TextFormatting.GRAY + damage);
   } else if (unformatted.contains(speed)) {
    lines.set(i, rainbow("Infinity") + " " + TextFormatting.GRAY + speed);
   }
  }
 }

 private static final ChargedSwordRenderer CHARGED_RENDERER = new ChargedSwordRenderer(false);
 private static final ChargedSwordRenderer INGOT_RENDERER = new ChargedSwordRenderer(true);

 @SubscribeEvent
 public static void models(ModelRegistryEvent e) {
  Item chargedSword = ModItems.get("charged_i_m_dominator");
  if (chargedSword != null) chargedSword.setTileEntityItemStackRenderer(CHARGED_RENDERER);
  Item chaoticIngot = ModItems.get("chaotic_ingot");
  if (chaoticIngot != null) chaoticIngot.setTileEntityItemStackRenderer(INGOT_RENDERER);
  for (Item i : ModItems.all().values()) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
  for (net.minecraft.block.Block b : ModBlocks.all().values()) {
   if (b.getRegistryName().getPath().endsWith("_crop")) continue;
   Item i = Item.getItemFromBlock(b);
   if (i != null) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
  }
  RenderingRegistry.registerEntityRenderingHandler(EntityWaaArrow.class, new IRenderFactory<EntityWaaArrow>() {
   @Override public Render<? super EntityWaaArrow> createRenderFor(RenderManager manager) { return new RenderWaaArrow(manager); }
  });
 }

 @SubscribeEvent
 public static void onModelBake(ModelBakeEvent event) {
  wrapGuiModel(event, "charged_i_m_dominator", CHARGED_RENDERER);
  wrapGuiModel(event, "chaotic_ingot", INGOT_RENDERER);
 }

 private static void wrapGuiModel(ModelBakeEvent event, String itemId, ChargedSwordRenderer renderer) {
  ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation("waa", itemId), "inventory");
  IBakedModel original = event.getModelRegistry().getObject(location);
  if (original != null && !(original instanceof ChargedSwordBakedModel)) {
   event.getModelRegistry().putObject(location, new ChargedSwordBakedModel(original, renderer));
  }
 }

 public static void registerKeys() {
  for (int i = 0; i < KEYS.length; i++) {
   KEYS[i] = new KeyBinding("key." + Reference.MOD_ID + "." + NAMES[i], CODES[i], "key.categories.waa");
   ClientRegistry.registerKeyBinding(KEYS[i]);
  }
 }

 @SubscribeEvent
 public static void onMouseWheel(MouseEvent e) {
  if (e.getDwheel() == 0 || Minecraft.getMinecraft().player == null ||
      !Minecraft.getMinecraft().player.isSneaking()) return;
  if (Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() != ModItems.get("crystal_shovel")) return;
  if (Net.CHANNEL == null) return;
  e.setCanceled(true);
  Net.CHANNEL.sendToServer(new Net.ShovelModeMessage(e.getDwheel() > 0 ? 1 : -1));
 }

 @SubscribeEvent
 public static void onKey(InputEvent.KeyInputEvent e) {
  for (int i = 0; i < KEYS.length; i++) {
   if (KEYS[i] != null && KEYS[i].isPressed()) Net.CHANNEL.sendToServer(new Net.KeyMessage(i));
  }
 }
}
