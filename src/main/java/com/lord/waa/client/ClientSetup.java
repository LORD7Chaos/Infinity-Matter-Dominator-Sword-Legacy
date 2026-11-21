package com.lord.waa.client;

import com.lord.waa.Reference;
import com.lord.waa.entity.EntityWaaArrow;
import com.lord.waa.logic.KeyActions;
import com.lord.waa.network.Net;
import com.lord.waa.registry.ModBlocks;
import com.lord.waa.registry.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = "waa", value = Side.CLIENT)
public final class ClientSetup {
 private ClientSetup() {}

 private static final String[] NAMES = {"bug_strike", "change_gamemode_a", "change_gamemode_c", "change_gamemode_s", "change_gamemode_spectator",
   "destroyer_of_all", "end", "evolved_infinity_final_ultimate_divine_end_dominator_attack", "infinity_killer", "twoeigattack"};
 private static final int[] CODES = {Keyboard.KEY_B, Keyboard.KEY_I, Keyboard.KEY_K, Keyboard.KEY_L, Keyboard.KEY_O,
   Keyboard.KEY_U, Keyboard.KEY_Z, Keyboard.KEY_RETURN, Keyboard.KEY_N, Keyboard.KEY_V};
 private static final KeyBinding[] KEYS = new KeyBinding[10];

 @SubscribeEvent
 public static void models(ModelRegistryEvent e) {
  for (Item i : ModItems.all().values()) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
  for (net.minecraft.block.Block b : ModBlocks.all().values()) {
   Item i = Item.getItemFromBlock(b);
   if (i != null) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
  }
  RenderingRegistry.registerEntityRenderingHandler(EntityWaaArrow.class, new IRenderFactory<EntityWaaArrow>() {
   @Override public Render<? super EntityWaaArrow> createRenderFor(RenderManager manager) { return new RenderWaaArrow(manager); }
  });
 }

 public static void registerKeys() {
  for (int i = 0; i < KEYS.length; i++) {
   KEYS[i] = new KeyBinding("key." + Reference.MOD_ID + "." + NAMES[i], CODES[i], "key.categories.waa");
   ClientRegistry.registerKeyBinding(KEYS[i]);
  }
 }

 @SubscribeEvent
 public static void onKey(InputEvent.KeyInputEvent e) {
  for (int i = 0; i < KEYS.length; i++) {
   if (KEYS[i] != null && KEYS[i].isPressed()) Net.CHANNEL.sendToServer(new Net.KeyMessage(i));
  }
 }
}
