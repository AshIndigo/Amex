package com.ashindigo.amex.upgradetable;

import com.ashindigo.amex.*;
import com.ashindigo.amex.modules.AmexModule;
import com.ashindigo.amex.widgets.WTooltipModule;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.client.screen.BaseScreen;
import spinnery.common.container.BaseContainer;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.List;
import java.util.Optional;

public class UpgradeTableScreen extends BaseContainerScreen<BaseContainer> {

    AmexModule selectedModule;
    Position pos;
    final Identifier checkTexture = new Identifier(AmexMod.MODID, "textures/misc/check.png");
    final Identifier configTexture = new Identifier(AmexMod.MODID, "textures/misc/config.png");
    final Identifier both = new Identifier(AmexMod.MODID, "textures/misc/both.png");
    final Identifier neither = new Identifier(AmexMod.MODID, "textures/misc/neither.png");
    private PlayerEntity player = playerInventory.player;

    public UpgradeTableScreen(PlayerEntity player, BaseContainer handler) {
        super(new TranslatableText("block.amex.upgradetable"), handler, player);
        WInterface mainInterface = getInterface();
        WTabHolder tabHolder = mainInterface.createChild(WTabHolder::new, Position.of(mainInterface), Size.of(248, 166));
        tabHolder.center();
        if (player.getEquippedStack(EquipmentSlot.HEAD).getItem() == ItemRegistry.AMEX_HELM) {
            addTab(EquipmentSlot.HEAD, new TranslatableText("text.amex.helmet"), ItemRegistry.AMEX_HELM, tabHolder);
        }
        if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() == ItemRegistry.AMEX_CHEST) {
            addTab(EquipmentSlot.CHEST, new TranslatableText("text.amex.chest"), ItemRegistry.AMEX_CHEST, tabHolder);
        }
        if (player.getEquippedStack(EquipmentSlot.LEGS).getItem() == ItemRegistry.AMEX_LEGS) {
            addTab(EquipmentSlot.LEGS, new TranslatableText("text.amex.legs"), ItemRegistry.AMEX_LEGS, tabHolder);
        }
        if (player.getEquippedStack(EquipmentSlot.FEET).getItem() == ItemRegistry.AMEX_BOOTS) {
            addTab(EquipmentSlot.FEET, new TranslatableText("text.amex.boots"), ItemRegistry.AMEX_BOOTS, tabHolder);
        } else {
            WTabHolder.WTab tab = tabHolder.addTab(Items.AIR, new LiteralText("Nothing"));
            tab.getBody().add(new WStaticText().setText("No Armor equipped!").setPosition(Position.of(tabHolder).add(4, 28, 0)));
        }
    }

    public void addTab(EquipmentSlot slot, Text name, Item item, WTabHolder tabHolder) {
        WTabHolder.WTab tab = tabHolder.addTab(item, name);
        List<AmexModule> moduleList = ModuleManager.getAllModulesForSlot(slot);
        WTextArea desc = tab.getBody().createChild(WTextArea::new, Position.ofTopRight(tabHolder).add(-112, 28, 0), Size.of(104, 128 - 18)).setLineWrap(true).setEditable(false);
        WButton install = tab.getBody().createChild(WButton::new, Position.ofBottomRight(tabHolder).add(-72 - 32, -24, 0), Size.of(48, 16)).setLabel(new TranslatableText("text.module.install")).setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeString(selectedModule.getName().toString());
            passedData.writeString(slot.getName());
            ClientSidePacketRegistry.INSTANCE.sendToServer(PacketRegistry.ADD_MODULE, passedData);
            if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.inventory.contains(ModuleManager.getModule(selectedModule.getName()).getItemStack())) { // Tooltip update fix
                if (!ModuleManager.hasModule(MinecraftClient.getInstance().player.getEquippedStack(slot), ModuleManager.getModule(selectedModule.getName()))) {
                    ModuleManager.addModule(MinecraftClient.getInstance().player.getEquippedStack(slot), ModuleManager.getModule(selectedModule.getName()));
                    Optional<WAbstractWidget> opt = tab.getWidgets().stream().filter(((predicate) -> {
                        if (predicate instanceof WStaticImage) {
                            WStaticImage image = (WStaticImage) predicate;
                            return HelperMethods.positionsXYSame(image.getPosition(), pos) && image.getTexture() == neither;
                        }
                        return false;
                    })).findFirst();

                    if (opt.isPresent()) {
                        WStaticImage image = (WStaticImage) opt.get();
                        image.setTexture(checkTexture);
                    }
                }
            }
        });
        WButton remove = tab.getBody().createChild(WButton::new, Position.ofBottomRight(tabHolder).add(-72 + 18, -24, 0), Size.of(48, 16)).setLabel(new TranslatableText("text.module.remove")).setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeString(selectedModule.getName().toString());
            passedData.writeString(slot.getName());
            ClientSidePacketRegistry.INSTANCE.sendToServer(PacketRegistry.REMOVE_MODULE, passedData);
            if (MinecraftClient.getInstance().player != null && ModuleManager.hasModule(MinecraftClient.getInstance().player.getEquippedStack(slot), ModuleManager.getModule(selectedModule.getName()))) { // Tool tip fix
                ModuleManager.removeModule(MinecraftClient.getInstance().player.getEquippedStack(slot), ModuleManager.getModule(selectedModule.getName()));
                Optional<WAbstractWidget> opt = tab.getWidgets().stream().filter(((predicate) -> {
                    if (predicate instanceof WStaticImage) {
                        WStaticImage image = (WStaticImage) predicate;
                        return HelperMethods.positionsXYSame(image.getPosition(), pos) && image.getTexture() == neither;
                    }
                    return false;
                })).findFirst();

                if (opt.isPresent()) {
                    WStaticImage image = (WStaticImage) opt.get();
                    image.setTexture(neither);
                }
            }
        });
        WHorizontalSlider powerSlider = tab.getBody().createChild(WHorizontalSlider::new, Position.ofBottomLeft(tabHolder).add(4, -24, 0), Size.of(64 * 2, 8)).setMin(0).setMax(10).setProgress(5).setOnMouseReleased((widget, mouseX, mouseY, mouseButton) -> {
            if (selectedModule != null && selectedModule.isConfigurable()) {
                if (ModuleManager.hasModule(player.getEquippedStack(slot), selectedModule)) {
                    ModuleManager.setConfiguredValue(player.getEquippedStack(slot), selectedModule, (int) widget.getProgress());
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeString(selectedModule.getName().toString());
                    passedData.writeString(slot.getName());
                    passedData.writeInt((int) widget.getProgress());
                    ClientSidePacketRegistry.INSTANCE.sendToServer(PacketRegistry.CHANGE_POWER, passedData);
                }
            }
        });
        int c = 0;
        int y = 0;
        for (AmexModule amexModule : moduleList) {
            WTooltipModule tipMod = tab.createChild(WTooltipModule::new, Position.of(tabHolder).add(4 + (20 * (c)), 28 + (20 * y), 0), Size.of(16, 16)).setStack(amexModule.getItemStack()).setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> {
                //if (widget.isFocused()) {
                    if (!tab.getBody().isHidden()) {
                        selectedModule = widget.getModule();
                        pos = widget.getPosition();
                        desc.setText(new TranslatableText("desc.module." + widget.getModule().getName().getPath()));
                        powerSlider.setHidden(!widget.getModule().isConfigurable());
                    }
                //}
            });
            tipMod.setModule(amexModule);
            WStaticImage check = tab.createChild(WStaticImage::new, Position.of(tabHolder).add(4 + (20 * (c)), 28, 55), Size.of(16, 16)).setTexture(checkTexture);
            check.setTexture(ModuleManager.hasModule(player.getEquippedStack(slot), amexModule) ? checkTexture : neither);
            pos = Position.of(tabHolder).add(4 + (20 * (c)), 28, 10);
            //WStaticImage check = tab.createChild(WStaticImage::new, Position.of(tabHolder).add(4 + (20 * (c)), 28, 10), Size.of(16, 16)).setTexture(checkTexture);
            if (c == 5) {
                y++;
            }
            c = c == 5 ? 0 : c + 1;
        }
    }
}