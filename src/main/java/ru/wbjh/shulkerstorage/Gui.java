package ru.wbjh.shulkerstorage;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Gui {

    public void show(Player player) {}

    public static void testShow(Player player, List<ItemStack> itemStackList) {
        ChestGui gui = new ChestGui(5, "My GUI");
    }
}
