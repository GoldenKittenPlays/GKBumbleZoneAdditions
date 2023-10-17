package me.goldenkitten.gkbumblezoneadditions.entity;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import me.goldenkitten.gkbumblezoneadditions.entity.goals.TraderBeeRandomFlyGoal;
import me.goldenkitten.gkbumblezoneadditions.entity.goals.TraderBeeTemptGoal;
import me.goldenkitten.gkbumblezoneadditions.sound.ModSounds;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class TraderBeeEntity extends BeehemothEntity implements Merchant {
    @Nullable
    protected MerchantOffers offers;
    @Nullable
    protected Player tradingPlayer;
    public TraderBeeEntity(EntityType<? extends BeehemothEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TraderBeeTemptGoal(this, 0.012D, Ingredient.of(BzTags.BEEHEMOTH_FAST_LURING_DESIRED_ITEMS)));
        this.goalSelector.addGoal(2, new TraderBeeTemptGoal(this, 0.006D, Ingredient.of(BzTags.BEEHEMOTH_DESIRED_ITEMS)));
        this.goalSelector.addGoal(3, new TraderBeeRandomFlyGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 60));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.FLYING_SPEED, 0.4000000059604645D)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean isTame() {
        return false;
    }

    @Override
    public void setTame(boolean tameStatus) {
        super.setTame(false);
    }

    @Override
    public void tame(@NotNull Player tamer) {

    }

    @Override
    public boolean isQueen() {
        return false;
    }

    @Override
    public void setQueen(boolean queen) {
        super.setQueen(false);
    }

    @Override
    public boolean isSaddleable() {
        return false;
    }

    @Override
    public void setSaddled(boolean saddled) {
        super.setSaddled(false);
    }

    @Override
    public boolean isSaddled() {
        return false;
    }

    @Override
    public void setTradingPlayer(@Nullable Player playerTrading) {
        boolean flag = this.getTradingPlayer() != null && playerTrading == null;
        setTradingPlayerForTrade(playerTrading);
        if (flag) {
            this.stopTrading();
        }
    }

    public void setTradingPlayerForTrade(@Nullable Player playerTrading) {
        this.tradingPlayer = playerTrading;
    }

    protected void stopTrading() {
        this.setTradingPlayerForTrade(null);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public @NotNull MerchantOffers getOffers() {
        ItemStack itemstack = new ItemStack(BzItems.ROYAL_JELLY_BOTTLE.get(), 1);

        ServerPlayer serverPlayer = ((ServerPlayer)this.tradingPlayer);
        assert serverPlayer != null;
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(BzCriterias.QUEENS_DESIRE_FINAL_ADVANCEMENT);
        Map<Advancement, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getProgress();
        this.offers = new MerchantOffers();
        if (advancement != null && advancementsProgressMap.containsKey(advancement) && advancementsProgressMap.get(advancement).isDone()) {
            this.offers.add(new MerchantOffer(itemstack, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), 1, getFriendship(), getFriendship()));
            return this.offers;
        }
        return this.offers;
    }

    @Override
    public float getFlyingSpeed() {
        return super.getFlyingSpeed();
    }

    @Override
    public void overrideOffers(@NotNull MerchantOffers newOffers) {

    }

    @Override
    public void notifyTrade(@NotNull MerchantOffer p_45305_) {

    }

    @Override
    public void notifyTradeUpdated(@NotNull ItemStack p_45308_) {

    }

    @Override
    public int getVillagerXp() {
        return 1;
    }

    @Override
    public void overrideXp(int p_45309_) {

    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public @NotNull SoundEvent getNotifyTradeSound() {
        return ModSounds.TRADER_BEE_TRADE_ACCEPTED.get();
    }

    @Override
    public boolean isClientSide() {
        return false;
    }
}
