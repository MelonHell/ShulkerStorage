//package ru.melonhell.shulkerstorage.terminal;
//
//import lombok.RequiredArgsConstructor;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockState;
//import org.bukkit.entity.Item;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.block.BlockDropItemEvent;
//import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.event.player.PlayerInteractEvent;
//import ru.melonhell.shulkerstorage.gui.IGui;
//import ru.melonhell.shulkerstorage.gui.ScrollableGui;
//import ru.melonhell.shulkerstorage.storage.Storage;
//import ru.melonhell.shulkerstorage.storage.StorageFinder;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//public class TerminalListeners implements Listener {
//
//    private final TerminalUtils terminalUtils;
//    private final StorageFinder storageFinder;
//
//    @EventHandler
//    public void onTerminalPlace(BlockPlaceEvent event) {
//        if (!terminalUtils.isTerminal(event.getItemInHand())) return;
//        terminalUtils.setIsTerminal(event.getBlock(), event.getBlock().getState());
//    }
//
//    @EventHandler
//    public void onTerminalBreak(BlockDropItemEvent event) {
//        BlockState blockState = event.getBlockState();
//        Block block = event.getBlock();
//        if (!terminalUtils.isTerminalAndRemove(block, blockState)) return;
//        List<Item> items = event.getItems();
//        for (Item item : items) {
//            if (item.getItemStack().getType().equals(Material.PLAYER_HEAD)) {
//                item.setItemStack(terminalUtils.getTerminal());
//            }
//        }
//    }
//
//    @EventHandler
//    public void PlayerInteractEvent(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
//        Block block = event.getClickedBlock();
//        if (block == null || !terminalUtils.isTerminal(block, block.getState())) return;
//        event.setCancelled(true);
//        Storage storage = storageFinder.findStorage(block);
//        if (storage == null) {
//            player.sendMessage("Hranilishe ne naideno");
//            return;
//        }
//        IGui gui = new ScrollableGui(storage);
//        gui.show(player);
//    }
//}
