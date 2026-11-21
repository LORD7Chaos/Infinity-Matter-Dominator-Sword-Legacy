package com.lord.waa.client;

import com.lord.waa.entity.EntityWaaArrow;
import com.lord.waa.logic.Projectiles;
import com.lord.waa.registry.ModItems;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderWaaArrow extends RenderSnowball<EntityWaaArrow> {
 public RenderWaaArrow(RenderManager manager) {
  super(manager, Items.SNOWBALL, net.minecraft.client.Minecraft.getMinecraft().getRenderItem());
 }

 @Override
 public ItemStack getStackToRender(EntityWaaArrow entity) {
  int k = Math.max(0, Math.min(Projectiles.ITEM.length - 1, entity.kind()));
  Item item = ModItems.get(Projectiles.ITEM[k]);
  return new ItemStack(item == null ? Items.SNOWBALL : item);
 }
}
