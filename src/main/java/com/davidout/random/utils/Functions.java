package com.davidout.random.utils;

import org.bukkit.Location;

import java.util.ArrayList;

public class Functions {

    public static ArrayList<Location> getBlockSphere(Location centerblock, int radius, boolean hollow) {
        ArrayList<Location> circleBlocks = new ArrayList();

        int bx = centerblock.getBlockX();
        int by = centerblock.getBlockY();
        int bz = centerblock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Location loc = new Location(centerblock.getWorld(), x, y, z);
                        circleBlocks.add(loc);
                    }
                }
            }
        }

        return circleBlocks;
    }
}
