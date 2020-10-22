package com.ashindigo.amex;

import com.ashindigo.amex.client.gui.ModuleCreatorScreen;
import com.ashindigo.amex.screenhandler.ModuleCreatorScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class AmexClient implements ClientModInitializer {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(Amex.MODULE_CREATOR_HANDLER, (ScreenRegistry.Factory) (handler, inventory, title) -> new ModuleCreatorScreen((ModuleCreatorScreenHandler) handler, inventory));
    }
}
