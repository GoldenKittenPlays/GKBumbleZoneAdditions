package me.goldenkitten.gkbumblezoneadditions.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BumbleTradeTracker extends SavedData {
    private final Map<UUID, Map<UUID, Long>> cooldowns = new HashMap<>();

    public static BumbleTradeTracker get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                BumbleTradeTracker::load,
                BumbleTradeTracker::new,
                "bumble_trade_tracker"
        );
    }

    public boolean canTrade(UUID traderId, UUID playerId, long currentTime) {
        Map<UUID, Long> traderCooldowns = cooldowns.get(traderId);
        if (traderCooldowns == null) return true;
        Long lastTrade = traderCooldowns.get(playerId);
        return lastTrade == null || (currentTime - lastTrade) >= 24000;
    }

    public void recordTrade(UUID traderId, UUID playerId, long currentTime) {
        cooldowns.computeIfAbsent(traderId, k -> new HashMap<>()).put(playerId, currentTime);
        setDirty();
    }

    public static BumbleTradeTracker load(CompoundTag tag) {
        BumbleTradeTracker tracker = new BumbleTradeTracker();
        ListTag tradersList = tag.getList("Traders", Tag.TAG_COMPOUND);

        for (Tag traderTag : tradersList) {
            CompoundTag traderEntry = (CompoundTag) traderTag;
            UUID traderId = traderEntry.getUUID("Trader");
            ListTag playerList = traderEntry.getList("Cooldowns", Tag.TAG_COMPOUND);

            Map<UUID, Long> playerCooldowns = new HashMap<>();
            for (Tag pt : playerList) {
                CompoundTag p = (CompoundTag) pt;
                playerCooldowns.put(p.getUUID("Player"), p.getLong("Time"));
            }

            tracker.cooldowns.put(traderId, playerCooldowns);
        }

        return tracker;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        ListTag tradersList = new ListTag();

        for (Map.Entry<UUID, Map<UUID, Long>> traderEntry : cooldowns.entrySet()) {
            CompoundTag traderTag = new CompoundTag();
            traderTag.putUUID("Trader", traderEntry.getKey());

            ListTag playerList = new ListTag();
            for (Map.Entry<UUID, Long> playerEntry : traderEntry.getValue().entrySet()) {
                CompoundTag playerTag = new CompoundTag();
                playerTag.putUUID("Player", playerEntry.getKey());
                playerTag.putLong("Time", playerEntry.getValue());
                playerList.add(playerTag);
            }

            traderTag.put("Cooldowns", playerList);
            tradersList.add(traderTag);
        }

        tag.put("Traders", tradersList);
        return tag;
    }
}