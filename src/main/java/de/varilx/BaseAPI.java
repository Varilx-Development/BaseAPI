package de.varilx;

import de.varilx.inventory.controller.GameInventoryController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.Plugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseAPI {

    @Getter static Plugin plugin;
    @Getter static GameInventoryController gameInventoryController;

    public static void enable(Plugin plugin) {
        BaseAPI.plugin = plugin;
        BaseAPI.gameInventoryController = new GameInventoryController();
    }

}
