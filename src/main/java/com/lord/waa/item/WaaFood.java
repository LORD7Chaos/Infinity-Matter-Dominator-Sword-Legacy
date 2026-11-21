package com.lord.waa.item;

import com.lord.waa.Reference;
import com.lord.waa.registry.ModPotions;
import com.lord.waa.registry.ModTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class WaaFood extends ItemFood {
 public WaaFood() {
  super(1000, 1000.0F, false);
  setAlwaysEdible();
  setRegistryName(Reference.MOD_ID, "infinity_matter_apple");
  setTranslationKey(Reference.MOD_ID + ".infinity_matter_apple");
  setCreativeTab(ModTabs.TABS[3]);
 }
 @Override public EnumRarity getRarity(ItemStack stack) { return EnumRarity.EPIC; }
 @Override public int getMaxItemUseDuration(ItemStack stack) { return 1; }
 @Override
 protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
  super.onFoodEaten(stack, world, player);
  if (!world.isRemote) player.addPotionEffect(new PotionEffect(ModPotions.DIVINE_POWER, 1000000, 255, false, false));
 }
}
