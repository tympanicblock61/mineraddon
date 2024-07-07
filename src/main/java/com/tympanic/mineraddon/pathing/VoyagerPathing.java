package com.tympanic.mineraddon.pathing;

import meteordevelopment.voyager.VInput;
import meteordevelopment.voyager.Voyager;
import meteordevelopment.voyager.goals.IGoal;
import meteordevelopment.voyager.goals.XYZGoal;
import net.minecraft.client.input.Input;
import net.minecraft.util.math.Vec3i;

import static meteordevelopment.voyager.Voyager.mc;

public class VoyagerPathing implements Pathing {
    public static VoyagerPathing INSTANCE = new VoyagerPathing();

    Input input;

    @Override
    public boolean canUseFlight() {
        return true;
    }

    @Override
    public boolean isPathing() {
        return Voyager.INSTANCE.isMoving();
    }

    @Override
    public void pathTo(int x, int y, int z) {
        paths.push(new XYZGoal(x,y,z));
    }

    @Override
    public void pathTo(Vec3i pos) {
        paths.push(new XYZGoal(pos.getX(),pos.getY(),pos.getZ()));
    }

    @Override
    public void followPath() {
        Voyager.INSTANCE.moveTo((XYZGoal) paths.pop());
    }

    @Override
    public void pausePathing() {
        if (mc.player != null) {
            this.input = mc.player.input;
            Voyager.INSTANCE.stop();
        }
    }

    @Override
    public void continuePathing() {
        if (mc.player != null) {
            Input temp = this.input;
            this.input = mc.player.input;
            mc.player.input = temp;
        }
    }

    @Override
    public void clearPath() {
        Voyager.INSTANCE.stop();
    }

    @Override
    public boolean isNearDestination(int threshold) {
        return false;
    }

    @Override
    public void onPathCompleted() {

    }

    @Override
    public void onPathFailed() {

    }

    @Override
    public void printPathfindingInfo() {

    }
}
