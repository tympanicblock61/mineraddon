package com.tympanic.mineraddon.pathing;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.calc.IPathFinder;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.process.IBaritoneProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.tympanic.mineraddon.AddonTemplate.pathing;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class BaritonePathing implements Pathing {
    public static BaritonePathing INSTANCE = new BaritonePathing();

    private boolean pathingPaused = false;
    private Goal current;

    public BaritonePathing () {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingControlManager().registerProcess(new BaritoneProcess());
    }

    @Override
    public boolean canUseFlight() {
        return true;
    }

    @Override
    public boolean isPathing() {
        return BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing();
    }

    @Override
    public void pathTo(int x, int y, int z) {
        paths.push(new GoalBlock(x,y,z));
    }

    @Override
    public void pathTo(@NotNull Vec3i pos) {
        paths.push(new GoalBlock(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public void followPath() {
        current =(Goal) paths.pop();
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(current);
    }

    @Override
    public void pausePathing() {
        pathingPaused = true;
    }

    @Override
    public void continuePathing() {
        pathingPaused = false;
    }

    @Override
    public void clearPath() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }

    @Override
    public boolean isNearDestination(int threshold) {
        if (mc.player != null && mc.player.getWorld() instanceof ServerWorld) {
            BlockPos pos = mc.player.getBlockPos();
            GoalBlock goal = (GoalBlock)BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal();
            double distance = Math.sqrt(
                Math.pow(pos.getX() - goal.x, 2) +
                Math.pow(pos.getY() - goal.y, 2) +
                Math.pow(pos.getZ() - goal.z, 2)
            );
            return distance <= threshold;
        }

        return false;
    }

    @Override
    public void onPathCompleted() {
        if (!paths.isEmpty()) {
            if (BaritoneAPI.getSettings().allowBreak.value) {
                // break blocks
            }
            followPath();
        } else {
            System.out.println(this.getClass().getSimpleName()+" finished all pathing goals");
        }
    }

    @Override
    public void onPathFailed() {
        // hmmm
    }

    @Override
    public void printPathfindingInfo() {
        // hmm
    }

    @EventHandler()
    public void onTick(TickEvent.Pre e) {
        if (pathing instanceof BaritonePathing) {
            Optional<? extends IPathFinder> pathFinder = BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getInProgress();
            if (pathFinder.isPresent()) {
                if (pathFinder.get().isFinished() && pathFinder.get().getGoal() == current) {
                    onPathCompleted();
                }
            }
        }
    }


    private class BaritoneProcess implements IBaritoneProcess {
        @Override
        public boolean isActive() {
            return pathingPaused;
        }

        @Contract("_, _ -> new")
        @Override
        public @NotNull PathingCommand onTick(boolean b, boolean b1) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getInputOverrideHandler().clearAllKeys();
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }

        @Override
        public boolean isTemporary() {
            return true;
        }

        @Override
        public void onLostControl() {
        }

        @Override
        public double priority() {
            return 0d;
        }

        @Contract(pure = true)
        @Override
        public @NotNull String displayName0() {
            return "Miner Addon";
        }
    }
}
