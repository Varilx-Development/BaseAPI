package de.varilx;

import de.varilx.config.Configuration;
import de.varilx.inventory.controller.GameInventoryController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseAPI {

    @Getter
    static BaseAPI baseAPI;

    final JavaPlugin plugin;
    final GameInventoryController gameInventoryController;
    final Map<String, Configuration> languageConfigurations;

    Configuration configuration;
    Configuration databaseConfiguration;

    public BaseAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        this.languageConfigurations = new HashMap<>();
        this.gameInventoryController = new GameInventoryController(this);
        BaseAPI.baseAPI = this;
    }

    public void enable() {
        this.databaseConfiguration = new Configuration(plugin.getDataFolder(), "database.yml");
        this.configuration = new Configuration(plugin.getDataFolder(), "config.yml");

        Locale.availableLocales().forEach(locale -> {
            @Nullable InputStream resource = plugin.getResource("lang/" + locale.getLanguage() + ".yml");
            if (resource == null) return;
            if (locale.getLanguage().isEmpty()) return;

            Configuration config = new Configuration(plugin.getDataFolder(), "lang/" + locale.getLanguage() + ".yml");
            this.languageConfigurations.put(locale.getLanguage(), config);
        });
    }

}
