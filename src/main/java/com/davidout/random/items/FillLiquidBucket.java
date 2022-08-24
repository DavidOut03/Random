package com.davidout.random.items;

import com.davidout.random.Main;
import com.davidout.random.utils.Functions;
import com.davidout.random.utils.Item;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class FillLiquidBucket extends RandomItem implements Listener {

    // lake filler
    @EventHandler
    public void onFill(PlayerBucketEmptyEvent e) {
        ItemStack usedItem = e.getPlayer().getInventory().getItemInMainHand();
        if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.BUCKET)) {
            usedItem = e.getPlayer().getInventory().getItemInOffHand();
        }

        if(!Item.itemIsSameAs(usedItem, new FillLiquidBucket().getItem())) return;
        new FillLiquidBucket().useItem(e.getBlock());
    }

    public ItemStack getItem() {
        String[] lore = {"&7Click with the bucket on water to fill the whole lake."};
        return Item.createItem(Material.WATER_BUCKET, "&dLake Filler", lore, 1);
    }

    // /fill -3 62 -3 3 60 3 minecraft:water
    public void useItem(Block b) {
        Location savedLocation = b.getLocation().clone();
        Material mat = b.getType();

        fill(savedLocation, mat, 0);
    }

    // /fill -3 62 3 3 60 -3 minecraft:water

    public void fill(Location blockLocation, Material mat, int limit) {
        List<Location> addedBlocks = new ArrayList<>();
        addedBlocks.add(blockLocation);

        final int[] placed = {0};

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Location> lastBlocks = new ArrayList<>(addedBlocks);
                addedBlocks.clear();
                for(Location currentLocation : lastBlocks) {
                    for(Block cBlock : getBlocksAround(currentLocation.getBlock())) {
                        if(!cBlock.getType().equals(Material.AIR)) continue;
                        if(lastBlocks.contains(cBlock.getLocation()) || addedBlocks.contains(cBlock.getLocation())) continue;
                        addedBlocks.add(cBlock.getLocation());
                    }
                }

                if(limit > 0 && placed[0] >= limit) {
                    cancel();
                    return;
                }

                if(addedBlocks.isEmpty()) {
                    cancel();
                    return;
                }

                addedBlocks.forEach(location -> {
                    location.getBlock().setType(Material.WATER);
                    placed[0]++;
                });

            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 2);

    }

    public List<Block> getBlocksAround(Block b) {
        List<Block> blocks = new ArrayList<>();

//        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ()));


        // X
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() ));
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() ));

        // Y
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY() - 1, b.getLocation().getBlockZ() ));

        // Z
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1 ));
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1 ));

        return blocks;
    }
}
