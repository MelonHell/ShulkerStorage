package ru.wbjh.shulkerstorage.gui;

import org.bukkit.entity.Player;
import ru.wbjh.shulkerstorage.Storage;

public interface IGui {
    void refresh();
    void show(Player player);
    Storage getStorage();
}
