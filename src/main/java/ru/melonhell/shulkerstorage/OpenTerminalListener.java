package ru.melonhell.shulkerstorage;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.melonhell.shulkerstorage.gui.IGui;
import ru.melonhell.shulkerstorage.gui.ScrollableGui;
import ru.melonhell.shulkerstorage.storage.Storage;

public class OpenTerminalListener implements Listener {

    final Main plugin;

    public OpenTerminalListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if (block == null || !block.getType().equals(Material.CREEPER_WALL_HEAD)) return;
        event.setCancelled(true);
        StorageFinder sf = new StorageFinder(plugin.config);
        Storage storage = sf.findStorage(block);
        if (storage == null) {
            player.sendMessage("Hranilishe ne naideno");
            return;
        }
        IGui gui = new ScrollableGui(storage);
        gui.show(player);
    }
}
