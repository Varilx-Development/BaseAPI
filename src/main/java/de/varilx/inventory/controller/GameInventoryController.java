package de.varilx.inventory.controller;

import de.varilx.BaseAPI;
import de.varilx.inventory.GameInventory;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameInventoryController implements Listener {

    List<GameInventory> inventories;

    public GameInventoryController() {
        Plugin plugin = BaseAPI.getPlugin();
        this.inventories = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void registerInventory(GameInventory inventory) {
        inventories.add(inventory);
    }

    public void unregisterInventory(GameInventory shadowInventory) {
        inventories.remove(shadowInventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Optional<GameInventory> optionalGameInventory = getInventory(event.getInventory());
        if(optionalGameInventory.isEmpty())
            return;

        event.setCancelled(true);
        GameInventory gameInventory = optionalGameInventory.get();
        gameInventory.handleClick(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Optional<GameInventory> optionalGameInventory = getInventory(event.getInventory());
        if (optionalGameInventory.isEmpty())
            return;

        if(event.getPlayer().getOpenInventory() == null)
            unregisterInventory(optionalGameInventory.get());
    }

    private Optional<GameInventory> getInventory(Inventory inventory) {
        return inventories.stream().filter(shadowInventory -> shadowInventory.getInventory().equals(inventory)).findAny();
    }

}
