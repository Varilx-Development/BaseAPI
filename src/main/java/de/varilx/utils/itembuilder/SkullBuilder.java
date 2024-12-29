package de.varilx.utils.itembuilder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

public class SkullBuilder extends BaseItemBuilder<@NotNull SkullBuilder, @NotNull SkullMeta> {

    /**
     * Constructs a {@link SkullBuilder} with a default {@link Material#PLAYER_HEAD}
     */
    public SkullBuilder() {
        super(Material.PLAYER_HEAD);
    }

    /**
     * Constructs a new {@link SkullBuilder} based on an {@link ItemStack}
     * @param itemStack the {@link ItemStack}
     */
    public SkullBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (isNonValidSkull()) throw new IllegalArgumentException("ItemStack is no skull");
    }

    /**
     * Constructs a new {@link SkullBuilder} based on a {@link Material}
     * @param material the {@link Material}
     */
    public SkullBuilder(@NotNull Material material) {
        super(material);
        if (isNonValidSkull()) throw new IllegalArgumentException("ItemStack is no skull");
    }

    /**
     * Constructs a new {@link SkullBuilder} based on a {@link Material} and an amount
     * @param material the {@link Material}
     * @param amount the amount for the new {@link ItemStack}
     */
    public SkullBuilder(@NotNull Material material, int amount) {
        super(material, amount);
        if (isNonValidSkull()) throw new IllegalArgumentException("ItemStack is no skull");
    }

    /**
     * Constructs a new {@link SkullBuilder} based on a {@link Material} and an amount and an {@link ItemMeta}
     * @param material the {@link Material}
     * @param amount the amount for the new {@link ItemStack}
     * @param meta the meta for the new {@link ItemMeta}
     */
    public SkullBuilder(@NotNull Material material, int amount, @NotNull ItemMeta meta) {
        super(material, amount, meta);
        if (isNonValidSkull()) throw new IllegalArgumentException("ItemStack is no skull");
    }

    /**
     * Sets the {@link PlayerProfile} for the {@link ItemStack}
     * @param profile the new {@link PlayerProfile}
     * @return this to make chain call
     * @see SkullMeta#setPlayerProfile(PlayerProfile)
     */
    @NotNull
    public SkullBuilder playerProfile(@NotNull PlayerProfile profile) {
        Preconditions.checkNotNull(profile, "profile");
        return editMeta(skullMeta -> skullMeta.setPlayerProfile(profile));
    }

    /**
     * Sets the owning {@link OfflinePlayer} of the skull
     * @param player the {@link OfflinePlayer}
     * @return this to make chain calls
     * @see SkullMeta#setOwningPlayer(OfflinePlayer)
     */
    @NotNull
    public SkullBuilder owningPlayer(@NotNull OfflinePlayer player) {
        checkNotNull(player, "player");
        return editMeta(meta -> meta.setOwningPlayer(player));
    }

    /**
     * Sets the owning Player Name of the skull
     * @param player the name
     * @return this to make chain calls
     * @see SkullMeta#setOwner(String)
     */
    @Deprecated
    @NotNull
    public SkullBuilder setOwner(@NotNull String player) {
        checkNotNull(player, "player");
        return editMeta(meta -> meta.setOwner(player));
    }

    @NotNull
    public SkullBuilder setSkullTextures(@NotNull UUID uuid) {
        return editMeta(skullMeta -> {
            try {
                skullMeta.setPlayerProfile(Bukkit.createProfile(uuid).update().get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Uses a {@code https://textures.minecraft.net/texture/} url to set the textures
     * @param url the url
     * @return this instance to make chain calls
     */
    @NotNull
    public SkullBuilder setSkullTextureFromUrl(@NotNull String url) {
        return this.editMeta(meta -> {
            final UUID uuid = UUID.randomUUID();
            final PlayerProfile playerProfile = Bukkit.createProfile(uuid);
            final PlayerTextures textures = playerProfile.getTextures();
            try {
                textures.setSkin(new URL(url));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            playerProfile.setTextures(textures);
            meta.setPlayerProfile(playerProfile);
        });
    }

    /**
     * Sets skull textures
     * @param texture the {@link Base64} decoded string
     * @return this to make chain calls
     */
    @NotNull
    public SkullBuilder texture(@NotNull String texture) {
        Preconditions.checkNotNull(texture, "texture");
        return getMeta(meta -> {
            final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
            final PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(URI.create(getUrl(texture)).toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            profile.setTextures(textures);
            playerProfile(profile);
        });
    }


    public boolean isNonValidSkull() {
        return !List.of(
            Material.CREEPER_HEAD,
            Material.DRAGON_HEAD,
            Material.CREEPER_WALL_HEAD,
            Material.PIGLIN_HEAD,
            Material.DRAGON_WALL_HEAD,
            Material.PLAYER_WALL_HEAD,
            Material.PIGLIN_WALL_HEAD,
            Material.ZOMBIE_HEAD,
            Material.ZOMBIE_WALL_HEAD,
            Material.PLAYER_HEAD
        ).contains(material());
    }

    @NotNull
    public String getUrl(@NotNull String encodedData) {
        checkNotNull(encodedData, "texture");
        String decoded = new String(Base64.getDecoder().decode(encodedData));
        JsonObject object = JsonParser.parseString(decoded).getAsJsonObject();
        return object.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
    }


}
