package de.varilx.inventory;

import de.varilx.inventory.item.ClickableItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameInventory {

    final Player holder;
    final String[] pattern;
    final Map<Character, ClickableItem> items;
    final List<ClickableItem> paginatedItems;
    final Map<Integer, ClickableItem> placedItems;
    @Getter
    Inventory inventory;
    final char PAGINATION_CHAR;
    final int ITEMS_PER_PAGE;

    int currentPage ;

    // Non-paginated constructor
    public GameInventory(Player holder, int inventorySize, Component inventoryName, String[] pattern, Map<Character, ClickableItem> items) {
        this(holder, inventorySize, inventoryName, pattern, items, new ArrayList<>(), ' ', 0); // Empty list for paginatedItems
    }

    // Paginated constructor
    public GameInventory(Player holder, int inventorySize, Component inventoryName, String[] pattern, Map<Character, ClickableItem> items, List<ClickableItem> paginatedItems, char paginationChar, int itemsPerPage) {
        this.holder = holder;
        this.pattern = pattern;
        this.items = items;
        this.paginatedItems = paginatedItems;
        this.placedItems = new HashMap<>();
        this.inventory = Bukkit.createInventory(holder, inventorySize, inventoryName);
        this.currentPage = 0;
        this.PAGINATION_CHAR = paginationChar;
        this.ITEMS_PER_PAGE = itemsPerPage;
    }

    public void open() {
        inventory.clear();
        applyPattern();
        holder.openInventory(inventory);
    }

    public void nextPage() {
        if (!paginatedItems.isEmpty()) {
            int totalPages = (int) Math.ceil((double) paginatedItems.size() / ITEMS_PER_PAGE);
            if (currentPage < totalPages - 1) {
                currentPage++;
                open();
            }
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            open();
        }
    }

    public void addItem(Character character, ClickableItem item) {
        items.put(character, item);
    }

    public void handleClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(inventory)) {
            int slot = event.getSlot();
            ClickableItem clickedItem = placedItems.get(slot);
            if (clickedItem != null) {
                clickedItem.handleClick(event);
            }
        }
    }

    private void applyPattern() {
        placedItems.clear();
        int startItem = currentPage * ITEMS_PER_PAGE;
        int endItem = Math.min(startItem + ITEMS_PER_PAGE, paginatedItems.size());
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length(); j++) {
                if (pattern[i].charAt(j) == PAGINATION_CHAR && !paginatedItems.isEmpty()) { // If equal to P, place the paginated item
                    if (startItem < endItem) {
                        placeItem(i, j, paginatedItems.get(startItem));
                        startItem++;
                    }
                } else {
                    ClickableItem item = items.get(pattern[i].charAt(j));
                    if (item != null) {
                        placeItem(i, j, item);
                    }
                }
            }
        }
    }

    private void placeItem(int i, int j, ClickableItem item) {
        int slot = i * 9 + j;
        inventory.setItem(slot, item.getItemStack());
        placedItems.put(slot, item);
    }

}
