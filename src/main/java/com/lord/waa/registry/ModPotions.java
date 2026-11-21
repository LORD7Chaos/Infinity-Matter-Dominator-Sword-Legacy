package com.lord.waa.registry;

import com.lord.waa.effect.WaaPotion;
import net.minecraft.potion.Potion;

public final class ModPotions {
 private ModPotions() {}
 public static final WaaPotion CHAOTIC_WITHER = new WaaPotion("chaotic_wither", "chaotic_wither");
 public static final WaaPotion DIVINE_POWER = new WaaPotion("divine_power", "divine_power");
 public static final WaaPotion EVOLVED_POWER = new WaaPotion("evolved_infinity_final_ultimate_divine_end_dominator_power", "evolved_infinity_final_ultimate_divine_end_dominator_power");
 public static final WaaPotion KILL = new WaaPotion("kill", "kill");
 public static final WaaPotion REJECT = new WaaPotion("reject", "reject");
 public static Potion[] all() { return new Potion[] {CHAOTIC_WITHER, DIVINE_POWER, EVOLVED_POWER, KILL, REJECT}; }
}
