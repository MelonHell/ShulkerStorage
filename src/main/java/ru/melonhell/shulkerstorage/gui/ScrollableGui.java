package ru.melonhell.shulkerstorage.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.Orientable;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.melonhell.shulkerstorage.ShulkerStorage;
import ru.melonhell.shulkerstorage.storage.StorageItem;
import ru.melonhell.shulkerstorage.storage.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ScrollableGui extends AbstractGui {

    private final Material background = Material.LIGHT_GRAY_STAINED_GLASS_PANE;
    private final MasonryPane masonryPane;
    private final StaticPane navPane;
    private final List<Pane> linePaneList = new ArrayList<>();
    int scrollPosition = 0;

    public ScrollableGui(Storage storage) {
        super(storage, new ChestGui(6, "ShulkerStorage"));
        masonryPane = new MasonryPane(8, 6);
        masonryPane.setOrientation(Orientable.Orientation.VERTICAL);
        chestGui.addPane(masonryPane);

        navPane = new StaticPane(8, 0, 1, 6);
        chestGui.addPane(navPane);

        chestGui.setOnClose(inventoryCloseEvent -> {
            storage.getActiveGuis().remove(this);
        });

        chestGui.setOnTopClick(inventoryClickEvent -> {
//            inventoryClickEvent.getWhoClicked().sendMessage("top click " + inventoryClickEvent.getAction());
            inventoryClickEvent.setCancelled(true);
            if (inventoryClickEvent.getAction().equals(InventoryAction.PLACE_SOME)) {
//                inventoryClickEvent.getWhoClicked().sendMessage("PLACE_SOME " + inventoryClickEvent.getClickedInventory());
//                if (inventoryClickEvent.getClickedInventory().getType().equals(InventoryType.PLAYER))
                inventoryClickEvent.setCancelled(false);
            }
            if (inventoryClickEvent.getAction().equals(InventoryAction.PLACE_ALL)) {
                ItemStack cursor = inventoryClickEvent.getCursor();
                if (cursor != null) {
                    ItemStack drop = storage.putItemStack(cursor);
                    inventoryClickEvent.setCursor(drop);
                }
                refreshAll();
            }
            if (inventoryClickEvent.getAction().equals(InventoryAction.SWAP_WITH_CURSOR) && inventoryClickEvent.getClick().isLeftClick()) {
                ItemStack cursor = inventoryClickEvent.getCursor();
                if (cursor != null) {
                    ItemStack drop = storage.putItemStack(cursor);
                    inventoryClickEvent.setCursor(drop);
                }
                refreshAll();
            }
        });

        chestGui.setOnTopDrag(inventoryDragEvent -> {
//            inventoryDragEvent.getWhoClicked().sendMessage("top drag " + inventoryDragEvent.getType());
            inventoryDragEvent.setCancelled(true);
        });

        chestGui.setOnBottomClick(inventoryClickEvent -> {
//            inventoryClickEvent.getWhoClicked().sendMessage("bottom click " + inventoryClickEvent.getAction());
            if (inventoryClickEvent.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                inventoryClickEvent.setCancelled(true);
                ItemStack cursor = inventoryClickEvent.getClickedInventory().getItem(inventoryClickEvent.getSlot());
                if (cursor != null) {
                    ItemStack drop = storage.putItemStack(cursor);
                    inventoryClickEvent.getClickedInventory().setItem(inventoryClickEvent.getSlot(), drop);
                }
                refreshAll();
            }
        });
    }

    private void scroll(int i) {
        if (scrollPosition + i >= 0 && scrollPosition + i < linePaneList.size() - 5) {
            scrollPosition += i;
            updateGui();
        }
    }

    private void updateGui() {
        masonryPane.clear();
        for (int i = 0; i < 6; i++) {
            if (i + scrollPosition < linePaneList.size()) {
                masonryPane.addPane(linePaneList.get(i + scrollPosition));
            }
        }
        chestGui.update();
    }

    public void refresh() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ShulkerStorage.getPlugin(ShulkerStorage.class), this::refresh1 , 1);
    }

    private void refresh1() {
        storage.updateList();
        List<StorageItem> storageItemList = storage.getStorageItems();
        SortUtils.sort(storageItemList, sortType, sortReverse);
        navPane.clear();
        // SCROLL UP
        navPane.fillWith(new ItemStack(background));
        navPane.addItem(new GuiItem(new ItemStack(Material.ARROW), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) scroll(-1);
        }), 0, 0);
        // SCROLL DOWN
        navPane.addItem(new GuiItem(new ItemStack(Material.ARROW), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) scroll(1);
        }), 0, 5);
        // SORT TYPE
        ItemStack sortItem = new ItemStack(Material.PAPER);
        updateSortItemMeta(sortItem);
        navPane.addItem(new GuiItem(sortItem, inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) {
                changeSortType();
                refresh();
            } else if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_HALF)) {
                sortReverse = !sortReverse;
                refresh();
            }
        }), 0, 1);
        // INFO
        ItemStack infoItem = new ItemStack(Material.BOOK);
        ItemMeta meta = infoItem.getItemMeta();
        meta.setLore(Arrays.asList("Empty slots: " + storage.getEmptySlotsCount()));
        infoItem.setItemMeta(meta);
        navPane.addItem(new GuiItem(infoItem, inventoryClickEvent -> {}), 0, 2);
        linePaneList.clear();
        for (int i = 0; i <= storageItemList.size() / 8; i++) {
            StaticPane linePane = new StaticPane(8, 1);
            linePaneList.add(linePane);
            for (int j = 0; j < 8; j++) {
                int itemId = i * 8 + j;
                if (itemId < storageItemList.size()) {
                    StorageItem storageItem = storageItemList.get(itemId);
                    linePane.addItem(itemCreator.createItem(storageItem), j, 0);
                }
            }
            linePane.fillWith(new ItemStack(background));
        }
        for (int i = 0; i < 5; i++) {
            StaticPane linePane = new StaticPane(8, 1);
            linePaneList.add(linePane);
            linePane.fillWith(new ItemStack(background));
        }
        updateGui();
    }
}
