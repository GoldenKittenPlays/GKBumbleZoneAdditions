package me.goldenkitten.gkbumblezoneadditions.entity.goals;

import me.goldenkitten.gkbumblezoneadditions.entity.TraderBeeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class TraderBeeRandomFlyGoal extends Goal {
    private final TraderBeeEntity traderBeeEntity;
    private BlockPos target = null;

    public TraderBeeRandomFlyGoal(TraderBeeEntity traderbeeEntity) {
        this.traderBeeEntity = traderbeeEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public void start() {
        target = getBlockInViewTraderBee();
        if (target != null) {
            traderBeeEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, traderBeeEntity.getFlyingSpeed());
        }
    }

    public boolean canUse() {
        MoveControl movementcontroller = traderBeeEntity.getMoveControl();
        if (traderBeeEntity.isStopWandering()) {
            return false;
        }
        if (!movementcontroller.hasWanted() || target == null) {
            target = getBlockInViewTraderBee();
            if (target != null) {
                traderBeeEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, traderBeeEntity.getFlyingSpeed());
            }
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        return target != null && !traderBeeEntity.isStopWandering() && traderBeeEntity.distanceToSqr(Vec3.atCenterOf(target)) > 2.4D && traderBeeEntity.getMoveControl().hasWanted() && !traderBeeEntity.horizontalCollision;
    }

    public void stop() {
        target = null;
    }

    public void tick() {
        if (target == null) {
            target = getBlockInViewTraderBee();
        }
        if (target != null) {
            double distance = traderBeeEntity.distanceToSqr(Vec3.atCenterOf(target));
            if (distance < 2.5D) {
                target = null;
                return;
            }

            float flyingSpeed = traderBeeEntity.isVehicle() ? traderBeeEntity.getFlyingSpeed() / 5 : traderBeeEntity.getFlyingSpeed();
            traderBeeEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, flyingSpeed);
        }
    }

    public BlockPos getBlockInViewTraderBee() {
        float radius = 3 + traderBeeEntity.getRandom().nextInt(6);
        float neg = traderBeeEntity.getRandom().nextBoolean() ? 1 : -1;
        float renderYawOffset = traderBeeEntity.yBodyRot;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (traderBeeEntity.getRandom().nextFloat() * neg);
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        BlockPos radialPos = BlockPos.containing(traderBeeEntity.getX() + extraX, traderBeeEntity.getY() + 2, traderBeeEntity.getZ() + extraZ);
        BlockPos ground = getGroundPosition(traderBeeEntity.level(), radialPos);
        BlockPos newPos = ground.above(1 + traderBeeEntity.getRandom().nextInt(6));
        if (!traderBeeEntity.isTargetBlocked(Vec3.atCenterOf(newPos)) && traderBeeEntity.distanceToSqr(Vec3.atCenterOf(newPos)) > 6) {
            return newPos;
        }
        return null;
    }

    private BlockPos getGroundPosition(Level level, BlockPos radialPos) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        mutableBlockPos.set(radialPos);

        while (level.isInWorldBounds(mutableBlockPos) && level.isEmptyBlock(mutableBlockPos)) {
            mutableBlockPos.move(Direction.DOWN);
        }

        if (!level.isInWorldBounds(mutableBlockPos)) {
            return radialPos;
        }

        return mutableBlockPos;
    }
}
