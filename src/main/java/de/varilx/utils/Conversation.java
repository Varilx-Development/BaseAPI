package de.varilx.utils;


import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conversation implements Listener {

    static Plugin plugin;
    static final List<Conversation> conversations = new ArrayList<>();

    final Player player;
    final Component startPrompt;

    final Set<ConvoListener> listeners = new HashSet<>();
    final Set<ConvoCancelListener> cancelListeners = new HashSet<>();

    final long timeout;
    final long start;

    boolean running = true;

    public Conversation(Plugin plugin, Player player, Component startPrompt, TimeUnit unit, long timeout) {
        this(plugin, player, startPrompt, unit.toMillis(timeout));
    }

    public Conversation(Plugin plugin, Player player, Component startPrompt, long timeout) {
        if (Conversation.plugin == null) {
            Conversation.plugin = plugin;
            Bukkit.getPluginManager().registerEvents(this, Conversation.plugin);
        }
        Conversation.conversations.add(this);
        this.player = player;
        this.startPrompt = startPrompt;
        this.timeout = timeout;
        this.start = System.currentTimeMillis();
        player.sendMessage(startPrompt);
        player.closeInventory();

        Bukkit.getScheduler().runTaskLater(Conversation.plugin, () -> {
            if (!running) return;
            cancel(false);
        }, timeout * 20);
    }

    public void cancel(boolean succeded) {
        running = false;
        for (ConvoCancelListener cancelListener : new ArrayList<>(cancelListeners)) {
            cancelListener.cancel(player, succeded);
        }
        Conversation.conversations.remove(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncChat(AsyncChatEvent event) {
        for (Conversation conversation : new ArrayList<>(Conversation.conversations)) {
            if (!conversation.running) continue;
            if (!conversation.player.getUniqueId().equals(event.getPlayer().getUniqueId())) continue;
            event.viewers().clear();

            Bukkit.getScheduler().runTask(plugin, () -> {
                conversation.listeners.stream()
                        .filter(listener -> listener.onMessage(event.getPlayer(), event.message(), PlainTextComponentSerializer.plainText().serialize(event.message())))
                        .findFirst()
                        .ifPresent(listener -> cancel(true));
            });
        }
    }


    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        for (Conversation conversation : new ArrayList<>(Conversation.conversations)) {
            if (!conversation.running) return;
            if (!conversation.player.getUniqueId().equals(event.getPlayer().getUniqueId())) return;
            conversation.cancel(false);
        }
    }

    public void addListener(ConvoListener listener) {
        listeners.add(listener);
    }

    public void addCancelListener(ConvoCancelListener listener) {
        cancelListeners.add(listener);
    }

    @FunctionalInterface
    public interface ConvoCancelListener {

        /**
         * Is called when the conversation is canceled
         * @param player the player
         * @param succeeded if the conversation was canceled because of a successful message
         */
        void cancel(Player player, boolean succeeded);

    }

    @FunctionalInterface
    public interface ConvoListener {

        /**
         * @return true -> cancel, false -> nothing
         */
        boolean onMessage(Player player, Component message, String plain);


    }

}
