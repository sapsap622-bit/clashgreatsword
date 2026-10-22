package com.clashgreatsword;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = ClashGreatsword.MODID)
public final class ServerStrike {
    /** Marca que el ataque en curso es un golpe cargado completo. */
    private static final ThreadLocal<Boolean> FULL_CHARGE = new ThreadLocal<>();

    private ServerStrike() {}

    public static void handle(ChargedStrikePayload payload, IPayloadContext ctx) {
        if (!(ctx.player() instanceof ServerPlayer player)) return;

        ItemStack stack = player.getMainHandItem();
        if (!ClashGreatsword.isWeapon(stack)) return;
        if (player.isSpectator() || !player.isAlive()) return;
        if (player.getCooldowns().isOnCooldown(stack.getItem())) return;

        int max = Config.MAX_CHARGE_TICKS.get();
        int ticks = Mth.clamp(payload.chargeTicks(), 0, max);
        if (ticks < Config.MIN_CHARGE_TICKS.get()) return;

        float progress = ticks / (float) max;
        boolean full = ticks >= max;

        double reach = player.entityInteractionRange() + Config.BONUS_REACH.get() * progress;

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        Vec3 end = eye.add(look.scale(reach));

        double maxDistSq = reach * reach;
        HitResult block = player.level().clip(
                new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (block.getType() != HitResult.Type.MISS) {
            maxDistSq = block.getLocation().distanceToSqr(eye);
        }

        AABB box = player.getBoundingBox().expandTowards(look.scale(reach)).inflate(1.0D);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(
                player, eye, end, box,
                e -> !e.isSpectator() && e.isPickable() && e instanceof LivingEntity,
                maxDistSq);

        player.swing(InteractionHand.MAIN_HAND, true);
        player.getCooldowns().addCooldown(stack.getItem(), Config.COOLDOWN_TICKS.get());

        if (hit == null) return;

        try {
            FULL_CHARGE.set(full);
            player.attack(hit.getEntity());
        } finally {
            FULL_CHARGE.remove();
        }
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        if (Boolean.TRUE.equals(FULL_CHARGE.get())) {
            event.setCriticalHit(true);
            event.setDamageMultiplier(Config.CRIT_MULTIPLIER.get().floatValue());
        }
    }
}
