package com.lord.waa.entity;

import com.lord.waa.logic.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWaaArrow extends EntityArrow {
 private static final DataParameter<Integer> KIND = EntityDataManager.<Integer>createKey(EntityWaaArrow.class, DataSerializers.VARINT);

 public EntityWaaArrow(World world) { super(world); }
 public EntityWaaArrow(World world, EntityLivingBase shooter, int kind) {
  super(world, shooter);
  this.dataManager.set(KIND, kind);
 }

 @Override protected void entityInit() {
  super.entityInit();
  this.dataManager.register(KIND, 0);
 }

 public int kind() { return this.dataManager.get(KIND); }

 @Override protected ItemStack getArrowStack() { return ItemStack.EMPTY; }

 @Override
 public void onUpdate() {
  super.onUpdate();
  if (!this.world.isRemote && !this.isDead) Projectiles.flightTick(kind(), this);
 }

 @Override
 protected void onHit(RayTraceResult result) {
  if (this.world.isRemote) { super.onHit(result); return; }
  Entity shooterEntity = this.shootingEntity;
  EntityLivingBase shooter = shooterEntity instanceof EntityLivingBase ? (EntityLivingBase) shooterEntity : null;
  int k = kind();
  if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null) {
   if (result.entityHit == shooterEntity) return;
   super.onHit(result);
   Projectiles.hitEntity(k, this, result.entityHit, shooter);
  } else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
   Projectiles.hitBlock(k, this, result.hitVec.x, result.hitVec.y, result.hitVec.z, shooter);
  }
  this.setDead();
 }

 @Override public void writeEntityToNBT(NBTTagCompound c) { super.writeEntityToNBT(c); c.setInteger("WaaKind", kind()); }
 @Override public void readEntityFromNBT(NBTTagCompound c) { super.readEntityFromNBT(c); this.dataManager.set(KIND, c.getInteger("WaaKind")); }
}
