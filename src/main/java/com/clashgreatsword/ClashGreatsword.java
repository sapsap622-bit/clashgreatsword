package com.clashgreatsword;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(ClashGreatsword.MODID)
public class ClashGreatsword {
    public static final String MODID = "clashgreatsword";

    public ClashGreatsword(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        modBus.addListener(this::registerPayloads);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ChargedStrikePayload.TYPE, ChargedStrikePayload.STREAM_CODEC, ServerStrike::handle);
    }

    /** True si el item es la espada configurada (por defecto alloyed:steel_sword). */
    public static boolean isWeapon(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(Config.WEAPON_ID.get());
    }
}
