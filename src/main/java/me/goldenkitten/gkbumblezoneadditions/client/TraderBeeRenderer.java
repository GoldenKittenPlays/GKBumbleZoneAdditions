package me.goldenkitten.gkbumblezoneadditions.client;

import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import me.goldenkitten.gkbumblezoneadditions.client.layer.ModLayers;
import me.goldenkitten.gkbumblezoneadditions.entity.TraderBeeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TraderBeeRenderer extends MobRenderer<TraderBeeEntity, TraderBeeModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation(GKBumbleZoneAdditions.MODID, "textures/model/entity/trader_bee.png")
    };
    public TraderBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new TraderBeeModel(context.bakeLayer(ModLayers.TRADER_BEE_LAYER)), 0.4f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TraderBeeEntity p_114482_) {
        return TEXTURES[0];
    }
}
