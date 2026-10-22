package com.clashgreatsword.client;

import com.clashgreatsword.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

/**
 * Envuelve el modelo de la espada y, despues de aplicar la transformacion normal de mano,
 * la rota alrededor de un pivote (la empunadura) para simular reverse grip.
 */
public class ReverseGripModel extends BakedModelWrapper<BakedModel> {

    public ReverseGripModel(BakedModel original) {
        super(original);
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext context, PoseStack poseStack, boolean leftHand) {
        BakedModel result = super.applyTransform(context, poseStack, leftHand);

        if (!Config.REVERSE_GRIP.get()) return result;

        switch (context) {
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> apply(poseStack,
                    Config.FP_ROT_X.get(), Config.FP_ROT_Y.get(), Config.FP_ROT_Z.get(),
                    Config.FP_PIVOT_X.get(), Config.FP_PIVOT_Y.get(), Config.FP_PIVOT_Z.get());
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> apply(poseStack,
                    Config.TP_ROT_X.get(), Config.TP_ROT_Y.get(), Config.TP_ROT_Z.get(),
                    Config.TP_PIVOT_X.get(), Config.TP_PIVOT_Y.get(), Config.TP_PIVOT_Z.get());
            default -> { }
        }
        return result;
    }

    private static void apply(PoseStack ps, double rx, double ry, double rz, double px, double py, double pz) {
        ps.translate(px, py, pz);
        ps.mulPose(Axis.XP.rotationDegrees((float) rx));
        ps.mulPose(Axis.YP.rotationDegrees((float) ry));
        ps.mulPose(Axis.ZP.rotationDegrees((float) rz));
        ps.translate(-px, -py, -pz);
    }
}
