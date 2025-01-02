package de.varilx.configuration;

import de.varilx.BaseAPI;
import de.varilx.configuration.file.YamlConfiguration;

import java.io.*;

public class VaxConfiguration extends YamlConfiguration {

    private YamlConfiguration config;
    private final File configFile;

    public VaxConfiguration(String yaml) {
        this.configFile = null;
        this.config = YamlConfiguration.loadConfiguration(new StringReader(yaml));
    }

    public VaxConfiguration(File dataFolder, String configName) {
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
        if (this.configFile == null) {
            BaseAPI.get().getLogger().warning("Tried to save file while file is not set");
            return;
        }
        try {
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig() {
        if (this.configFile == null) {
            BaseAPI.get().getLogger().warning("Tried to save file while file is not set");
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
