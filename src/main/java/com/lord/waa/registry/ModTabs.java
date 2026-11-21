package com.lord.waa.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModTabs {
 private ModTabs() {}

 public static CreativeTabs[] TABS;

 private static final String[] LABELS = {"tabchunibyo_ultimate_weapon", "tabchunibyo_weapon_rifle", "tabchunibyo_weapons", "tabinfinity_matter_dominator_sword"};
 private static final String[] ICONS = {"the_sword_of_end", "get_chunibyo_rifle", "nameless", "i_m_dominator"};

 public static void create() {
  if (TABS != null) return;
  TABS = new CreativeTabs[4];
  for (int i = 0; i < 4; i++) {
   final String icon = ICONS[i];
   TABS[i] = new CreativeTabs(LABELS[i]) {
    @Override @SideOnly(Side.CLIENT)
    public ItemStack createIcon() {
     Item item = ModItems.get(icon);
     return item == null ? new ItemStack(net.minecraft.init.Items.DIAMOND_SWORD) : new ItemStack(item);
    }
    @Override public boolean hasSearchBar() { return false; }
   };
  }
 }
}
