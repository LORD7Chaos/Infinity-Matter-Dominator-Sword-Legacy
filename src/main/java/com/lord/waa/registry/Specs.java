package com.lord.waa.registry;

public final class Specs {
 private Specs() {}

 public static final class ItemSpec {
  public final String id; public final int tab; public final String rarity; public final boolean glint, fireproof; public final int stack;
  public final boolean tool; public final double damage, speed;
  ItemSpec(String id,int tab,String rarity,boolean glint,boolean fireproof,int stack,boolean tool,double damage,double speed){
   this.id=id;this.tab=tab;this.rarity=rarity;this.glint=glint;this.fireproof=fireproof;this.stack=((damage>0 && speed!=0 && !com.lord.waa.logic.Vouchers.ids().contains(id)) || id.equals("i_m_ultimate")) ? 1 : stack;this.tool=tool;this.damage=damage;this.speed=speed;}
 }

 public static final class BlockSpec {
  public final String id; public final boolean iron, metal; public final float hardness, resistance; public final int light, harvestLevel;
  public final boolean requiresTool, powered, noPush, transparent, nonOpaque;
  BlockSpec(String id,boolean iron,boolean metal,float hardness,float resistance,int light,int harvestLevel,boolean requiresTool,boolean powered,boolean noPush,boolean transparent,boolean nonOpaque){
   this.id=id;this.iron=iron;this.metal=metal;this.hardness=hardness;this.resistance=resistance;this.light=light;this.harvestLevel=harvestLevel;
   this.requiresTool=requiresTool;this.powered=powered;this.noPush=noPush;this.transparent=transparent;this.nonOpaque=nonOpaque;}
 }

 public static final double CHAOTIC_DAMAGE = 100000.0D - 1.0D;
 public static final double CHAOTIC_SPEED = 2.4D - 4.0D;

 public static final ItemSpec[] ITEMS = {
  new ItemSpec("annihilator",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("chaos_ingot",3,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("chaotic_ingot",3,"RARE",false,false,64,false,0D,0D),
  new ItemSpec("chaoschunk",3,"RARE",false,false,64,false,0D,0D),
  new ItemSpec("chaoscrystal",3,"RARE",false,false,64,false,0D,0D),
  new ItemSpec("chaoticsword",3,"COMMON",false,true,64,false,CHAOTIC_DAMAGE,CHAOTIC_SPEED),
  new ItemSpec("chunibyo_bullet",-1,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("chunibyo_rifle",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("chunibyo_weapon_infinity",-1,"COMMON",true,true,64,true,127998.0D,96.0D),
  new ItemSpec("chunibyo_weapon",-1,"COMMON",true,true,64,true,127998.0D,96.0D),
  new ItemSpec("crystal_axe",3,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("crystal_hoe",3,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("crystal_pickaxe",3,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("crystal_shovel",3,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("destroyer_of_chunibyo",-1,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("destroyer_of_matter",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("divine_arrow",-1,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("divinecrystal",3,"EPIC",false,true,64,true,127998.0D,0D),
  new ItemSpec("divinepower",-1,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("evolved_divine_crystal",3,"EPIC",false,true,64,false,0D,0D),
  new ItemSpec("evolved_divine_p",-1,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("evolved_infinity_final_ultimate_destruction_divine_antiatter",3,"EPIC",true,true,64,true,127998.0D,0D),
  new ItemSpec("dominator_infinity",-1,"COMMON",true,true,64,true,127998.0D,96.0D),
  new ItemSpec("i_m_divine_evolved",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("extendedchaoticsword",3,"COMMON",false,true,64,false,CHAOTIC_DAMAGE,CHAOTIC_SPEED),
  new ItemSpec("final_matter_chunk",3,"EPIC",false,true,64,false,0D,0D),
  new ItemSpec("final_matter_debris",3,"RARE",true,false,64,false,0D,0D),
  new ItemSpec("final_matter",3,"EPIC",true,true,64,true,127998.0D,0D),
  new ItemSpec("final_matter_singularity_chunk",3,"EPIC",false,true,64,false,0D,0D),
  new ItemSpec("final_matter_singularity",3,"EPIC",false,true,64,false,0D,0D),
  new ItemSpec("gcwi",2,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("gimbs",0,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("gtwoeg",0,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("gtwoeig",0,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("gtwofe",0,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("geifudaedsi",0,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("get_annihilator",1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("get_chunibyo_rifle",1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("get_chunibyo_weapon",2,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("get_the_sword_of_end",0,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("get_evolved_infinity_matter_divine_sword",3,"EPIC",false,true,1,false,0D,0D),
  new ItemSpec("get_im_ultimate",3,"RARE",false,false,64,false,0D,0D),
  new ItemSpec("get_i_mdestruction",3,"RARE",false,false,64,false,0D,0D),
  new ItemSpec("get_infinity_matter_final_sword",3,"EPIC",true,true,64,false,0D,0D),
  new ItemSpec("get_nameless",3,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("get_the_obliterator",3,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("getinfinityarmor",3,"RARE",false,true,1,false,0D,0D),
  new ItemSpec("getinfinitymatterdivinesword",3,"EPIC",false,true,1,false,0D,0D),
  new ItemSpec("huge_final_matter_singularity_chunk",3,"EPIC",true,true,64,false,0D,0D),
  new ItemSpec("get_infinity_final_antimatter_annihilator_rifle",1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("infinity_annihilator",-1,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("infinity_crystal",3,"RARE",false,true,64,false,0D,0D),
  new ItemSpec("infinity_destroyer",-1,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("infinity_final_antimatter",3,"EPIC",true,true,64,true,127998.0D,0D),
  new ItemSpec("infinity_final_matter_1",3,"EPIC",true,false,64,false,0D,0D),
  new ItemSpec("infinity_final_matter_2",3,"EPIC",true,false,64,false,0D,0D),
  new ItemSpec("infinity_final_matter_3",3,"EPIC",true,false,64,false,0D,0D),
  new ItemSpec("infinity_final_antimatter_annihilator_rifle",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("infinity_final_matter",3,"EPIC",true,true,64,false,0D,0D),
  new ItemSpec("infinity_final_matter_singularity_chunk",3,"EPIC",true,true,64,false,0D,0D),
  new ItemSpec("infinity_final_matter_singularity",3,"EPIC",true,true,64,false,0D,0D),
  new ItemSpec("dominator",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("i_m_final",-1,"COMMON",true,true,64,true,127998.0D,96.0D),
  new ItemSpec("infinitymatter",3,"RARE",false,false,64,false,0D,0D),
  new ItemSpec("infinitymatterarrow",-1,"COMMON",false,false,1,false,0D,0D),
  new ItemSpec("infinitymatterdebris",3,"RARE",false,false,64,false,0D,0D),
  new ItemSpec("i_m_destruction",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("i_m_divine",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("infinitymattergrenade",-1,"COMMON",false,false,64,false,0D,0D),
  new ItemSpec("i_m_ultimate",-1,"COMMON",false,true,64,false,0D,0D),
  new ItemSpec("right_click_me",3,"RARE",false,false,1,false,0D,0D),
  new ItemSpec("nameless",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("the_obliterator",-1,"COMMON",true,true,64,true,127998.0D,96.0D),
  new ItemSpec("the_sword_of_end",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("the_weapon_of_end_god",-1,"COMMON",true,true,64,true,127998.0D,96.0D),
  new ItemSpec("the_weapon_of_end_infinity_god",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("the_weapon_of_end",-1,"COMMON",true,true,64,true,127998.0D,96.0D),
  new ItemSpec("ultimatecrystal",3,"EPIC",false,false,64,false,0D,0D),
  new ItemSpec("unstable_huge_final_matter_singularity_chunk",3,"EPIC",true,true,64,false,0D,0D),
  new ItemSpec("i_m_dominator",-1,"COMMON",false,true,64,true,127998.0D,96.0D),
  new ItemSpec("charged_i_m_dominator",-1,"COMMON",false,true,64,true,127998.0D,96.0D)
 };

 public static final BlockSpec[] BLOCKS = {
  new BlockSpec("chaos_block",true,true,5.0F,10.0F,0,2,true,false,false,false,false),
  new BlockSpec("compressed_chaos_block",true,true,5.0F,10.0F,0,2,true,false,false,false,false),
  new BlockSpec("double_compressed_chaos_block",true,true,5.0F,10.0F,0,2,true,false,false,false,false),
  new BlockSpec("triple_compressed_chaos_block",true,true,5.0F,10.0F,0,2,true,false,false,false,false),
  new BlockSpec("chaotic_ingot_block",true,true,5.0F,10.0F,0,2,true,false,false,false,false),
  new BlockSpec("chaoschunkblock",false,true,1.0F,9999.0F,0,-1,false,false,false,false,false),
  new BlockSpec("divinecrystalblock",true,true,64000.0F,2147483647.0F,0,-1,false,true,true,false,false),
  new BlockSpec("evolved_divine_crystal_block",true,true,64000.0F,2147483647.0F,15,-1,false,true,true,true,false),
  new BlockSpec("final_matter_block",true,true,64000.0F,2147483647.0F,0,-1,false,false,false,false,false),
  new BlockSpec("final_matter_chunk_block",true,true,64000.0F,2147483647.0F,0,-1,false,false,false,false,true),
  new BlockSpec("final_matter_singularity_block",true,true,64000.0F,2147483647.0F,0,-1,false,false,false,false,false),
  new BlockSpec("infinity_matter_ore",false,false,64000.0F,36000000.0F,0,1,true,false,false,false,false),
  new BlockSpec("infinitycrystalblock",false,true,25.0F,3333.0F,0,-1,false,false,false,false,false),
  new BlockSpec("infinitymatterblock",false,true,64000.0F,2147483647.0F,15,-1,false,false,true,false,false),
  new BlockSpec("ultimatecrystalblock",false,true,50.0F,9999.0F,0,-1,false,false,false,false,false)
 };
}
