package ru.wbjh.shulkerstorage;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.wbjh.shulkerstorage.gui.ScrollableGui;

public class OpenTerminalListener implements Listener {
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if (block == null || !block.getType().equals(Material.CREEPER_WALL_HEAD)) return;
        StorageFinder sf = new StorageFinder();
        Storage storage = sf.findStorage(block);
        if (storage == null) return;
        ScrollableGui gui = new ScrollableGui(storage);
        gui.refresh();
        gui.show(player);
        event.setCancelled(true);
    }
}
