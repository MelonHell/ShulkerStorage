package ru.melonhell.shulkerstorage;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import ru.melonhell.shulkerstorage.gui.AbstractGui;

import java.util.Collections;
import java.util.HashMap;

@AllArgsConstructor
public class ItemCreator {

    private final AbstractGui gui;

    public GuiItem createItem(StorageItem storageItem) {
        ItemStack itemStack = storageItem.getItemStack().clone();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Amount: " + storageItem.getAmount()));
        itemStack.setItemMeta(meta);
        return new GuiItem(itemStack, inventoryClickEvent -> {
            InventoryAction action = inventoryClickEvent.getAction();
            ClickType clickType = inventoryClickEvent.getClick();
            switch (action) {
                case PICKUP_ALL:
                    inventoryClickEvent.getView().setCursor(gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), itemStack.getMaxStackSize())));
                    gui.refresh();
                    break;
                case PICKUP_HALF:
                    inventoryClickEvent.getView().setCursor(gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), 1)));
                    gui.refresh();
                    break;
                case MOVE_TO_OTHER_INVENTORY:
                    PlayerInventory pInv = inventoryClickEvent.getWhoClicked().getInventory();
                    int size = clickType.isRightClick() ? 1 : itemStack.getMaxStackSize();
                    HashMap<Integer, ItemStack> backItems = pInv.addItem(gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), size)));
                    backItems.forEach((integer, itemStack1) -> gui.getStorage().putItemStack(itemStack1));
                    gui.refresh();
                    break;
                case SWAP_WITH_CURSOR:
                    if (inventoryClickEvent.getCurrentItem() != null) {
                        ItemStack cursor = inventoryClickEvent.getCursor();
                        if (cursor == null) break;

                        if (clickType.isRightClick() && storageItem.getItemStack().isSimilar(cursor)) {
                            ItemStack item = gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), 1));
                            if (cursor.isSimilar(item)) {
                                cursor.setAmount(cursor.getAmount() + item.getAmount());
                                gui.refresh();
                            }
                        } else {
                            ItemStack drop = gui.getStorage().putItemStack(cursor);
                            inventoryClickEvent.getView().setCursor(drop);
                            gui.refresh();
                        }
                    }
                    break;
            }
        });
    }
}
