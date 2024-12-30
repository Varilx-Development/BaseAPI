package de.varilx.utils.language;

import de.varilx.BaseAPI;
import de.varilx.config.Configuration;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class LanguageUtils {


    public Component getMessage(String path, TagResolver... args) {
        String language = Optional.ofNullable(BaseAPI.getBaseAPI().getConfiguration().getConfig().getString("language")).orElse("en");
        Configuration langConfig = BaseAPI.getBaseAPI().getLanguageConfigurations().get(language);

        List<TagResolver> baseResolvers = new ArrayList<>();

        @Nullable String prefix = langConfig.getConfig().getString("prefix");
        if (prefix != null) baseResolvers.add(Placeholder.parsed("prefix", prefix));
        baseResolvers.addAll(Arrays.stream(args).toList());

        return getMessage(language, path, baseResolvers.toArray(TagResolver[]::new));
    }

    public String getMessageString(String path) {
        String language = Optional.ofNullable(BaseAPI.getBaseAPI().getConfiguration().getConfig().getString("language")).orElse("en");
        Configuration langConfig = BaseAPI.getBaseAPI().getLanguageConfigurations().get(language);
        @Nullable String raw = langConfig.getConfig().getString(path);
        if (raw == null) {
            BaseAPI.getBaseAPI().getPlugin().getLogger().warning(path + " was not found in lang/" + language + ".yml");
            return "Path: " + path + " not found!";
        }
        return raw;
    }

    private Component getMessage(String lang, String path, TagResolver... args) {
        Configuration langConfig = BaseAPI.getBaseAPI().getLanguageConfigurations().get(lang);
        @Nullable String raw = langConfig.getConfig().getString(path);
        if (raw == null) {
            BaseAPI.getBaseAPI().getPlugin().getLogger().warning(path + " was not found in lang/" + lang + ".yml");
            return Component.text("Path: " + path + " not found!");
        }
        return MiniMessage.miniMessage().deserialize(raw, args);
    }

}
