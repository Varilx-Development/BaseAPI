package de.varilx;

import de.varilx.configuration.VaxConfiguration;
import de.varilx.configuration.file.YamlConfiguration;
import de.varilx.inventory.controller.GameInventoryController;
import de.varilx.utils.Metrics;
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
    final Map<String, VaxConfiguration> languageConfigurations;
    final Metrics metrics;

    final int pluginId;

    VaxConfiguration configuration;
    VaxConfiguration databaseConfiguration;

    public BaseAPI(JavaPlugin plugin, int pluginId) {
        this.plugin = plugin;
        this.pluginId = pluginId;
        this.languageConfigurations = new HashMap<>();
        this.gameInventoryController = new GameInventoryController(this);
        this.metrics = new Metrics(plugin, pluginId);
        BaseAPI.baseAPI = this;
    }

    public void enable() {
        this.databaseConfiguration = new VaxConfiguration(plugin.getDataFolder(), "database.yml");
        this.configuration = new VaxConfiguration(plugin.getDataFolder(), "config.yml");

        Locale.availableLocales().forEach(locale -> {
            @Nullable InputStream resource = plugin.getResource("lang/" + locale.getLanguage() + ".yml");
            if (resource == null) return;
            if (locale.getLanguage().isEmpty()) return;

            VaxConfiguration config = new VaxConfiguration(plugin.getDataFolder(), "lang/" + locale.getLanguage() + ".yml");
            this.languageConfigurations.put(locale.getLanguage(), config);
        });
        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> {
            String language = configuration.getString("language", "en");
            if (!this.languageConfigurations.containsKey(language)) return "en";
            return language;
        }));
    }

}
