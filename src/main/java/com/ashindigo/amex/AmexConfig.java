package com.ashindigo.amex;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = AmexMod.MODID)
public class AmexConfig implements ConfigData { // TODO Proper value balancing

    // Max Power per armor
    public int maxPower = 100;

    @ConfigEntry.Gui.CollapsibleObject
    public PowerUsageValues powerUsageValues = new PowerUsageValues();

    @ConfigEntry.Gui.CollapsibleObject
    public GeneratorValues generatorValues  = new GeneratorValues();


    static class PowerUsageValues {

        // Armor power usage
        public int ironPlatingPower = 2;
        public int diamondPlatingPower = 5;

        // Modules power usage
        public int rebreatherPower = 5;
        public int elytraUsage = 2;
        public int speedUsage = 1;
        public int damageUsage = 1;
        public int jumpUsage = 3;
        public int fallResistUsage = 5;

        // HUD
        public  int compassUsage = 0;
        public int clockUsage = 0;
        public int lightUsage = 0;
    }

    static class GeneratorValues {
        // Generator values
        public int kineticGenerator = 1;
        public int solarGenerator = 1;
        public int heatGenerator = 2;
    }
}
