package de.varilx;

import de.varilx.configuration.VaxConfiguration;
import de.varilx.inventory.controller.GameInventoryController;
import de.varilx.utils.Metrics;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class BaseSpigotAPI extends BaseAPI {

    @Getter
    final JavaPlugin plugin;
    @Getter
    final GameInventoryController gameInventoryController;
    final Map<String, VaxConfiguration> languageConfigurations;
    final Metrics metrics;

    final int pluginId;

    VaxConfiguration configuration;
    VaxConfiguration databaseConfiguration;

    public BaseSpigotAPI(JavaPlugin plugin, int pluginId) {
        this.plugin = plugin;
        this.pluginId = pluginId;
        this.languageConfigurations = new HashMap<>();
        this.gameInventoryController = new GameInventoryController(this);
        this.metrics = new Metrics(plugin, pluginId);
        BaseAPI.set(this);
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

    @Override
    public @Nullable VaxConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public @Nullable VaxConfiguration getLanguageConfiguration(String language) {
        return this.languageConfigurations.get(language);
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public @Nullable VaxConfiguration getDatabaseConfiguration() {
        return this.databaseConfiguration;
    }
}
