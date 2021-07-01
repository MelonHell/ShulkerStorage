package ru.melonhell.shulkerstorage;

import org.bukkit.plugin.java.JavaPlugin;
import ru.melonhell.shulkerstorage.commands.TestCommand;
import ru.melonhell.shulkerstorage.configs.Config;

public final class Main extends JavaPlugin {

    Config config = new Config(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("sstest").setExecutor(new TestCommand());
        getServer().getPluginManager().registerEvents(new OpenTerminalListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
