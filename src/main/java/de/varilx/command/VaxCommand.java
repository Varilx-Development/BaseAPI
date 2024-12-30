package de.varilx.command;


import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

public abstract class VaxCommand extends Command {

    public VaxCommand(@NotNull String name) {
        super(name);
    }

}
