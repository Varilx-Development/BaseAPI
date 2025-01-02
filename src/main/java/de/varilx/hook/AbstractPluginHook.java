package de.varilx.hook;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.Plugin;

/**
 * Project: base-api
 * Package: de.varilx.hook
 * <p>
 * Author: ShadowDev1929
 * Created on: 01.01.2025
 */

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractPluginHook<P> {

    Plugin plugin;
    String hookPluginName;
    P hookedPlugin;
    boolean enabled;

    public AbstractPluginHook(Plugin plugin, String hookPluginName) {
        this.plugin = plugin;
        this.hookPluginName = hookPluginName;
        this.enabled = false;
    }

    public abstract void check();

}
