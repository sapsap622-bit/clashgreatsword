package com.clashgreatsword;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec CLIENT_SPEC;

    // ---- Gameplay (common) ----
    public static final ModConfigSpec.ConfigValue<String> WEAPON_ID;
    public static final ModConfigSpec.IntValue MIN_CHARGE_TICKS;
    public static final ModConfigSpec.IntValue MAX_CHARGE_TICKS;
    public static final ModConfigSpec.DoubleValue BONUS_REACH;
    public static final ModConfigSpec.DoubleValue CRIT_MULTIPLIER;
    public static final ModConfigSpec.IntValue COOLDOWN_TICKS;

    // ---- Visual (client) ----
    public static final ModConfigSpec.BooleanValue REVERSE_GRIP;
    public static final ModConfigSpec.DoubleValue FP_ROT_X, FP_ROT_Y, FP_ROT_Z;
    public static final ModConfigSpec.DoubleValue FP_PIVOT_X, FP_PIVOT_Y, FP_PIVOT_Z;
    public static final ModConfigSpec.DoubleValue TP_ROT_X, TP_ROT_Y, TP_ROT_Z;
    public static final ModConfigSpec.DoubleValue TP_PIVOT_X, TP_PIVOT_Y, TP_PIVOT_Z;

    static {
        ModConfigSpec.Builder c = new ModConfigSpec.Builder();
        c.push("weapon");
        WEAPON_ID = c.comment("ID del item que usa el ataque cargado")
                .define("weaponId", "alloyed:steel_sword");
        MIN_CHARGE_TICKS = c.comment("Ticks minimos de carga para que el golpe se ejecute (20 ticks = 1 s)")
                .defineInRange("minChargeTicks", 8, 1, 200);
        MAX_CHARGE_TICKS = c.comment("Ticks para carga completa (critico)")
                .defineInRange("maxChargeTicks", 20, 2, 200);
        BONUS_REACH = c.comment("Alcance extra (bloques) con carga completa; escala con la carga")
                .defineInRange("bonusReach", 1.5, 0.0, 10.0);
        CRIT_MULTIPLIER = c.comment("Multiplicador de dano critico con carga completa")
                .defineInRange("critMultiplier", 1.5, 1.0, 10.0);
        COOLDOWN_TICKS = c.comment("Enfriamiento tras el golpe cargado")
                .defineInRange("cooldownTicks", 10, 0, 400);
        c.pop();
        COMMON_SPEC = c.build();

        ModConfigSpec.Builder v = new ModConfigSpec.Builder();
        v.push("reverse_grip");
        REVERSE_GRIP = v.comment("Empunar la espada al reves (reverse grip)")
                .define("enabled", true);
        v.comment("Primera persona: rotacion en grados y pivote (en unidades de modelo, 0 = centro del item)").push("first_person");
        FP_ROT_X = v.defineInRange("rotX", 0.0, -360.0, 360.0);
        FP_ROT_Y = v.defineInRange("rotY", 0.0, -360.0, 360.0);
        FP_ROT_Z = v.defineInRange("rotZ", 180.0, -360.0, 360.0);
        FP_PIVOT_X = v.defineInRange("pivotX", -0.25, -2.0, 2.0);
        FP_PIVOT_Y = v.defineInRange("pivotY", -0.25, -2.0, 2.0);
        FP_PIVOT_Z = v.defineInRange("pivotZ", 0.0, -2.0, 2.0);
        v.pop();
        v.comment("Tercera persona (y otros jugadores)").push("third_person");
        TP_ROT_X = v.defineInRange("rotX", 0.0, -360.0, 360.0);
        TP_ROT_Y = v.defineInRange("rotY", 0.0, -360.0, 360.0);
        TP_ROT_Z = v.defineInRange("rotZ", 180.0, -360.0, 360.0);
        TP_PIVOT_X = v.defineInRange("pivotX", -0.25, -2.0, 2.0);
        TP_PIVOT_Y = v.defineInRange("pivotY", -0.25, -2.0, 2.0);
        TP_PIVOT_Z = v.defineInRange("pivotZ", 0.0, -2.0, 2.0);
        v.pop();
        v.pop();
        CLIENT_SPEC = v.build();
    }
}
