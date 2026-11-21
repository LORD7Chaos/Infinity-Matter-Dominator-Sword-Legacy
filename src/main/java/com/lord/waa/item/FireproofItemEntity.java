package com.lord.waa.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FireproofItemEntity extends EntityItem {
 public FireproofItemEntity(World world, Entity location, ItemStack stack) {
  super(world, location.posX, location.posY, location.posZ, stack);
  this.motionX = location.motionX;
  this.motionY = location.motionY;
  this.motionZ = location.motionZ;
  this.setPickupDelay(10);
  this.isImmuneToFire = true;
 }
}
