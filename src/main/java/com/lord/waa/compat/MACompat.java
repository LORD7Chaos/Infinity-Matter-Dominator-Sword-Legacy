package com.lord.waa.compat;

import com.lord.waa.registry.ModBlocks;
import com.lord.waa.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import java.lang.reflect.Method;

public final class MACompat {
    private MACompat() {}
    private static final String[] NAMES = {"finallium", "infinity_crystal", "chaos", "ultimate_crystal"};
    private static final String[] MATERIALS = {"finallium_ingot", "infinity_crystal", "chaos_ingot", "ultimatecrystal"};
    public static boolean enabled() { return Loader.isModLoaded("mysticalagriculture"); }

    public static void createCrop() {
        if (!enabled()) return;
        try {
            Class<?> type = Class.forName("com.blakebr0.mysticalagriculture.blocks.crop.BlockMysticalCrop");
            for (String name : NAMES) {
                Block crop = (Block) type.getConstructor(String.class).newInstance(name + "_crop");
                crop.setRegistryName("waa", name + "_crop");
                ModBlocks.all().put(name + "_crop", crop);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot register Waa Mystical Agriculture crops", e);
        }
    }

    public static void createSeedsAndEssence() {
        if (!enabled()) return;
        try {
            Class<?> seedType = Class.forName("com.blakebr0.mysticalagriculture.items.ItemSeed");
            for (String name : NAMES) {
                Block crop = ModBlocks.get(name + "_crop");
                if (crop == null) throw new IllegalStateException("Missing crop: " + name);
                Item essence = new Item().setRegistryName("waa", name + "_essence")
                    .setTranslationKey("waa." + name + "_essence")
                    .setCreativeTab(net.minecraft.creativetab.CreativeTabs.MATERIALS);
                Item seeds = (Item) seedType.getConstructor(String.class, Block.class, int.class)
                    .newInstance(name + "_seeds", crop, 5);
                seeds.setRegistryName("waa", name + "_seeds");
                seeds.setTranslationKey("waa." + name + "_seeds");
                crop.getClass().getMethod("setSeed", Item.class).invoke(crop, seeds);
                crop.getClass().getMethod("setCrop", Item.class).invoke(crop, essence);
                ModItems.all().put(name + "_seeds", seeds);
                ModItems.all().put(name + "_essence", essence);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot register Waa Mystical Agriculture seeds", e);
        }
    }

    public static void registerReprocessorRecipes() {
        if (!enabled()) return;
        try {
            Class<?> manager = Class.forName("com.blakebr0.mysticalagriculture.crafting.ReprocessorManager");
            Method add = manager.getMethod("addRecipe", ItemStack.class, ItemStack.class);
            for (String name : NAMES) {
                Item seed = ModItems.get(name + "_seeds");
                Item essence = ModItems.get(name + "_essence");
                if (seed == null || essence == null) throw new IllegalStateException("Missing MA items for " + name);
                add.invoke(null, new ItemStack(essence, 2), new ItemStack(seed));
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot register Waa Seed Reprocessor recipes", e);
        }
    }
}
