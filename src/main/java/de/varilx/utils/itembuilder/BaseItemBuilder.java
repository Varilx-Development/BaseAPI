package de.varilx.utils.itembuilder;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This is the Baseclass of all {@link de.varilx.utils.itembuilder ItemBuilder}s
 * To use this Class initialize one of the predefined {@link de.varilx.utils.itembuilder ItemBuilder}s
 * @param <B> The <b>B</b>uilder wich the implementation returns
 * @param <M> The <b>M</b>eta which {@link ItemMeta} the implementation uses
 */
@SuppressWarnings("unchecked")
public abstract class BaseItemBuilder<B extends BaseItemBuilder<B, M>, M extends ItemMeta> {

    private final ItemStack itemStack;


    /**
     * Constructs a ItemBuilder based on an itemstack
     * @param itemStack the itemstack
     */
    protected BaseItemBuilder(@NotNull ItemStack itemStack) {
        checkNotNull(itemStack, "itemStack");
        this.itemStack = itemStack;
    }

    /**
     * Constructs an ItemBuilder based on a material
     * @param material the material
     */
    protected BaseItemBuilder(@NotNull Material material) {
        checkNotNull(material, "material");
        this.itemStack = new ItemStack(material);
    }

    /**
     * Constructs an ItemBuilder based on a material and amount
     * @param material the material
     * @param amount the amount
     */
    protected BaseItemBuilder(@NotNull Material material, @NotNull int amount) {
        checkNotNull(material, "material");
        this.itemStack = new ItemStack(material, amount);
    }

    /**
     * Constructs an ItemBuilder based on a material, amount and item meta
     * @param material the material
     * @param amount the amount
     * @param meta the meta
     */
    protected BaseItemBuilder(@NotNull Material material, @NotNull int amount, @NotNull ItemMeta meta) {
        checkNotNull(material, "material");
        checkNotNull(material, "meta");
        this.itemStack = new ItemStack(material, amount);
        this.itemStack.setItemMeta(meta);
    }

    /**
     * Sets the custom modeldata for the item
     * @param customModelData the data
     * @return this for chain calls
     */
    @NotNull
    public B setCustomModelData(int customModelData) {
        return this.editMeta(m -> m.setCustomModelData(customModelData));
    }


    /**
     * Retrieves the translated display name of the item.
     *
     * @return the translated display name as a {@link Component}
     */
    @NotNull
    public Component translatedName() {
        return get(ItemMeta::displayName);
    }




    /**
     * Removes the last lore line
     * @return this to make chain calls
     */
    @NotNull
    public B removeLastLore() {
        return this.editMeta(meta -> {
            List<Component> lore = meta.lore();
            if (lore == null) lore = new ArrayList<>();
            lore.removeLast();
            meta.lore(lore);
        });
    }


    /**
     * Sets the displayname of the itemstack
     * @param displayname the displayname as a {@link Component}
     * @return this to make chain calls
     * @see ItemMeta#displayName(Component)
     * @see #name(Component)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B displayname(@NotNull Component displayname) {
        checkNotNull(displayname, "displayname");
        return name(displayname);
    }

    /**
     * Gets the current displayname as a {@link Component}
     * @return the displayname
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public Component displayname() {
        return name();
    }

    /**
     * Sets the displayname of the {@link ItemStack}
     * @param name the displayname as a {@link Component}
     * @return this to make chain calls
     * @see ItemMeta#displayName(Component)
     * @see #displayname(Component)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B name(@NotNull Component name) {
        checkNotNull(name, "name");
        return this.editMeta(meta -> meta.displayName(name));
    }

    /**
     * Sets the displayname of the {@link ItemStack}
     * @param name the displayname as a {@link Supplier<Component>}
     * @return this to make chain calls
     * @see ItemMeta#displayName(Component)
     * @see #displayname(Component)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B name(@NotNull Supplier<Component> name) {
        checkNotNull(name, "name");
        return this.editMeta(meta -> meta.displayName(name.get()));
    }

    /**
     * Gets the name of the {@link ItemStack}
     * @return the name of the {@link ItemStack} as a {@link Component}
     * @see ItemMeta#displayName()
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public Component name() {
        return get(ItemMeta::displayName);
    }

    /**
     * Replaces and sets the new lore as {@link Component}s
     * @param lore the lore
     * @return this to make chain calls
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B lore(@NotNull Component... lore) {
        checkNotNull(lore, "lore");
        return this.lore(Arrays.asList(lore));
    }

    /**
     * Replaces and sets the new lore as {@link Component}s
     * @param lore the lore
     * @return this to make chain calls
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B lore(@NotNull List<Component> lore) {
        checkNotNull(lore, "lore");
        return editMeta(meta -> {
            if (meta.lore() == null) meta.lore(new ArrayList<>());
            meta.lore(lore);
        });
    }

    /**
     * Replaces and sets the new lore as {@link Component}s
     * @param lore the lore
     * @return this to make chain calls
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B lore(@NotNull Supplier<List<Component>> lore) {
        checkNotNull(lore, "lore");
        return lore(lore.get());
    }

    /**
     * Replaces a lore if the given {@link Supplier} is true
     * @param supplier the {@link Supplier}
     * @param toAdd the lore
     * @return this to make chain calls
     * @see #lore(Supplier)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B loreIf(@NotNull Supplier<Boolean> supplier, @NotNull Supplier<List<Component>> toAdd) {
        checkNotNull(supplier, "supplier");
        checkNotNull(toAdd, "lore");
        if (!supplier.get()) return (B) this;
        return lore(toAdd);
    }

    /**
     * Adds a lore if the given {@link Supplier} is true
     * @param supplier the {@link Supplier}
     * @param toAdd the lore
     * @return this to make chain calls
     * @see #lore(List)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B loreIf(@NotNull Supplier<Boolean> supplier, @NotNull List<Component> toAdd) {
        checkNotNull(supplier, "supplier");
        checkNotNull(toAdd, "lore");
        if (!supplier.get()) return (B) this;
        return lore(toAdd);
    }

    /**
     * Adds a lore if the given {@link Supplier} is true
     * @param supplier the {@link Supplier}
     * @param toAdd the lore
     * @return this to make chain calls
     * @see #lore(Component...)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B loreIf(@NotNull Supplier<Boolean> supplier, @NotNull Component... toAdd) {
        checkNotNull(supplier, "supplier");
        checkNotNull(toAdd, "lore");
        if (!supplier.get()) return (B) this;
        return lore(toAdd);
    }

    /**
     * Adds a lore if the given {@link Supplier} is true
     * @param supplier the {@link Supplier}
     * @param toAdd the lore
     * @return this to make chain calls
     * @see #addLore(Supplier)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B addLoreIf(@NotNull Supplier<Boolean> supplier, @NotNull Supplier<List<Component>> toAdd) {
        checkNotNull(supplier, "supplier");
        checkNotNull(toAdd, "lore");
        if (!supplier.get()) return (B) this;
        return addLore(toAdd);
    }

    /**
     * Adds a lore if the given {@link Supplier} is true
     * @param supplier the {@link Supplier}
     * @param toAdd the lore
     * @return this to make chain calls
     * @see #addLore(List)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B addLoreIf(@NotNull Supplier<Boolean> supplier, @NotNull List<Component> toAdd) {
        checkNotNull(supplier, "supplier");
        checkNotNull(toAdd, "lore");
        if (!supplier.get()) return (B) this;
        return addLore(toAdd);
    }

    /**
     * Adds a lore if the given {@link Supplier} is true
     * @param supplier the {@link Supplier}
     * @param toAdd the lore
     * @return this to make chain calls
     * @see #addLore(Component...)
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B addLoreIf(@NotNull Supplier<Boolean> supplier, @NotNull Component... toAdd) {
        checkNotNull(supplier, "supplier");
        checkNotNull(toAdd, "lore");
        if (!supplier.get()) return (B) this;
        return addLore(toAdd);
    }

    /**
     * Adds the given lore lines to the current {@link ItemStack}
     * @param lore the lore to add
     * @return this to make chain calls
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B addLore(@NotNull Supplier<List<Component>> lore) {
        checkNotNull(lore, "lore");
        return addLore(lore.get());
    }

    /**
     * Adds the given lore lines to the current {@link ItemStack}
     * @param toAdd the lore to add
     * @return this to make chain calls
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B addLore(@NotNull List<Component> toAdd) {
        checkNotNull(toAdd, "lore");
        return editMeta(m -> {
            List<Component> lore = m.lore();
            if (lore == null) lore = new ArrayList<>();
            lore.addAll(toAdd);
            m.lore(lore);
        });
    }

    /**
     * Adds the given lore lines to the current {@link ItemStack}
     * @param toAdd the lore to add
     * @return this to make chain calls
     */
    @ApiStatus.Obsolete(since = "1.12.4")
    @NotNull
    public B addLore(@NotNull Component... toAdd) {
        checkNotNull(toAdd, "lore");
        return addLore(Arrays.asList(toAdd));
    }


    /**
     * Removes the given {@link ItemFlag}s from the {@link ItemStack}
     * @param flags the flags to remove
     * @return this to make chain calls
     */
    @NotNull
    public B removeFlags(@NotNull ItemFlag... flags) {
        checkNotNull(flags, "flags");
        return editMeta(m -> {
            m.removeItemFlags(flags);
        });
    }

    /**
     * Removes the given {@link ItemFlag}s from the {@link ItemStack}
     * @param flags the flags to remove
     * @return this to make chain calls
     */
    @NotNull
    public B removeFlags(@NotNull List<ItemFlag> flags) {
        checkNotNull(flags, "flags");
        return removeFlags(flags.toArray(new ItemFlag[]{}));
    }

    /**
     * Adds the given {@link ItemFlag}s to the current {@link ItemStack}
     * @param flags the {@link ItemFlag}s
     * @return this to make chain calls
     */
    @NotNull
    public B flags(@NotNull ItemFlag... flags) {
        checkNotNull(flags, "flags");
        return editMeta(m -> {
            m.addItemFlags(flags);
        });
    }

    /**
     * Adds the given {@link ItemFlag}s to the current {@link ItemStack}
     * @param flags the {@link ItemFlag}s
     * @return this to make chain calls
     */
    @NotNull
    public B flags(@NotNull List<ItemFlag> flags) {
        checkNotNull(flags, "flags");
        return flags(flags.toArray(new ItemFlag[]{}));
    }

    /**
     * Clears <b>all</b> {@link ItemFlag}s from the {@link ItemStack}
     * @return this to make chain calls
     */
    @NotNull
    public B clearFlags() {
        return editMeta(m -> {
            m.removeItemFlags(ItemFlag.values());
        });
    }

    /**
     * Lets the {@link ItemStack} glow (it gives it invisible {@link Enchantment#MENDING})
     * @return this to make chain calls
     * @see #glow(boolean)
     */
    @NotNull
    public B glow() {
        return glow(true);
    }

    /**
     * Lets the {@link ItemStack} glow (it gives it invisible {@link Enchantment#MENDING})
     * @return this to make chain calls
     * @see #glow()
     */
    @NotNull
    public B glow(boolean glow) {
        return editMeta(m -> {
            if (glow) {
                m.addEnchant(Enchantment.MENDING, 1, false);
                m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                return;
            }
            m.removeEnchant(Enchantment.MENDING);
            m.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
    }

    /**
     * Enchants to current {@link ItemStack} with the given {@link Enchantment}
     * with level "1"
     * @param enchantment the {@link Enchantment} to add
     * @return this to make chain calls
     * @see Enchantment
     * @see ItemMeta#addEnchant(Enchantment, int, boolean)
     */
    @NotNull
    public B enchant(@NotNull Enchantment enchantment) {
        checkNotNull(enchantment, "enchantment");
        return editMeta(m -> m.addEnchant(enchantment, 1, false));
    }

    /**
     * Enchants to current {@link ItemStack} with the given {@link Enchantment}
     * with the given level
     * @param enchantment the {@link Enchantment} to add
     * @param level the level which the {@link Enchantment} should be
     * @return this to make chain calls
     * @see Enchantment
     * @see ItemMeta#addEnchant(Enchantment, int, boolean)
     */
    @NotNull
    public B enchant(@NotNull Enchantment enchantment, int level) {
        checkNotNull(enchantment, "enchantment");
        return editMeta(m -> m.addEnchant(enchantment, level, false));
    }

    /**
     * Enchants to current {@link ItemStack} with the given {@link Enchantment}
     * with the given level
     * @param enchantment the {@link Enchantment} to add
     * @param level the level which the {@link Enchantment} should be
     * @param restrictions whether the {@link Enchantment} should be restricted
     * @return this to make chain calls
     * @see Enchantment
     * @see ItemMeta#addEnchant(Enchantment, int, boolean)
     */
    @NotNull
    public B enchant(@NotNull Enchantment enchantment, int level, boolean restrictions) {
        checkNotNull(enchantment, "enchantment");
        return editMeta(m -> m.addEnchant(enchantment, level, restrictions));
    }

    /**
     * Remove <b>all</b> {@link Enchantment}s from the current {@link ItemStack}
     * @return this to make chain calls
     * @see #disenchant(Enchantment)
     */
    @NotNull
    public B disenchant() {
        return getMeta(m -> m.getEnchants().forEach((enchantment, integer) -> disenchant(enchantment)));
    }

    /**
     * Removes the given {@link Enchantment} from the current {@link ItemStack}
     * @param type the {@link Enchantment} to remove
     * @return this to make chain calls
     */
    @NotNull
    public B disenchant(@NotNull Enchantment type) {
        checkNotNull(type, "enchantment");
        return editMeta(m -> m.removeEnchant(type));
    }

    /**
     * Returns whether the item is unbreakable
     * @return True -> item is unbreakable, false -> its not
     * @see ItemMeta#setUnbreakable(boolean)
     */
    @NotNull
    public boolean isUnbreakable() {
        return get(ItemMeta::isUnbreakable);
    }

    /**
     * Makes the item unbreakable
     * @return this to make chain calls
     */
    @NotNull
    public B unbreakable() {
        return unbreakable(true);
    }

    /**
     * Sets the unbreakable value to the given value
     * @param unbreakable whether the item should be unbreakable or not
     * @return this to make chain calls
     */
    @NotNull
    public B unbreakable(boolean unbreakable) {
        return editMeta(m -> m.setUnbreakable(unbreakable));
    }

    /**
     * Sets the amount of the {@link ItemStack}
     * @param amount the amount
     * @return this to make chain calls
     */
    @NotNull
    public B amount(int amount) {
        this.itemStack.setAmount(amount);
        return (B) this;
    }

    /**
     * You can use this method to make a call to the itembuilder
     * if a condition is true
     * @param bool the {@link Predicate} to supply the condition
     * @param action the action
     * @return this t make chain calls
     * @see #loreIf(Supplier, Component...)
     */
    @NotNull
    public B doIf(@NotNull Supplier<Boolean> bool, @NotNull Consumer<B> action) {
        checkNotNull(bool, "supplier");
        checkNotNull(action, "action");
        if (!bool.get()) return (B) this;
        action.accept((B) this);
        return (B) this;
    }

    /**
     * Gets the {@link Material} of the current {@link ItemStack}
     * @return the {@link Material}
     */
    @NotNull
    public Material material() {
        return this.itemStack.getType();
    }


    /**
     * Edits the meta
     * @param consumer the consumer to edit
     * @return this to make chain calls
     */
    @NotNull
    protected B editMeta(@NotNull Consumer<M> consumer) {
        checkNotNull(consumer, "edit item meta consumer");
        M meta = meta();
        consumer.accept(meta);
        this.itemStack.setItemMeta(meta);
        return (B) this;
    }

    /**
     * The same as {@code BaseItemBuilder#editMeta} but it doesnt save it
     * @param consumer the consumer to get
     * @return this to make chain calls
     * @see #editMeta(Consumer)
     */
    @NotNull
    protected B getMeta(@NotNull Consumer<M> consumer) {
        checkNotNull(consumer, "get item meta consumer");
        consumer.accept(meta());
        return (B) this;
    }

    private <T> T get(@NotNull Function<M, T> function) {
        checkNotNull(function, "get meta func");
        return function.apply(meta());
    }

    private M meta() {
        return (M) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
    }

    @NotNull
    public ItemStack build() {
        return this.itemStack;
    }

    @Override
    @NotNull
    public ItemBuilder clone() {
        return new ItemBuilder(this.itemStack.clone());
    }


}
