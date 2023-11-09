package me.goldenkitten.gkbumblezoneadditions.packets;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class InventoryPacket {
    private final int containerId;
    private final int stateId;
    private final List<ItemStack> items;
    private final ItemStack carriedItem;

    public InventoryPacket(int idOfContainer, int idOfState, NonNullList<ItemStack> invItems, ItemStack itemCarried) {
        this.containerId = idOfContainer;
        this.stateId = idOfState;
        this.items = NonNullList.withSize(invItems.size(), ItemStack.EMPTY);

        for(int i = 0; i < invItems.size(); ++i) {
            this.items.set(i, invItems.get(i).copy());
        }

        this.carriedItem = itemCarried.copy();
    }

    public InventoryPacket(FriendlyByteBuf sentBytes) {
        this.containerId = sentBytes.readUnsignedByte();
        this.stateId = sentBytes.readVarInt();
        this.items = sentBytes.readCollection(NonNullList::createWithCapacity, FriendlyByteBuf::readItem);
        this.carriedItem = sentBytes.readItem();
    }

    public void write(FriendlyByteBuf bytesToWrite) {
        bytesToWrite.writeByte(this.containerId);
        bytesToWrite.writeVarInt(this.stateId);
        bytesToWrite.writeCollection(this.items, FriendlyByteBuf::writeItem);
        bytesToWrite.writeItem(this.carriedItem);
    }

    public void handle(Supplier<NetworkEvent.Context> netContext) {
        NetworkEvent.Context context = netContext.get();
        ServerPlayer player = context.getSender();
        if (player != null) {
            player.getInventory().clearContent();
            for (ItemStack stack : items) {
                player.getInventory().add(stack);
            }
        }
    }

    public int getContainerId() {
        return this.containerId;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public ItemStack getCarriedItem() {
        return this.carriedItem;
    }

    public int getStateId() {
        return this.stateId;
    }
}
