package ru.melonhell.shulkerstorage;

import org.bukkit.plugin.java.JavaPlugin;
import ru.melonhell.shulkerstorage.commands.TestCommand;
import ru.melonhell.shulkerstorage.configs.Config;
import ru.melonhell.shulkerstorage.storage.StorageFinder;
import ru.melonhell.shulkerstorage.terminal.OpenGuiListener;
import ru.melonhell.shulkerstorage.terminal.TerminalUtils;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Config config = new Config(this);
        StorageFinder storageFinder = new StorageFinder(config);
        TerminalUtils terminalUtils = new TerminalUtils(config, this);
        getCommand("sstest").setExecutor(new TestCommand(terminalUtils));
        getServer().getPluginManager().registerEvents(new OpenGuiListener(storageFinder), this);
//        getServer().getPluginManager().registerEvents(new TerminalListeners(terminalUtils, storageFinder), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
