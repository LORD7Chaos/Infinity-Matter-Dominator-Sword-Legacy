package com.lord.waa.registry;

import com.lord.waa.Reference;
import com.lord.waa.registry.Specs.ItemSpec;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraftforge.common.util.EnumHelper;

final class CrystalTools {
 private CrystalTools() {}
 private static final ToolMaterial PICK = EnumHelper.addToolMaterial("WAA_CRYSTAL_PICK", 20, 5000, 100.0F, 5.0F, 15);
 private static final ToolMaterial AXE = EnumHelper.addToolMaterial("WAA_CRYSTAL_AXE", 20, 5000, 100.0F, 28.0F, 20);
 private static final ToolMaterial SHOVEL = EnumHelper.addToolMaterial("WAA_CRYSTAL_SHOVEL", 20, 5000, 100.0F, 5.0F, 20);
 private static final ToolMaterial HOE = EnumHelper.addToolMaterial("WAA_CRYSTAL_HOE", 20, 50000, 100.0F, 20.0F, 20);

 private static Item finish(Item i, ItemSpec s) {
  i.setRegistryName(Reference.MOD_ID, s.id);
  i.setTranslationKey(Reference.MOD_ID + "." + s.id);
  i.setCreativeTab(ModTabs.TABS[s.tab]);
  return i;
 }
 static Item pickaxe(ItemSpec s) { return finish(new ItemPickaxe(PICK) {}, s); }
 static Item axe(ItemSpec s) { return finish(new ItemAxe(AXE, 29.0F, -3.0F) {}, s); }
 static Item shovel(ItemSpec s) { return finish(new ItemSpade(SHOVEL), s); }
 static Item hoe(ItemSpec s) { return finish(new ItemHoe(HOE), s); }
}
