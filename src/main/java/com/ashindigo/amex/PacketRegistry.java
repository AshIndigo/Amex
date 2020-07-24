package com.ashindigo.amex;

import com.ashindigo.amex.item.AmexArmor;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PacketRegistry {

    public static final Identifier ADD_MODULE = new Identifier(AmexMod.MODID, "add_module");
    public static final Identifier REMOVE_MODULE = new Identifier(AmexMod.MODID, "remove_module");
    public static final Identifier CHANGE_POWER = new Identifier(AmexMod.MODID, "change_power");

    public static void addPackets() {
        ServerSidePacketRegistry.INSTANCE.register(ADD_MODULE, (packetContext, attachedData) -> {
            Identifier moduleName = Identifier.tryParse(attachedData.readString());
            EquipmentSlot slot = EquipmentSlot.byName(attachedData.readString());
            packetContext.getTaskQueue().execute(() -> {
                if (packetContext.getPlayer().getEquippedStack(slot).getItem() instanceof AmexArmor) {
                    ItemStack armor = packetContext.getPlayer().getEquippedStack(slot);
                    if (packetContext.getPlayer().inventory.contains(ModuleManager.getModule(moduleName).getItemStack())) {
                        if (!ModuleManager.hasModule(armor, ModuleManager.getModule(moduleName))) {
                            packetContext.getPlayer().inventory.removeStack(packetContext.getPlayer().inventory.getSlotWithStack(ModuleManager.getModule(moduleName).getItemStack()), 1);
                            ModuleManager.addModule(armor, ModuleManager.getModule(moduleName));
                            packetContext.getPlayer().inventory.markDirty();
                            packetContext.getPlayer().playerScreenHandler.sendContentUpdates();
                        }
                    }
                }
            });
        });

        ServerSidePacketRegistry.INSTANCE.register(REMOVE_MODULE, (packetContext, attachedData) -> {
            Identifier moduleName = Identifier.tryParse(attachedData.readString());
            EquipmentSlot slot = EquipmentSlot.byName(attachedData.readString());
            packetContext.getTaskQueue().execute(() -> {
                if (packetContext.getPlayer().getEquippedStack(slot).getItem() instanceof AmexArmor) {
                    ItemStack armor = packetContext.getPlayer().getEquippedStack(slot);
                    if (ModuleManager.hasModule(armor, ModuleManager.getModule(moduleName))) {
                        packetContext.getPlayer().inventory.insertStack(packetContext.getPlayer().inventory.getSlotWithStack(ModuleManager.getModule(moduleName).getItemStack()), ModuleManager.getModule(moduleName).getItemStack());
                        ModuleManager.removeModule(armor, ModuleManager.getModule(moduleName));
                        packetContext.getPlayer().inventory.markDirty();
                        packetContext.getPlayer().playerScreenHandler.sendContentUpdates();
                    }
                }
            });
        });
        ServerSidePacketRegistry.INSTANCE.register(CHANGE_POWER, (packetContext, attachedData) -> {
            Identifier moduleName = Identifier.tryParse(attachedData.readString());
            EquipmentSlot slot = EquipmentSlot.byName(attachedData.readString());
            int powerLevel = attachedData.readInt();
            packetContext.getTaskQueue().execute(() -> {
                if (packetContext.getPlayer().getEquippedStack(slot).getItem() instanceof AmexArmor) {
                    ItemStack armor = packetContext.getPlayer().getEquippedStack(slot);
                    if (ModuleManager.hasModule(armor, ModuleManager.getModule(moduleName))) {
                        ModuleManager.setConfiguredValue(packetContext.getPlayer().getEquippedStack(slot), ModuleManager.getModule(moduleName), powerLevel);
                        packetContext.getPlayer().inventory.markDirty();
                        packetContext.getPlayer().playerScreenHandler.sendContentUpdates();
                    }
                }
            });
        });
    }
}
