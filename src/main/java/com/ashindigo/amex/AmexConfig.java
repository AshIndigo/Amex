package com.ashindigo.amex;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = AmexMod.MODID)
public class AmexConfig implements ConfigData {

    // Max Power per armor
    int maxPower = 100;

    @ConfigEntry.Gui.CollapsibleObject
    PowerUsageValues powerUsageValues = new PowerUsageValues();

    @ConfigEntry.Gui.CollapsibleObject
    GeneratorValues generatorValues  = new GeneratorValues();


    static class PowerUsageValues {

        // Armor power usage
        int ironPlatingPower = 2;
        int diamondPlatingPower = 5;

        // Modules power usage
        int rebreatherPower = 5;
        int elytraUsage = 2;
        int speedUsage = 1;
        int damageUsage = 1;
        int jumpUsage = 3;
        int fallResistUsage = 5;

        // HUD
        int compassUsage = 0;
        int clockUsage = 0;
        int lightUsage = 0;
    }

    static class GeneratorValues {
        // Generator values
        int kineticGenerator = 1;
        int solarGenerator = 1;
        int heatGenerator = 2;
    }
}
