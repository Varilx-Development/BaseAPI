package de.varilx;

import de.varilx.configuration.VaxConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public abstract class BaseAPI {

    private static BaseAPI INSTANCE;

    public static BaseAPI get() {
        return BaseAPI.INSTANCE;
    }

    public static void set(BaseAPI instance) {
        if (INSTANCE != null) throw new IllegalStateException("Already initialized");
        BaseAPI.INSTANCE = instance;
    }

    public Logger getLogger() {
        return Logger.getLogger("BaseAPI");
    }

    public abstract boolean isDatabaseDisabled();


    @Nullable // When enable is not called
    public abstract VaxConfiguration getDatabaseConfiguration();
    @Nullable // When enable is not called
    public abstract VaxConfiguration getConfiguration();

    public final VaxConfiguration getCurrentLanguageConfiguration() {
        return this.getLanguageConfiguration(this.getLanguage());
    }

    public abstract VaxConfiguration getLanguageConfiguration(String language);

    public final String getLanguage() {
        return this.getConfiguration().getString("language", "en");
    }

}
