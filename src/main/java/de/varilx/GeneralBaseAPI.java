package de.varilx;

import de.varilx.configuration.VaxConfiguration;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneralBaseAPI extends BaseAPI {


    final Map<String, VaxConfiguration> languageConfigurations;
    final File dataFolder;
    final ClassLoader loader;

    @Nullable
    VaxConfiguration configuration;
    @Nullable
    VaxConfiguration databaseConfiguration;

    public GeneralBaseAPI(File dataFolder, ClassLoader loader) {
        this.loader = loader;
        this.dataFolder = dataFolder;
        this.languageConfigurations = new HashMap<>();
        BaseAPI.set(this);
    }

    public void enable() {
        this.databaseConfiguration = new VaxConfiguration(dataFolder, "database.yml");
        this.configuration = new VaxConfiguration(dataFolder, "config.yml");

        Locale.availableLocales().forEach(locale -> {
            @Nullable InputStream resource = loader.getResourceAsStream("lang/" + locale.getLanguage() + ".yml");
            if (resource == null) return;
            if (locale.getLanguage().isEmpty()) return;

            VaxConfiguration config = new VaxConfiguration(dataFolder, "lang/" + locale.getLanguage() + ".yml");
            this.languageConfigurations.put(locale.getLanguage(), config);
        });
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
        return log;
    }

    @Override
    public @Nullable VaxConfiguration getDatabaseConfiguration() {
        return this.databaseConfiguration;
    }
}
