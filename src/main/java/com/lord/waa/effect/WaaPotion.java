package com.lord.waa.effect;

import com.lord.waa.Reference;
import com.lord.waa.logic.Procs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WaaPotion extends Potion {
 private final String id;
 private final ResourceLocation icon;

 public WaaPotion(String id, String iconName) {
  super(false, 0x000000);
  this.id = id;
  this.icon = new ResourceLocation(Reference.MOD_ID, "textures/mob_effect/" + iconName + ".png");
  setRegistryName(Reference.MOD_ID, id);
  setPotionName("effect." + id);
 }

 public String id() { return id; }
 @Override public boolean isReady(int duration, int amplifier) { return true; }
 @Override public void performEffect(EntityLivingBase entity, int amplifier) { Procs.potionTick(id, entity); }
 @Override public void applyAttributesModifiersToEntity(EntityLivingBase entity, AbstractAttributeMap map, int amplifier) {
  super.applyAttributesModifiersToEntity(entity, map, amplifier);
  Procs.potionStart(id, entity);
 }

 @Override @SideOnly(Side.CLIENT)
 public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
  mc.getTextureManager().bindTexture(icon);
  Gui.drawScaledCustomSizeModalRect(x + 6, y + 7, 0, 0, 1, 1, 18, 18, 1, 1);
 }

 @Override @SideOnly(Side.CLIENT)
 public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
  mc.getTextureManager().bindTexture(icon);
  Gui.drawScaledCustomSizeModalRect(x + 3, y + 3, 0, 0, 1, 1, 18, 18, 1, 1);
 }
}
