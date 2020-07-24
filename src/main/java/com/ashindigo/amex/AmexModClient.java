package com.ashindigo.amex;

import com.ashindigo.amex.item.AmexArmor;
import com.ashindigo.amex.power.PowerManager;
import com.ashindigo.amex.upgradetable.UpgradeTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import spinnery.common.container.BaseContainer;

public class AmexModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
//        ScreenRegistry.register(new Identifier(AmexMod.MODID, "upgradetable"),
//                (id, identifier, player, buf) -> {
//                    BlockPos pos = buf.readBlockPos();
//                    int x = buf.readInt();
//                    int y = buf.readInt();
//                    int m = buf.readInt();
//                    Text text = buf.readText();
//                    return new UpgradeTableScreen(text, new UpgradeTableContainer(id, player.inventory, pos, x, y, m), player, x, y);
//                });
        //ScreenRegistry.register(ScreenHandlerType.ANVIL);
        ScreenRegistry.register(AmexMod.tableScreenHandler, (ScreenRegistry.Factory<BaseContainer, UpgradeTableScreen>) (handler, inventory, title) -> new UpgradeTableScreen(inventory.player, handler));
        HudRenderCallback.EVENT.register(AmexModClient::renderHud);
    }

    private static void renderHud(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient instance = MinecraftClient.getInstance();
        if (instance.player != null && instance.world != null) {
            ItemStack stack = instance.player.getEquippedStack(EquipmentSlot.HEAD);
            int i = instance.getWindow().getScaledWidth() / 2;
            if (stack.getItem() instanceof AmexArmor) {
                if (ModuleManager.hasModule(stack, ModuleManager.COMPASS)) {
                    instance.getItemRenderer().renderInGuiWithOverrides(instance.player, new ItemStack(Items.COMPASS), i, 0);
                }
                if (ModuleManager.hasModule(stack, ModuleManager.CLOCK)) {
                    instance.getItemRenderer().renderInGuiWithOverrides(instance.player, new ItemStack(Items.CLOCK), i - 16, 0);
                }
                if (ModuleManager.hasModule(stack, ModuleManager.LIGHT)) {
                    instance.textRenderer.draw(matrixStack, instance.world.getLightLevel(instance.player.getBlockPos()) + "/" + instance.world.getMaxLightLevel(), i + 16, 4, 8421504);
                }
                instance.textRenderer.draw(matrixStack, PowerManager.getPlayerPower(instance.player) + "/" + PowerManager.getMaxPowerPlayer(instance.player), i, i-32f, 8421504);
            }
        }
    }
}
