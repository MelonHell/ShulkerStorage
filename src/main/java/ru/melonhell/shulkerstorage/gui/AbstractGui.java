package ru.melonhell.shulkerstorage.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.melonhell.shulkerstorage.storage.Storage;

import java.util.Arrays;

@Getter
public abstract class AbstractGui implements IGui {
    protected final Storage storage;
    protected final ChestGui chestGui;
    protected final ItemCreator itemCreator;
    protected SortUtils.SortType sortType = SortUtils.SortType.ID;
    protected FilterUtils.FilterType filterType = FilterUtils.FilterType.ALL;
    protected boolean sortReverse = false;

    protected AbstractGui(Storage storage, ChestGui chestGui) {
        this.itemCreator = new ItemCreator(this);
        this.storage = storage;
        this.chestGui = chestGui;
        storage.getActiveGuis().add(this);
    }

    public void show(Player player) {
        refresh();
        chestGui.show(player);
    }

    protected void changeSortType() {
        int val = sortType.ordinal()+1;
        if (val >= SortUtils.SortType.values().length) val = 0;
        sortType = SortUtils.SortType.values()[val];
    }

    protected void changeFilterType(boolean back) {
        int val;
        if (!back) {
            val = filterType.ordinal() + 1;
            if (val >= FilterUtils.FilterType.values().length) val = 0;
        } else {
            val = filterType.ordinal() - 1;
            if (val < 0) val = FilterUtils.FilterType.values().length - 1;
        }
        filterType = FilterUtils.FilterType.values()[val];

    }

    public abstract void refresh();

    public void refreshAll() {
        for (IGui activeGui : storage.getActiveGuis()) {
            activeGui.refresh();
        }
    }
}
