package ru.wbjh.shulkerstorage.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.wbjh.shulkerstorage.Storage;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PaginatedGui implements IGui {

    private final Storage storage;
    private final ChestGui gui;
    private final PaginatedPane paginatedPane;
    private final StaticPane navPane;
    private final List<OutlinePane> outlinePaneList = new ArrayList<>();
    int page = 0;

    public PaginatedGui(Storage storage) {
        this.storage = storage;
        gui = new ChestGui(6, "My GUI");
        paginatedPane = new PaginatedPane(0, 0, 9, 5);
        gui.addPane(paginatedPane);

        navPane = new StaticPane(0, 5, 9, 1);
        navPane.addItem(new GuiItem(new ItemStack(Material.ARROW), inventoryClickEvent -> {
            scroll(-1);
            inventoryClickEvent.setCancelled(true);
        }), 0, 0);
        navPane.addItem(new GuiItem(new ItemStack(Material.ARROW), inventoryClickEvent -> {
            scroll(1);
            inventoryClickEvent.setCancelled(true);
        }), 8, 0);
        gui.addPane(navPane);
    }

    private void scroll(int i) {
        page += i;
        paginatedPane.setPage(page);
        gui.update();
    }

    public void show(Player player) {
        gui.show(player);
    }

    public void refresh() {
//        for (int i = 0; i <= itemStackList.size() / 45; i++) {
//            OutlinePane outlinePane = new OutlinePane(0, 0, 9, 5);
//            outlinePaneList.add(outlinePane);
//            for (int j = 0; j < 45; j++) {
//                int itemId = i * 45 + j;
//                if (itemId < itemStackList.size()) {
//                    outlinePane.addItem(new GuiItem(itemStackList.get(itemId)));
//                }
//            }
//            paginatedPane.addPane(i, outlinePane);
//        }
    }
}
