package de.varilx.configuration.file;

import com.google.common.base.Preconditions;
import de.varilx.configuration.file.FileConfigurationOptions;
import de.varilx.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Various settings for controlling the input and output of a {@link
 * de.varilx.configuration.file.YamlConfiguration}
 */
public class YamlConfigurationOptions extends FileConfigurationOptions {
    private int indent = 2;
    private int width = 80;
    private int codePointLimit = Integer.MAX_VALUE; // Paper - use upstream's default from YamlConfiguration

    protected YamlConfigurationOptions(@NotNull de.varilx.configuration.file.YamlConfiguration configuration) {
        super(configuration);
    }

    @NotNull
    @Override
    public de.varilx.configuration.file.YamlConfiguration configuration() {
        return (YamlConfiguration) super.configuration();
    }

    @NotNull
    @Override
    public YamlConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions setHeader(@Nullable List<String> value) {
        super.setHeader(value);
        return this;
    }

    @NotNull
    @Override
    @Deprecated(since = "1.18.1")
    public YamlConfigurationOptions header(@Nullable String value) {
        super.header(value);
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions setFooter(@Nullable List<String> value) {
        super.setFooter(value);
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions parseComments(boolean value) {
        super.parseComments(value);
        return this;
    }

    @NotNull
    @Override
    @Deprecated(since = "1.18.1")
    public YamlConfigurationOptions copyHeader(boolean value) {
        super.copyHeader(value);
        return this;
    }

    /**
     * Gets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @return How much to indent by
     */
    public int indent() {
        return indent;
    }

    /**
     * Sets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @param value New indent
     * @return This object, for chaining
     */
    @NotNull
    public YamlConfigurationOptions indent(int value) {
        Preconditions.checkArgument(value >= 2, "Indent must be at least 2 characters");
        Preconditions.checkArgument(value <= 9, "Indent cannot be greater than 9 characters");

        this.indent = value;
        return this;
    }

    /**
     * Gets how long a line can be, before it gets split.
     *
     * @return How the max line width
     */
    public int width() {
        return width;
    }

    /**
     * Sets how long a line can be, before it gets split.
     *
     * @param value New width
     * @return This object, for chaining
     */
    @NotNull
    public YamlConfigurationOptions width(int value) {
        this.width = value;
        return this;
    }

    // Paper start
    /**
     * Gets the maximum code point limit, that being, the maximum length of the document
     * in which the loader will read
     *
     * @return The current value
     */
    public int codePointLimit() {
        return codePointLimit;
    }

    /**
     * Sets the maximum code point limit, that being, the maximum length of the document
     * in which the loader will read
     *
     * @param codePointLimit new codepoint limit
     * @return This object, for chaining
     */
    @NotNull
    public YamlConfigurationOptions codePointLimit(int codePointLimit) {
        this.codePointLimit = codePointLimit;
        return this;
    }
    // Paper end
}
