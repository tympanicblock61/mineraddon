package com.tympanic.mineraddon.pathing;

import net.minecraft.util.math.Vec3i;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
public interface Pathing {
    ArrayDeque<Object> paths = new ArrayDeque<>();
    boolean canUseFlight();
    boolean isPathing();
    void pathTo(int x, int y, int z);
    void pathTo(Vec3i pos);
    void followPath();
    void pausePathing();
    void continuePathing();
    void clearPath();
    boolean isNearDestination(int threshold);
    void onPathCompleted();
    void onPathFailed();
    void printPathfindingInfo();
}
