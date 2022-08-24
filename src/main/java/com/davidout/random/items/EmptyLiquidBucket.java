package com.davidout.random.items;

import com.davidout.random.Main;
import com.davidout.random.utils.Functions;
import com.davidout.random.utils.Item;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class EmptyLiquidBucket extends RandomItem implements Listener {

    // lake emptier
    @EventHandler(priority = EventPriority.HIGHEST )
    public void onLiquidClick(PlayerBucketFillEvent e) {
        ItemStack usedItem = e.getPlayer().getInventory().getItemInMainHand();
        if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.BUCKET)) {
            usedItem = e.getPlayer().getInventory().getItemInOffHand();
        }

        if(!Item.itemIsSameAs(usedItem, new EmptyLiquidBucket().getItem())) return;
        e.setCancelled(true);
        new EmptyLiquidBucket().useItem(e.getBlock());
    }


    public ItemStack getItem() {
        String[] lore = {"&7Click with the bucket on water to empty the whole lake."};
        return Item.createItem(Material.BUCKET, "&dLake emptier", lore, 1);
    }

    // /fill -3 62 -3 3 60 3 minecraft:water
    public void useItem(Block b) {
        if(!b.isLiquid()) return;
        Location savedLocation = b.getLocation().clone();
        Material mat = b.getType();


        b.setType(Material.AIR);
       clearBlocksAround(savedLocation, mat, 0);
    }

    // /fill -3 62 3 3 60 -3 minecraft:water

    public void clearBlocksAround(Location blockLocation, Material mat, int limit) {

        List<Location> addedBlocks = new ArrayList<>();
        addedBlocks.add(blockLocation);

        final int removed[] = {0};

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Location> lastBlocks = new ArrayList<>(addedBlocks);
                addedBlocks.clear();

                for(Location currentLocation : lastBlocks) {
                    for(Block cBlock : getBlocksAround(currentLocation.getBlock())) {
                        if(!cBlock.getType().equals(mat) && !cBlock.getType().equals(Material.KELP_PLANT) && !cBlock.getType().equals(Material.TALL_SEAGRASS) && !cBlock.getType().equals(Material.SEAGRASS) && !cBlock.getType().equals(Material.GLOW_LICHEN)) continue;
                        if(lastBlocks.contains(cBlock.getLocation()) || addedBlocks.contains(cBlock.getLocation())) continue;
                        addedBlocks.add(cBlock.getLocation());
                    }
                }

                if(limit > 0 && removed[0] >= limit) {
                    cancel();
                    return;
                }

                if(addedBlocks.isEmpty()) {
                    cancel();
                    return;
                }

                addedBlocks.forEach(location -> {
                    location.getBlock().setType(Material.AIR);
                    removed[0]++;
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
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY() + 1, b.getLocation().getBlockZ() ));
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY() - 1, b.getLocation().getBlockZ() ));

        // Z
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1 ));
        blocks.add(b.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1 ));

        return blocks;
    }

}
