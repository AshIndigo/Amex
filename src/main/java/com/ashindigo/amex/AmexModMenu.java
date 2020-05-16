package com.ashindigo.amex;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

public class AmexModMenu implements ModMenuApi {
    @Override
    public String getModId() {
        return AmexMod.MODID;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) parent -> AutoConfig.getConfigScreen(AmexConfig.class, parent).get();
    }
}
