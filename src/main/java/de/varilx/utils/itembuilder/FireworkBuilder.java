package de.varilx.utils.itembuilder;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class FireworkBuilder extends BaseItemBuilder<FireworkBuilder, FireworkMeta> {

    /**
     * Constructs a {@link FireworkBuilder} with a default {@link Material#FIREWORK_ROCKET}
     */
    public FireworkBuilder() {
        super(Material.FIREWORK_ROCKET);
    }

    /**
     * Constructs a new {@link FireworkBuilder} based on an {@link ItemStack}
     * @param itemStack the {@link ItemStack}
     */
    public FireworkBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (isNonValidFirework()) throw new IllegalArgumentException("ItemStack is no skull");
    }


    /**
     * Constructs a new {@link FireworkBuilder} based on a {@link Material}
     * @param material the {@link Material}
     */
    public FireworkBuilder(@NotNull Material material) {
        super(material);
        if (isNonValidFirework()) throw new IllegalArgumentException("ItemStack is no skull");
    }

    /**
     * Constructs a new {@link FireworkBuilder} based on a {@link Material} and an amount
     * @param material the {@link Material}
     * @param amount the amount for the new {@link ItemStack}
     */
    public FireworkBuilder(@NotNull Material material, int amount) {
        super(material, amount);
        if (isNonValidFirework()) throw new IllegalArgumentException("ItemStack is no skull");
    }

    /**
     * Constructs a new {@link FireworkBuilder} based on a {@link Material} and an amount and an {@link ItemMeta}
     * @param material the {@link Material}
     * @param amount the amount for the new {@link ItemStack}
     * @param meta the meta for the new {@link ItemMeta}
     */
    public FireworkBuilder(@NotNull Material material, int amount, @NotNull ItemMeta meta) {
        super(material, amount, meta);
        if (isNonValidFirework()) throw new IllegalArgumentException("ItemStack is no skull");
    }

    /**
     * Sets the {@link FireworkEffect}s via the {@link FireworkEffect.Builder}
     * @param builders the {@link FireworkEffect.Builder}s
     * @return this to make chain calls
     * @see FireworkEffect.Builder
     */
    @NotNull
    public FireworkBuilder effects(@NotNull FireworkEffect.Builder @NotNull ... builders) {
        for (FireworkEffect.Builder builder : builders) {
            effects(builder.build());
        }
        return this;
    }


    /**
     * Adds the {@link FireworkEffect}s to the {@link ItemStack}
     * @param effects the {@link FireworkEffect}s
     * @return this to make chain calls
     * @see #effects(FireworkEffect.Builder...)
     * @see FireworkMeta#addEffects(FireworkEffect...)
     */
    @NotNull
    public FireworkBuilder effects(@NotNull FireworkEffect@NotNull ... effects) {
        return this.effects(Arrays.asList(effects));
    }

    /**
     * Adds the {@link FireworkEffect}s to the {@link ItemStack}
     * @param effects the {@link FireworkEffect}s
     * @return this to make chain calls
     * @see #effects(FireworkEffect.Builder...)
     * @see FireworkMeta#addEffects(FireworkEffect...)
     */
    @NotNull
    public FireworkBuilder effects(@NotNull List<FireworkEffect> effects) {
        return editMeta(fireworkMeta -> fireworkMeta.addEffects(effects));
    }

    /**
     * Sets the power of the {@link ItemStack}
     * @param power the power (0-255)
     * @return this to make chain calls
     * @see FireworkMeta#setPower(int)
     */
    @NotNull
    public FireworkBuilder power(int power) {
        return editMeta(fireworkMeta -> fireworkMeta.setPower(power));
    }

    private boolean isNonValidFirework() {
        return !List.of(Material.FIREWORK_ROCKET).contains(material());
    }

}
