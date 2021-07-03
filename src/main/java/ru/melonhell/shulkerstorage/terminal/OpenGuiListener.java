package ru.melonhell.shulkerstorage.terminal;

import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import ru.melonhell.shulkerstorage.RegionUtils;
import ru.melonhell.shulkerstorage.gui.IGui;
import ru.melonhell.shulkerstorage.gui.ScrollableGui;
import ru.melonhell.shulkerstorage.storage.Storage;
import ru.melonhell.shulkerstorage.storage.StorageFinder;

@RequiredArgsConstructor
public class OpenGuiListener implements Listener {

    private final StorageFinder storageFinder;

//    @EventHandler
//    public void test(PlayerInteractEvent event) {
//        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
//        boolean access = RegionUtils.checkAccess(event.getPlayer(), event.getClickedBlock());
//        event.getPlayer().sendMessage("access = " + access);
//    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if (block == null || !storageFinder.isFrame(block)) return;
        Storage storage = storageFinder.findStorage(block);
        if (storage == null) return;
        event.setCancelled(true);
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (!RegionUtils.checkAccess(player, block)) return;
        IGui gui = new ScrollableGui(storage);
        gui.show(player);
    }
}
