package ru.wbjh.shulkerstorage.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.Orientable;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import ru.wbjh.shulkerstorage.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScrollableGui implements IGui {

    private final Storage storage;
    private final ChestGui gui;
    private final MasonryPane masonryPane;
    private final StaticPane navPane;
    private final List<Pane> linePaneList = new ArrayList<>();
    private final ItemCreator itemCreator;
    int scrollPosition = 0;
    SortUtils.SortType sortType = SortUtils.SortType.ID;
    boolean sortReverse = false;

    public ScrollableGui(Storage storage) {
        itemCreator = new ItemCreator(this);
        this.storage = storage;
        gui = new ChestGui(6, "ShulkerStorage");
        masonryPane = new MasonryPane(8, 6);
        masonryPane.setOrientation(Orientable.Orientation.VERTICAL);
        gui.addPane(masonryPane);

        navPane = new StaticPane(8, 0, 1, 6);
        // SCROLL UP
        navPane.addItem(new GuiItem(new ItemStack(Material.ARROW), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) scroll(-1);
        }), 0, 0);
        // SCROLL DOWN
        navPane.addItem(new GuiItem(new ItemStack(Material.ARROW), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) scroll(1);
        }), 0, 5);
        // SORT TYPE
        navPane.addItem(new GuiItem(new ItemStack(Material.PAPER), inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_ALL)) {
                changeSortType();
            } else if (inventoryClickEvent.getAction().equals(InventoryAction.PICKUP_HALF)) {
                sortReverse = !sortReverse;
                refresh();
            }
        }), 0, 1);
        gui.addPane(navPane);

        gui.setOnTopClick(inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
//            inventoryClickEvent.getWhoClicked().sendMessage("top click " + inventoryClickEvent.getAction().name());
            if (inventoryClickEvent.getAction().equals(InventoryAction.PLACE_ALL)) {
                ItemStack cursor = inventoryClickEvent.getCursor();
                if (cursor != null) {
                    ItemStack drop = storage.putItemStack(cursor);
                    inventoryClickEvent.setCursor(drop);
                }
                refresh();
            }
        });

        gui.setOnTopDrag(inventoryDragEvent -> {
            inventoryDragEvent.setCancelled(true);
//            inventoryDragEvent.getWhoClicked().sendMessage("top drag " + inventoryDragEvent.getType().name());
        });

        gui.setOnBottomClick(inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                inventoryClickEvent.setCancelled(true);
                ItemStack cursor = inventoryClickEvent.getClickedInventory().getItem(inventoryClickEvent.getSlot());
                if (cursor != null) {
                    ItemStack drop = storage.putItemStack(cursor);
                    inventoryClickEvent.getClickedInventory().setItem(inventoryClickEvent.getSlot(), drop);
                }
                refresh();
            }
//            inventoryClickEvent.getWhoClicked().sendMessage("bottom click " + inventoryClickEvent.getAction().name());
        });

//        gui.setOnBottomDrag(inventoryDragEvent -> {
//            inventoryDragEvent.getWhoClicked().sendMessage("bottom drag " + inventoryDragEvent.getType().name());
//        });
    }

    private void changeSortType() {
        int val = sortType.ordinal()+1;
        if (val >= SortUtils.SortType.values().length) val = 0;
        sortType = SortUtils.SortType.values()[val];
        refresh();
    }

    private void scroll(int i) {
        if (scrollPosition + i >= 0 && scrollPosition + i < linePaneList.size()) {
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
        gui.update();
    }

    public void show(Player player) {
        gui.show(player);
    }

    public void refresh() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), this::refresh1 , 1);
    }

    private void refresh1() {
        List<StorageItem> storageItemList = storage.getStorageItems();
        SortUtils.sort(storageItemList, sortType, sortReverse);
        linePaneList.clear();
        for (int i = 0; i <= storageItemList.size() / 8; i++) {
            StaticPane outlinePane = new StaticPane(8, 1);
            linePaneList.add(outlinePane);
            for (int j = 0; j < 8; j++) {
                int itemId = i * 8 + j;
                if (itemId < storageItemList.size()) {
                    StorageItem storageItem = storageItemList.get(itemId);
                    outlinePane.addItem(itemCreator.createItem(storageItem), j, 0);
                }
            }
        }
        updateGui();
    }
}
