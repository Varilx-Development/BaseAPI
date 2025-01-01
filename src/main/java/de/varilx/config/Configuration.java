package de.varilx.config;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Configuration {

    @Getter
    private Map<String, Object> config;
    private final File configFile;

    public Configuration(String yaml) {
        this.configFile = null;
        this.config = new Yaml().load(yaml);
    }

    public Configuration(File dataFolder, String configName) {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        this.configFile = new File(dataFolder, configName);

        try {
            this.config = new Yaml().load(new FileInputStream(configFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (!configFile.exists()) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configName)) {
                if (inputStream != null) {
                    this.config = new Yaml().load(new InputStreamReader(inputStream));
                    this.saveConfig();
                } else {
                    System.out.println("Could not find resource: " + configName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        try {
            this.config = new Yaml().load(new FileInputStream(this.configFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig() {
        this.saveConfig(this.configFile);
    }

    public void saveConfig(File file) {
        try {
            new Yaml().dump(this.config, new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        Object o = this.get(path);
        if (o instanceof String s) return s;
        if (o == null) return null;
        return o.toString();
    }

    public Object get(String path) {
        return this.get(path, Object.class);
    }

    public <T> T get(String path, Class<T> clazz) {
        return (T) this.getValue(path, null);
    }

    public List<String> getStringList(String path) {
        return (List<String>) this.get(path);
    }

    public Map<Object, String> getConfigurationSection(String path) {
        return (Map<Object, String>) this.getValue(path, new HashMap<>());
    }

    private Object getValue(String path, Object defaultValue) {
        String[] keys = path.split("\\.");
        Object current = this.config;

        for (String key : keys) {
            if (current instanceof Map) {
                Map<Object, Object> map = (Map<Object, Object>) current;
                Object resolvedKey = this.resolveKey(map, key);

                if (resolvedKey != null) {
                    current = map.get(resolvedKey);
                } else {
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        }
        return current != null ? current : defaultValue;
    }

    @Nullable
    private Object resolveKey(Map<Object, Object> map, String key) {
        if (map.containsKey(key)) return key;
        for (Object mapKey : map.keySet()) {
            if (mapKey.toString().equals(key)) {
                return mapKey;
            }
        }
        return null;
    }
}
