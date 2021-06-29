package ru.wbjh.shulkerstorage.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import ru.wbjh.shulkerstorage.ItemCreator;
import ru.wbjh.shulkerstorage.SortUtils;
import ru.wbjh.shulkerstorage.Storage;

public abstract class AbstractGui implements IGui {
    private final Storage storage;
    private final ChestGui chestGui;
    private final ItemCreator itemCreator;
    SortUtils.SortType sortType = SortUtils.SortType.ID;
    boolean sortReverse = false;

    protected AbstractGui(Storage storage, ChestGui chestGui) {
        this.itemCreator = new ItemCreator(this);
        this.storage = storage;
        this.chestGui = chestGui;
    }
}
