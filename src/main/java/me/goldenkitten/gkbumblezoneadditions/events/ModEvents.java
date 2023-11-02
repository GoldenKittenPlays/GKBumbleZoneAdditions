package me.goldenkitten.gkbumblezoneadditions.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import me.goldenkitten.gkbumblezoneadditions.entity.ModEntities;
import me.goldenkitten.gkbumblezoneadditions.entity.TraderBeeEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = GKBumbleZoneAdditions.MODID)
public class ModEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMobDeath(LivingDeathEvent event) {
        if((event.getEntity() instanceof Spider || event.getEntity() instanceof TraderBeeEntity) && event.getSource().getEntity() instanceof Skeleton) {
            Item item = BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV.get();
            event.getEntity().spawnAtLocation(item);
        }
    }

    @SubscribeEvent
    public void playerTradedEvent(ScreenEvent event) {
        if (Screen.hasShiftDown()) {
            if (event.getScreen().getTitle().getString().equals("Bumble Trader")) {
                MerchantScreen screen = (MerchantScreen) event.getScreen();
                MerchantMenu menu = screen.getMenu();
                if (menu.getSlot(0).getItem().is(Items.ENCHANTED_GOLDEN_APPLE) || menu.getSlot(1).getItem().is(Items.ENCHANTED_GOLDEN_APPLE)) {
                    Slot clickedSlot = menu.getSlot(2);
                    if (clickedSlot.getItem().is(BzItems.ROYAL_JELLY_BOTTLE.get())) {
                        if (event.getScreen().isMouseOver(clickedSlot.x, clickedSlot.y)) {
                            if (screen.mouseClicked(clickedSlot.x, clickedSlot.y, InputConstants.MOUSE_BUTTON_LEFT)) {
                                GKBumbleZoneAdditions.LOGGER.debug("Meow I am but a Bumble Trader!");
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void entityRightClicked(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            if (event.getTarget() instanceof TraderBeeEntity bee) {
                ItemStack itemstack = player.getItemInHand(event.getHand());
                if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && bee.isAlive() && !bee.isTrading() && !bee.isSleeping() && !player.isSecondaryUseActive()) {
                    if (bee.isBaby()) {
                        //this.setUnhappy();
                        event.setCanceled(true);
                    } else {
                        ServerPlayer sp = ((ServerPlayer) player);
                        bee.overrideOffers(bee.generateTradesForPlayer(sp));
                        boolean flag = bee.getOffers().isEmpty();
                        if (event.getHand() == InteractionHand.MAIN_HAND) {
                            if (flag && !bee.level().isClientSide) {
                                //this.setUnhappy();
                            }

                            //player.awardStat(Stats.TALKED_TO_VILLAGER);
                        }

                        if (flag) {
                            event.setCanceled(true);
                        } else {
                            if (!bee.level().isClientSide) {
                                if (!bee.getOffers().isEmpty()) {
                                    bee.startTrading(player);
                                }
                            }
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void jukeboxChangeDiscEvent(PlayerInteractEvent.RightClickBlock event) {
        BlockState eventBlockState = event.getLevel().getBlockState(event.getPos());
        ItemStack eventItem = event.getItemStack();
        if (eventBlockState.is(Blocks.JUKEBOX)) {
            if (eventItem.is(BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV.get())) {
                AABB searchBox = AABB.ofSize(event.getPos().getCenter(), 42, 256, 42);
                List<TraderBeeEntity> existingTraderBees = event.getLevel().getEntitiesOfClass(TraderBeeEntity.class, searchBox);
                if (existingTraderBees.size() < 30) {
                    for (int i = 0; i < 30 - existingTraderBees.size(); i++) {
                        TraderBeeEntity traderBee = new TraderBeeEntity(ModEntities.BUMBLE_TRADER.get(), event.getLevel());
                        Vec3 pos = event.getEntity().position();
                        traderBee.setPos(new Vec3(pos.x() + 7, pos.y(), pos.z() + 7));
                        event.getLevel().addFreshEntity(traderBee);
                        GKBumbleZoneAdditions.LOGGER.debug("Spawning Bees!");
                    }
                }
                else {
                    Vec3 pos = event.getEntity().position();
                    for(TraderBeeEntity traderBee : existingTraderBees) {
                        traderBee.moveTo(pos.x + 7, pos.y, pos.z + 7);
                    }
                }
                GKBumbleZoneAdditions.LOGGER.debug("Start the Bee Craze!");
            }
        }
    }
}
