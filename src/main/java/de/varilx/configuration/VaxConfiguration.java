package de.varilx.configuration;

import de.varilx.configuration.file.YamlConfiguration;
import lombok.Getter;

import java.io.*;

public class VaxConfiguration {

    @Getter
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
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public String getString(String path, String defaultValue) {
        return this.config.getString(path, defaultValue);
    }
}
