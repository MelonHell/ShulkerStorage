package ru.melonhell.shulkerstorage;

import org.bukkit.Bukkit;

public class Debug {

    static boolean enabled = false;

    public static void info(Object text) {
        if (enabled) Bukkit.getLogger().info("§6" + text + "");
    }
}
