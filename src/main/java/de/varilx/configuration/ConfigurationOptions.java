package de.varilx.configuration;

import de.varilx.configuration.Configuration;
import de.varilx.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * Various settings for controlling the input and output of a {@link
 * de.varilx.configuration.Configuration}
 */
public class ConfigurationOptions {
    private char pathSeparator = '.';
    private boolean copyDefaults = false;
    private final de.varilx.configuration.Configuration configuration;

    protected ConfigurationOptions(@NotNull de.varilx.configuration.Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Returns the {@link de.varilx.configuration.Configuration} that this object is responsible for.
     *
     * @return Parent configuration
     */
    @NotNull
    public de.varilx.configuration.Configuration configuration() {
        return configuration;
    }

    /**
     * Gets the char that will be used to separate {@link
     * ConfigurationSection}s
     * <p>
     * This value does not affect how the {@link de.varilx.configuration.Configuration} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @return Path separator
     */
    public char pathSeparator() {
        return pathSeparator;
    }

    /**
     * Sets the char that will be used to separate {@link
     * ConfigurationSection}s
     * <p>
     * This value does not affect how the {@link de.varilx.configuration.Configuration} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @param value Path separator
     * @return This object, for chaining
     */
    @NotNull
    public ConfigurationOptions pathSeparator(char value) {
        this.pathSeparator = value;
        return this;
    }

    /**
     * Checks if the {@link de.varilx.configuration.Configuration} should copy values from its default
     * {@link de.varilx.configuration.Configuration} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link ConfigurationSection#contains(String)} will always
     * return the same value as {@link
     * ConfigurationSection#isSet(String)}. The default value is
     * false.
     *
     * @return Whether or not defaults are directly copied
     */
    public boolean copyDefaults() {
        return copyDefaults;
    }

    /**
     * Sets if the {@link de.varilx.configuration.Configuration} should copy values from its default
     * {@link Configuration} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link ConfigurationSection#contains(String)} will always
     * return the same value as {@link
     * ConfigurationSection#isSet(String)}. The default value is
     * false.
     *
     * @param value Whether or not defaults are directly copied
     * @return This object, for chaining
     */
    @NotNull
    public ConfigurationOptions copyDefaults(boolean value) {
        this.copyDefaults = value;
        return this;
    }
}
