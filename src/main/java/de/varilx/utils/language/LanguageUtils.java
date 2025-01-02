package de.varilx.utils.language;

import de.varilx.BaseAPI;
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
        @Nullable String raw = BaseAPI.get().getCurrentLanguageConfiguration().getString(path);
        if (raw == null) {
            BaseAPI.get().getLogger().warning(path + " was not found in lang/" + BaseAPI.get().getLanguage() + ".yml");
            return Component.text("Path: " + path + " not found!");
        }
        return MiniMessage.miniMessage().deserialize("<!i><gray>" + raw, args);
    }

    public List<Component> getMessageList(String path, TagResolver... args) {
        List<TagResolver> resolvers = initializeLanguageAndResolvers(args);

        List<Component> components = new ArrayList<>();
        BaseAPI.get().getCurrentLanguageConfiguration().getStringList(path).forEach(line ->
                components.add(MiniMessage.miniMessage().deserialize("<gray><!i>" + line, resolvers.toArray(TagResolver[]::new)))
        );

        return components;
    }

    public String getMessageString(String path) {
        String language = Optional.ofNullable(BaseAPI.get().getConfiguration().getString("language")).orElse("en");
        @Nullable String raw = BaseAPI.get().getCurrentLanguageConfiguration().getString(path);
        if (raw == null) {
            BaseAPI.get().getLogger().warning(path + " was not found in lang/" + language + ".yml");
            return "Path: " + path + " not found!";
        }
        return raw;
    }



    private List<TagResolver> initializeLanguageAndResolvers(TagResolver... args) {

        List<TagResolver> baseResolvers = new ArrayList<>();
        @Nullable String prefix = BaseAPI.get().getCurrentLanguageConfiguration().getString("prefix");
        if (prefix != null) baseResolvers.add(Placeholder.parsed("prefix", prefix));
        baseResolvers.addAll(Arrays.stream(args).toList());

        return baseResolvers;
    }

}
