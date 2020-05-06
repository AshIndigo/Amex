package com.ashindigo.amex;

import com.ashindigo.amex.item.AmexArmor;
import com.ashindigo.amex.upgradetable.UpgradeTableContainer;
import com.ashindigo.amex.upgradetable.UpgradeTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AmexModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(AmexMod.MODID, "upgradetable"),
                (id, identifier, player, buf) -> {
                    BlockPos pos = buf.readBlockPos();
                    int x = buf.readInt();
                    int y = buf.readInt();
                    int m = buf.readInt();
                    Text text = buf.readText();
                    return new UpgradeTableScreen(text, new UpgradeTableContainer(id, player.inventory, pos, x, y, m), player, x, y);
                });
        HudRenderCallback.EVENT.register(AmexModClient::onHudRender);
    }

    private static void onHudRender(float tickDelta) {
        MinecraftClient instance = MinecraftClient.getInstance();
        if (instance.player != null && instance.world != null) {
            ItemStack stack = instance.player.getEquippedStack(EquipmentSlot.HEAD);
            int i = instance.getWindow().getScaledWidth() / 2;
            if (stack.getItem() instanceof AmexArmor) {
                if (ModuleManager.hasModule(stack, ModuleManager.COMPASS)) {
                    instance.getItemRenderer().renderGuiItem(instance.player, new ItemStack(Items.COMPASS), i, 0);
                }
                if (ModuleManager.hasModule(stack, ModuleManager.CLOCK)) {
                    instance.getItemRenderer().renderGuiItem(instance.player, new ItemStack(Items.CLOCK), i - 16, 0);
                }
                if (ModuleManager.hasModule(stack, ModuleManager.LIGHT)) {
                    instance.textRenderer.draw(instance.world.getLightLevel(instance.player.getBlockPos()) + "/" + instance.world.getMaxLightLevel(), i + 16, 4, 8421504);
                }
            }
        }
    }
}
