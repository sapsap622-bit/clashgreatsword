package com.clashgreatsword;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ChargedStrikePayload(int chargeTicks) implements CustomPacketPayload {
    public static final Type<ChargedStrikePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ClashGreatsword.MODID, "charged_strike"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChargedStrikePayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, ChargedStrikePayload::chargeTicks, ChargedStrikePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
