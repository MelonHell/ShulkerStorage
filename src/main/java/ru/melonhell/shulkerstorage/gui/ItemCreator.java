package ru.melonhell.shulkerstorage.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import ru.melonhell.shulkerstorage.storage.StorageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class ItemCreator {

    private final AbstractGui gui;

    private void dropItem(HumanEntity player, ItemStack itemStack) {
        Item drop = player.getWorld().dropItemNaturally(player.getEyeLocation(), itemStack);
        drop.setVelocity(player.getLocation().getDirection().multiply(0.4));
    }

    public GuiItem createItem(StorageItem storageItem) {
        ItemStack itemStack = storageItem.getItemStack().clone();
        itemStack.setAmount(Math.min(storageItem.getAmount(), 64));
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Количество: " + storageItem.getAmount());
        lore.add(ChatColor.GRAY + "Слоты: " + storageItem.getSlots());
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return new GuiItem(itemStack, inventoryClickEvent -> {
            InventoryAction action = inventoryClickEvent.getAction();
            ClickType clickType = inventoryClickEvent.getClick();
            HumanEntity player = inventoryClickEvent.getWhoClicked();
            switch (action) {
                case PICKUP_ALL:
                    inventoryClickEvent.getView().setCursor(gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), itemStack.getMaxStackSize())));
                    gui.refreshAll();
                    break;
                case PICKUP_HALF:
                    inventoryClickEvent.getView().setCursor(gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), 1)));
                    gui.refreshAll();
                    break;
                case MOVE_TO_OTHER_INVENTORY:
                    PlayerInventory pInv = inventoryClickEvent.getWhoClicked().getInventory();
                    int size = clickType.isRightClick() ? 1 : itemStack.getMaxStackSize();
                    HashMap<Integer, ItemStack> backItems = pInv.addItem(gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), size)));
                    backItems.forEach((integer, itemStack1) -> gui.getStorage().putItemStack(itemStack1));
                    gui.refreshAll();
                    break;
                case SWAP_WITH_CURSOR:
                    if (inventoryClickEvent.getCurrentItem() != null) {
                        ItemStack cursor = inventoryClickEvent.getCursor();
                        if (cursor == null) break;

                        if (clickType.isRightClick() && storageItem.getItemStack().isSimilar(cursor)) {
                            ItemStack item = gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), 1));
                            if (cursor.isSimilar(item)) {
                                cursor.setAmount(cursor.getAmount() + item.getAmount());
                                gui.refreshAll();
                            }
                        } else {
                            ItemStack drop = gui.getStorage().putItemStack(cursor);
                            inventoryClickEvent.getView().setCursor(drop);
                            gui.refreshAll();
                        }
                    }
                    break;
                case DROP_ONE_SLOT:
                    dropItem(player, gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), 1)));
                    gui.refreshAll();
                    break;
                case DROP_ALL_SLOT:
                    dropItem(player, gui.getStorage().pullItemStack(new StorageItem(storageItem.getItemStack(), itemStack.getMaxStackSize())));
                    gui.refreshAll();
                    break;
            }
        });
    }
}
