package me.goldenkitten.gkbumblezoneadditions.handlers;

import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import me.goldenkitten.gkbumblezoneadditions.packets.InventoryPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
                    new ResourceLocation(GKBumbleZoneAdditions.MODID, "main"))
            .serverAcceptedVersions((version) -> true)
            .clientAcceptedVersions((version) -> true)
            .networkProtocolVersion(() -> String.valueOf(1))
            .simpleChannel();
    public static int packetId;

    public static void register()
    {
        packetId = 0;
        INSTANCE.messageBuilder(InventoryPacket.class, packetId, NetworkDirection.PLAY_TO_SERVER)
                .encoder(InventoryPacket::write)
                .decoder(InventoryPacket::new)
                .consumerMainThread(InventoryPacket::handle)
                .add();
    }

    public static void sendToServer(Object msg)
    {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);
        packetId++;
    }

    public static void sendToPlayer(Object msg, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
        packetId++;
    }

    public static void sendToAllClients(Object msg)
    {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
        packetId++;
    }
}
