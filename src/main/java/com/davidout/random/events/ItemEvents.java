package com.davidout.random.events;

import com.davidout.random.items.EmptyLiquidBucket;
import com.davidout.random.items.FillLiquidBucket;
import com.davidout.random.items.RandomItem;
import com.davidout.random.items.TNTBow;
import com.davidout.random.utils.Item;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;


public class ItemEvents implements Listener {


    public static ArrayList<Entity> explodingEntities = new ArrayList<>();

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if(explodingEntities.size() >= 1000) return;

        Entity et = e.getEntity();
        Entity et1 = et.getWorld().spawn(et.getLocation(), et.getClass());
        Entity et2 = et.getWorld().spawn(et.getLocation(), et.getClass());
        Entity et3 = et.getWorld().spawn(et.getLocation(), et.getClass());
        Entity et4 = et.getWorld().spawn(et.getLocation(), et.getClass());

        et1.setVelocity(new Vector().setY(.5).setX(.5));
        et2.setVelocity(new Vector().setY(.5).setX(-.5));
        et3.setVelocity(new Vector().setY(.5).setZ(.5));
        et4.setVelocity(new Vector().setY(.5).setZ(-.5));

        if(et.getType().equals(EntityType.CREEPER)) {
            Creeper cr1 = (Creeper) et1;
            Creeper cr2 = (Creeper) et2;
            Creeper cr3 = (Creeper) et3;
            Creeper cr4 = (Creeper) et4;
            cr1.setFuseTicks(20);
            cr2.setFuseTicks(20);
            cr3.setFuseTicks(20);
            cr4.setFuseTicks(20);
        }

        explodingEntities.add(et1);
        explodingEntities.add(et2);
        explodingEntities.add(et3);
        explodingEntities.add(et4);
    }





}
