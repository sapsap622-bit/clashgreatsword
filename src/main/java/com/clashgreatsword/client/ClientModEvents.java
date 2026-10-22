package com.clashgreatsword.client;

import com.clashgreatsword.ClashGreatsword;
import com.clashgreatsword.Config;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = ClashGreatsword.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ResourceLocation id = ResourceLocation.parse(Config.WEAPON_ID.get());
        ModelResourceLocation key = ModelResourceLocation.inventory(id);
        BakedModel model = event.getModels().get(key);
        if (model != null) {
            event.getModels().put(key, new ReverseGripModel(model));
        }
    }
}
