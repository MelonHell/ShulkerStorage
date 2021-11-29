package ru.melonhell.shulkerstorage.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@AllArgsConstructor
public class StorageItem {
    private ItemStack itemStack;
    private int amount;

    public int getSlots() {
        return (int) Math.ceil(amount * 1.0 / itemStack.getMaxStackSize());
    }
}
