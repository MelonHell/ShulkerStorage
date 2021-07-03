package ru.melonhell.shulkerstorage;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class HeadUtils {
    public static ItemStack getHead(String texture) {
        return getHead(texture, null, null);
    }
    public static ItemStack getHead(String texture, String itemName) {
        return getHead(texture, itemName, null);
    }
    public static ItemStack getHead(String texture, String itemName, List<String> lore) {
        ItemStack head = createSkull();
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        if (skullMeta == null || texture == null || texture.isEmpty()) return head;
        if (itemName != null) skullMeta.setDisplayName(itemName);
        if (lore != null) skullMeta.setLore(lore);
        mutateItemMeta(skullMeta, texture);
        head.setItemMeta(skullMeta);
        return head;
    }

    public static ItemStack createSkull() {
        try {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } catch (IllegalArgumentException e) {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

    private static void mutateItemMeta(SkullMeta meta, String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Method metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            metaSetProfileMethod.setAccessible(true);
            metaSetProfileMethod.invoke(meta, profile);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            try {
                Field metaProfileField = meta.getClass().getDeclaredField("profile");
                metaProfileField.setAccessible(true);
                metaProfileField.set(meta, profile);

            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                ex2.printStackTrace();
            }
        }
    }
}
