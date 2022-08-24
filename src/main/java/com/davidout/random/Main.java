package com.davidout.random;

import com.davidout.random.events.GiveItemEvent;
import com.davidout.random.events.ItemEvents;
import com.davidout.random.items.EmptyLiquidBucket;
import com.davidout.random.items.FillLiquidBucket;
import com.davidout.random.items.TNTBow;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        registerItems();
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ItemEvents(), this);
        pm.registerEvents(new GiveItemEvent(), this);
    }

    public void registerItems() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EmptyLiquidBucket(), this);
        pm.registerEvents(new FillLiquidBucket(), this);
        pm.registerEvents(new TNTBow(), this);
    }
}
