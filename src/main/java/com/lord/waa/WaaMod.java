package com.lord.waa;

import com.lord.waa.client.ClientSetup;
import com.lord.waa.command.UltraKillCommand;
import com.lord.waa.compat.MACompat;
import com.lord.waa.compat.ProjectECompat;
import com.lord.waa.config.WaaConfig;
import com.lord.waa.entity.EntityWaaArrow;
import com.lord.waa.network.Net;
import com.lord.waa.registry.ModBlocks;
import com.lord.waa.registry.ModItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = "[1.12.2]", dependencies = "after:mysticalagriculture")
public class WaaMod {
 @Mod.Instance(Reference.MOD_ID)
 public static WaaMod instance;

 @Mod.EventHandler
 public void preInit(FMLPreInitializationEvent event) {
  WaaConfig.load(event.getSuggestedConfigurationFile());
  ModBlocks.create();
  ModItems.create();
  Net.init();
  EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "projectile"), EntityWaaArrow.class, "waa_projectile", 0, this, 128, 1, true);
  if (event.getSide() == Side.CLIENT) ClientSetup.registerKeys();
 }

 @Mod.EventHandler
 public void init(FMLInitializationEvent event) {
  GameRegistry.addSmelting(ModBlocks.get("finallium_debris"), new ItemStack(ModItems.get("finallium_scrap")), 2.0F);
  GameRegistry.registerWorldGenerator(new com.lord.waa.world.FinalliumGen(), 0);
  ProjectECompat.register();
  MACompat.registerReprocessorRecipes();
 }

 @Mod.EventHandler
 public void postInit(FMLPostInitializationEvent event) {
  ProjectECompat.registerConversions();
 }

 @Mod.EventHandler
 public void serverStarting(FMLServerStartingEvent event) {
  event.registerServerCommand(new UltraKillCommand());
 }
}
