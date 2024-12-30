package de.varilx.config;

import de.varilx.utils.ResourceUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Configuration {

    @Getter
    private YamlConfiguration config;
    private final File configFile;

    public Configuration(File dataFolder, String configName) {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        this.configFile = new File(dataFolder, configName);

        this.config = YamlConfiguration.loadConfiguration(configFile);

        if (!configFile.exists()) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configName)) {
                if (inputStream != null) {
                    YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
                    defaultConfig.save(configFile);
                    this.config.setDefaults(defaultConfig);
                } else {
                    System.out.println("Could not find resource: " + configName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
