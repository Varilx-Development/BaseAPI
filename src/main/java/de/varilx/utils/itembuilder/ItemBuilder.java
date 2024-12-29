package de.varilx.utils.itembuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder extends BaseItemBuilder<@NotNull ItemBuilder, @NotNull ItemMeta> {

    /**
     * Constructs a new {@link ItemBuilder} based on an {@link ItemStack}
     * @param itemStack the {@link ItemStack}
     */
    public ItemBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * Constructs a new {@link ItemBuilder} based on a {@link Material}
     * @param material the {@link Material}
     */
    public ItemBuilder(@NotNull Material material) {
        super(material);
    }

    /**
     * Constructs a new {@link ItemBuilder} based on a {@link Material} and an amount
     * @param material the {@link Material}
     * @param amount the amount for the new {@link ItemStack}
     */
    public ItemBuilder(@NotNull Material material, int amount) {
        super(material, amount);
    }

    /**
     * Constructs a new {@link ItemBuilder} based on a {@link Material} and an amount and an {@link ItemMeta}
     * @param material the {@link Material}
     * @param amount the amount for the new {@link ItemStack}
     * @param meta the meta for the new {@link ItemMeta}
     */
    public ItemBuilder(@NotNull Material material, int amount, @NotNull ItemMeta meta) {
        super(material, amount, meta);
    }


}
