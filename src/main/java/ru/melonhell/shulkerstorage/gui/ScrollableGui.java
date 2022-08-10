package ru.melonhell.shulkerstorage.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.Orientable;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import ru.melonhell.shulkerstorage.ShulkerStorage;
import ru.melonhell.shulkerstorage.storage.StorageItem;
import ru.melonhell.shulkerstorage.storage.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ScrollableGui extends AbstractGui {

    private final MasonryPane masonryPane;
    private final StaticPane navPane;
    private final List<Pane> linePaneList = new ArrayList<>();
    int scrollPosition = 0;

    public ScrollableGui(Storage storage) {
        super(storage, new ChestGui(6, "ShulkerStorage"));
        masonryPane = new MasonryPane(0, 0, 8, 6);
        masonryPane.setOrientation(Orientable.Orientation.VERTICAL);
        chestGui.addPane(masonryPane);

        navPane = new StaticPane(8, 0, 1, 6);
        chestGui.addPane(navPane);

        chestGui.setOnClose(inventoryCloseEvent -> storage.getActiveGuis().remove(this));

        chestGui.setOnTopClick(inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            if (inventoryClickEvent.getAction().equals(InventoryAction.PLACE_SOME)) {
                inventoryClickEvent.setCancelled(false);
            }
            if (inventoryClickEvent.getAction().equals(InventoryAction.PLACE_ALL)) {
                ItemStack cursor = inventoryClickEvent.getCursor();
                if (cursor != null) {
                    ItemStack drop = storage.putItemStack(cursor);
                    inventoryClickEvent.getView().setCursor(drop);
                }
                refreshAll();
            }
            if (inventoryClickEvent.getAction().equals(InventoryAction.SWAP_WITH_CURSOR) && inventoryClickEvent.getClick().isLeftClick()) {
                ItemStack cursor = inventoryClickEvent.getCursor();
                if (cursor != null) {
                    ItemStack drop = storage.putItemStack(cursor);
                    inventoryClickEvent.getView().setCursor(drop);
                }
                refreshAll();
            }
        });

        chestGui.setOnTopDrag(inventoryDragEvent -> inventoryDragEvent.setCancelled(true));

        chestGui.setOnBottomClick(inventoryClickEvent -> {
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
        for (int j = 0; j < Math.abs(i); j++) {
            if (i > 0 && scrollPosition + 1 < linePaneList.size() - 5) {
                scrollPosition++;
            } else if (i < 0 && scrollPosition >= 1) {
                scrollPosition--;
            }
        }
        updateGui();
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
        storageItemList = FilterUtils.filter(storageItemList, filterType);
        SortUtils.sort(storageItemList, sortType, sortReverse);
        navPane.clear();
        // SCROLL UP
        navPane.fillWith(GuiItemCreator.getEmptyItem());
        navPane.addItem(new GuiItem(GuiItemCreator.getScrollUpItem(), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) scroll(-1);
            else if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_HALF)) scroll(-6);
        }), 0, 0);
        // SCROLL DOWN
        navPane.addItem(new GuiItem(GuiItemCreator.getScrollDownItem(), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) scroll(1);
            else if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_HALF)) scroll(6);
        }), 0, 5);
        // INFO
        navPane.addItem(new GuiItem(GuiItemCreator.getInfoItem(storage.getSlotsCount(), storage.getEmptySlotsCount()), inventoryClickEvent -> {}), 0, 1);
        // SORT TYPE
        navPane.addItem(new GuiItem(GuiItemCreator.getSortItem(sortType, sortReverse), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) {
                changeSortType();
                refresh();
            } else if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_HALF)) {
                sortReverse = !sortReverse;
                refresh();
            }
        }), 0, 2);
        // FILTER TYPE
        navPane.addItem(new GuiItem(GuiItemCreator.getFilterItem(filterType), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) {
                scrollPosition = 0;
                changeFilterType(false);
                refresh();
            } else if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_HALF)) {
                scrollPosition = 0;
                changeFilterType(true);
                refresh();
            }
        }), 0, 3);
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
            linePane.fillWith(GuiItemCreator.getEmptyItem());
        }
        for (int i = 0; i < 5; i++) {
            StaticPane linePane = new StaticPane(8, 1);
            linePaneList.add(linePane);
            linePane.fillWith(GuiItemCreator.getEmptyItem());
        }
        updateGui();
    }
}
