package com.davidout.random.events;

import com.davidout.random.items.EmptyLiquidBucket;
import com.davidout.random.items.FillLiquidBucket;
import com.davidout.random.items.RandomItem;
import com.davidout.random.items.TNTBow;
import com.davidout.random.utils.Item;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GiveItemEvent implements Listener {

    @EventHandler
    public void playerInteracktEvent(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
//        e.getPlayer().sendMessage(e.getClickedBlock().getType().toString());

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.BEDROCK)) {
            e.getPlayer().getInventory().addItem(new EmptyLiquidBucket().getItem());
            e.getPlayer().getInventory().addItem(new FillLiquidBucket().getItem());

            e.getPlayer().getInventory().addItem(new TNTBow().getItem());
            e.getPlayer().getInventory().addItem(Item.createItem(Material.ARROW, 64));
        }
    }
}
