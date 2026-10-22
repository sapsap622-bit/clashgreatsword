package com.clashgreatsword.client;

import com.clashgreatsword.ChargedStrikePayload;
import com.clashgreatsword.ClashGreatsword;
import com.clashgreatsword.Config;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = ClashGreatsword.MODID, value = Dist.CLIENT)
public final class ClientCharge {
    private static boolean charging = false;
    private static int chargeTicks = 0;

    private ClientCharge() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            reset();
            return;
        }

        boolean holding = ClashGreatsword.isWeapon(player.getMainHandItem());
        boolean keyDown = mc.options.keyUse.isDown();
        boolean allowed = holding
                && mc.screen == null
                && !player.isSpectator()
                && !player.isUsingItem()
                && !player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem());

        boolean lookingAtBlock = mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK;

        if (allowed && keyDown && (charging || !lookingAtBlock)) {
            charging = true;
            chargeTicks = Math.min(chargeTicks + 1, Config.MAX_CHARGE_TICKS.get());
        } else if (charging) {
            if (allowed && chargeTicks >= Config.MIN_CHARGE_TICKS.get()) {
                PacketDistributor.sendToServer(new ChargedStrikePayload(chargeTicks));
                player.swing(InteractionHand.MAIN_HAND);
            }
            reset();
        }
    }

    @SubscribeEvent
    public static void onMovement(MovementInputUpdateEvent event) {
        if (charging) {
            event.getInput().forwardImpulse *= 0.6F;
            event.getInput().leftImpulse *= 0.6F;
        }
    }

    /** Animacion de "tirar hacia atras" en primera persona mientras se carga. */
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (!charging || event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!ClashGreatsword.isWeapon(event.getItemStack())) return;

        float max = Config.MAX_CHARGE_TICKS.get();
        float progress = Mth.clamp((chargeTicks + event.getPartialTick()) / max, 0.0F, 1.0F);
        PoseStack ps = event.getPoseStack();

        ps.translate(0.12F * progress, -0.08F * progress, 0.22F * progress);
        ps.mulPose(Axis.XP.rotationDegrees(-20.0F * progress));
        ps.mulPose(Axis.YP.rotationDegrees(15.0F * progress));

        // pequeno temblor al llegar a carga completa
        if (chargeTicks >= max) {
            float t = (Minecraft.getInstance().player.tickCount + event.getPartialTick()) * 2.5F;
            ps.translate(Mth.sin(t) * 0.004F, Mth.cos(t * 1.3F) * 0.004F, 0.0F);
        }
    }

    private static void reset() {
        charging = false;
        chargeTicks = 0;
    }
}
