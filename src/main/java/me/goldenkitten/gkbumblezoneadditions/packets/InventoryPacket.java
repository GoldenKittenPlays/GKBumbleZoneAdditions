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

    public InventoryPacket(int p_182704_, int p_182705_, NonNullList<ItemStack> p_182706_, ItemStack p_182707_) {
        this.containerId = p_182704_;
        this.stateId = p_182705_;
        this.items = NonNullList.withSize(p_182706_.size(), ItemStack.EMPTY);

        for(int i = 0; i < p_182706_.size(); ++i) {
            this.items.set(i, p_182706_.get(i).copy());
        }

        this.carriedItem = p_182707_.copy();
    }

    public InventoryPacket(FriendlyByteBuf p_178823_) {
        this.containerId = p_178823_.readUnsignedByte();
        this.stateId = p_178823_.readVarInt();
        this.items = p_178823_.readCollection(NonNullList::createWithCapacity, FriendlyByteBuf::readItem);
        this.carriedItem = p_178823_.readItem();
    }

    public void write(FriendlyByteBuf p_131956_) {
        p_131956_.writeByte(this.containerId);
        p_131956_.writeVarInt(this.stateId);
        p_131956_.writeCollection(this.items, FriendlyByteBuf::writeItem);
        p_131956_.writeItem(this.carriedItem);
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
