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
    private final TraderBeeEntity traderBeeEntityEntity;
    private BlockPos target = null;

    public TraderBeeRandomFlyGoal(TraderBeeEntity traderbeeEntity) {
        this.traderBeeEntityEntity = traderbeeEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public void start() {
        target = getBlockInViewBeehemoth();
        if (target != null) {
            traderBeeEntityEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, traderBeeEntityEntity.getFlyingSpeed());
        }
    }

    public boolean canUse() {
        MoveControl movementcontroller = traderBeeEntityEntity.getMoveControl();
        if (traderBeeEntityEntity.isStopWandering()) {
            return false;
        }
        if (!movementcontroller.hasWanted() || target == null) {
            target = getBlockInViewBeehemoth();
            if (target != null) {
                traderBeeEntityEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, traderBeeEntityEntity.getFlyingSpeed());
            }
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        return target != null && !traderBeeEntityEntity.isStopWandering() && traderBeeEntityEntity.distanceToSqr(Vec3.atCenterOf(target)) > 2.4D && traderBeeEntityEntity.getMoveControl().hasWanted() && !traderBeeEntityEntity.horizontalCollision;
    }

    public void stop() {
        target = null;
    }

    public void tick() {
        if (target == null) {
            target = getBlockInViewBeehemoth();
        }
        if (target != null) {
            double distance = traderBeeEntityEntity.distanceToSqr(Vec3.atCenterOf(target));
            if (distance < 2.5D) {
                target = null;
                return;
            }

            float flyingSpeed = traderBeeEntityEntity.isVehicle() ? traderBeeEntityEntity.getFlyingSpeed() / 5 : traderBeeEntityEntity.getFlyingSpeed();
            traderBeeEntityEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, flyingSpeed);
        }
    }

    public BlockPos getBlockInViewBeehemoth() {
        float radius = 3 + traderBeeEntityEntity.getRandom().nextInt(6);
        float neg = traderBeeEntityEntity.getRandom().nextBoolean() ? 1 : -1;
        float renderYawOffset = traderBeeEntityEntity.yBodyRot;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (traderBeeEntityEntity.getRandom().nextFloat() * neg);
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        BlockPos radialPos = BlockPos.containing(traderBeeEntityEntity.getX() + extraX, traderBeeEntityEntity.getY() + 2, traderBeeEntityEntity.getZ() + extraZ);
        BlockPos ground = getGroundPosition(traderBeeEntityEntity.level(), radialPos);
        BlockPos newPos = ground.above(1 + traderBeeEntityEntity.getRandom().nextInt(6));
        if (!traderBeeEntityEntity.isTargetBlocked(Vec3.atCenterOf(newPos)) && traderBeeEntityEntity.distanceToSqr(Vec3.atCenterOf(newPos)) > 6) {
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
