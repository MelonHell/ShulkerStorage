package ru.wbjh.shulkerstorage;

import org.bukkit.plugin.java.JavaPlugin;
import ru.wbjh.shulkerstorage.commands.TestCommand;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("sstest").setExecutor(new TestCommand());
        getServer().getPluginManager().registerEvents(new OpenTerminalListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
