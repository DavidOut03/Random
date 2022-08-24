package com.davidout.random.items;

import com.davidout.random.Main;
import com.davidout.random.utils.Item;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TNTBow extends RandomItem implements Listener {

    // tnt bow
    @EventHandler
    public void onShoot(ProjectileLaunchEvent e) {
        if(!(e.getEntity() instanceof Arrow)) return;
        if(!(e.getEntity().getShooter() instanceof Player)) return;
        Arrow arrow = (Arrow) e.getEntity();
        Player shooter = (Player) e.getEntity().getShooter();

        ItemStack usedItem = shooter.getInventory().getItemInMainHand();
        if(shooter.getInventory().getItemInOffHand().getType().equals(Material.BUCKET)) {
            usedItem = shooter.getInventory().getItemInOffHand();
        }

        if(!Item.itemIsSameAs(usedItem, new TNTBow().getItem())) return;
        new TNTBow().useItem(arrow);
    }

    @EventHandler
    public void onLand(ProjectileHitEvent e) {
        if(!(e.getEntity() instanceof Arrow)) return;
        if(!(e.getEntity().getShooter() instanceof Player)) return;
        if(e.getHitEntity() != null) {
            if(e.getHitEntity().getType().equals(EntityType.PRIMED_TNT)) return;
        }

        if(e.getHitBlock() != null) {
            if(e.getHitBlock().getType().equals(Material.WATER)) return;
            if(e.getHitBlock().getType().equals(Material.LAVA)) return;
            if(e.getHitBlock().getType().equals(Material.COBWEB)) return;
        }

        Arrow arrow = (Arrow) e.getEntity();
        if(arrows.get(arrow) == null || arrows.get(arrow).isEmpty()) return;
        for(TNTPrimed tnt : arrows.get(arrow)) {
            tnt.setFuseTicks(0);
        }

        arrows.remove(arrow);
        arrow.remove();
    }

    private static HashMap<Arrow, List<TNTPrimed>> arrows = new HashMap<Arrow, List<TNTPrimed>>();

    public ItemStack getItem() {
        String[] lore = {"&7Shoot an arrow every new block there will spawn a tnt."};
        return Item.createItem(Material.BOW, "&cTNT Bow", lore, 1);
    }

    // /fill -3 62 -3 3 60 3 minecraft:water
    public void useItem(Arrow arrow) {
        arrows.put(arrow, new ArrayList<>());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {

                if(arrow == null || arrows.get(arrow) == null || arrow.getLocation().getBlock().getType().equals(Material.COBWEB)) {
                    if(arrows.get(arrow) == null) return;
                    TNTPrimed tnt = (TNTPrimed) arrow.getWorld().spawnEntity(arrow.getLocation(), EntityType.PRIMED_TNT);
                    tnt.setSource((Entity) arrow.getShooter());
                    arrows.get(arrow).add(tnt);
                    arrows.get(arrow).forEach(tntPrimed -> {
                        tntPrimed.setFuseTicks(100);
                    });

                    cancel();
                    return;
                }
                TNTPrimed tnt = (TNTPrimed) arrow.getWorld().spawnEntity(arrow.getLocation(), EntityType.PRIMED_TNT);
                tnt.setSource((Entity) arrow.getShooter());
               arrows.get(arrow).add(tnt);
               arrows.get(arrow).forEach(tntPrimed -> {
                   tntPrimed.setFuseTicks(100);
               });
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 3L);
    }


}
