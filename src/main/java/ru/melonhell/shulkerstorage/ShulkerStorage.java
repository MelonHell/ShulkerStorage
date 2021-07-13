package ru.melonhell.shulkerstorage;

import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.configmanager.ConfigManager;
import ru.melonhell.shulkerstorage.commands.TestCommand;
import ru.melonhell.shulkerstorage.configs.MainConfig;
import ru.melonhell.shulkerstorage.storage.StorageFinder;
import ru.melonhell.shulkerstorage.terminal.OpenGuiListener;
import ru.melonhell.shulkerstorage.terminal.TerminalUtils;

public final class ShulkerStorage extends JavaPlugin {

    @Override
    public void onEnable() {

        MainConfig mainConfig = new MainConfig(this);

        StorageFinder storageFinder = new StorageFinder(mainConfig);
        TerminalUtils terminalUtils = new TerminalUtils(mainConfig, this);
        getCommand("sstest").setExecutor(new TestCommand(terminalUtils));
        getServer().getPluginManager().registerEvents(new OpenGuiListener(storageFinder), this);
//        getServer().getPluginManager().registerEvents(new TerminalListeners(terminalUtils, storageFinder), this);
    }

    @Override
    public void onDisable() {}
}
