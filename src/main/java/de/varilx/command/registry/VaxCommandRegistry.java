package de.varilx.command.registry;


import de.varilx.BaseAPI;
import de.varilx.command.VaxCommand;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VaxCommandRegistry {

    Plugin plugin;
    Map<String, VaxCommand> commands;

    public VaxCommandRegistry() {
        this.plugin = BaseAPI.getBaseAPI().getPlugin();
        this.commands = new ConcurrentHashMap<>();
    }

    public void registerCommand(VaxCommand command) {
        commands.put(command.getName(), command);
    }

    public void unregisterCommand(String command) {
        CommandMap commandMap = Bukkit.getServer().getCommandMap();

        commandMap.getKnownCommands().remove(command);
        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }

    @SneakyThrows
    private void registerServerCommand(VaxCommand vaxCommand) {
        CommandMap commandMap = Bukkit.getServer().getCommandMap();
        commandMap.register("", vaxCommand);
        commandMap.getKnownCommands().put(vaxCommand.getName(), vaxCommand);
        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }


}
