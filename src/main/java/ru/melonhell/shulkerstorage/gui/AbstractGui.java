package ru.melonhell.shulkerstorage.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.melonhell.shulkerstorage.SortUtils;
import ru.melonhell.shulkerstorage.storage.Storage;
import ru.melonhell.shulkerstorage.ItemCreator;

import java.util.Arrays;

@Getter
public abstract class AbstractGui implements IGui {
    protected final Storage storage;
    protected final ChestGui chestGui;
    protected final ItemCreator itemCreator;
    protected SortUtils.SortType sortType = SortUtils.SortType.ID;
    protected boolean sortReverse = false;

    protected AbstractGui(Storage storage, ChestGui chestGui) {
        this.itemCreator = new ItemCreator(this);
        this.storage = storage;
        this.chestGui = chestGui;
    }

    public void show(Player player) {
        refresh();
        chestGui.show(player);
    }

    protected void updateSortItemMeta(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList("Sort type: " + sortType.name(), "Sort reverse: " + sortReverse));
        itemStack.setItemMeta(meta);
    }

    protected void changeSortType() {
        int val = sortType.ordinal()+1;
        if (val >= SortUtils.SortType.values().length) val = 0;
        sortType = SortUtils.SortType.values()[val];
    }

    public abstract void refresh();
}
