package de.varilx.inventory.builder;

import de.varilx.BaseAPI;
import de.varilx.inventory.GameInventory;
import de.varilx.inventory.exception.GameInventoryBuildException;
import de.varilx.inventory.item.ClickableItem;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameInventoryBuilder {

    String[] pattern;
    Map<Character, ClickableItem> items;
    Player holder;
    Component inventoryName;
    int inventorySize;
    boolean pagination;
    Character patternCharacter;
    int itemsPerPage;
    List<ClickableItem> pagingItems;

    public GameInventoryBuilder() {
        this.items = new HashMap<>();
    }

    public GameInventoryBuilder pattern(String[] pattern) {
        this.pattern = pattern;
        return this;
    }

    public GameInventoryBuilder holder(Player holder) {
        this.holder = holder;
        return this;
    }

    public GameInventoryBuilder inventoryName(Component inventoryName) {
        this.inventoryName = inventoryName;
        return this;
    }

    public GameInventoryBuilder size(int size) {
        this.inventorySize = size;
        return this;
    }

    public GameInventoryBuilder addItem(Character character, ClickableItem clickableItem) {
        items.put(character, clickableItem);
        return this;
    }

    public GameInventoryBuilder enablePagination(Character patternCharacter, int itemsPerPage, List<ClickableItem> pagingItems) {
        this.pagination = true;
        this.patternCharacter = patternCharacter;
        this.itemsPerPage = itemsPerPage;
        this.pagingItems = pagingItems;
        return this;
    }

    public GameInventory build() {
        if(pattern == null)
            throw new RuntimeException(new GameInventoryBuildException("Missing Pattern!"));
        if(holder == null)
            throw new RuntimeException(new GameInventoryBuildException("Missing Inventory Holder!"));
        if(inventoryName == null)
            throw new RuntimeException(new GameInventoryBuildException("Missing Inventory name!"));

        GameInventory shadowInventory = null;

        if(pagination) {
            if(pagingItems == null)
                throw new RuntimeException(new GameInventoryBuildException("Missing items to page!"));
            if(patternCharacter == null)
                throw new RuntimeException(new GameInventoryBuildException("Missing paging char!"));
            shadowInventory = new GameInventory(holder, (inventorySize == 0 ? 9 : inventorySize), inventoryName, pattern, items, pagingItems, patternCharacter, itemsPerPage);
        } else {
            shadowInventory = new GameInventory(holder, (inventorySize == 0 ? 9 : inventorySize), inventoryName, pattern, items);
        }
        BaseAPI.getGameInventoryController().registerInventory(shadowInventory);
        return shadowInventory;
    }

}
