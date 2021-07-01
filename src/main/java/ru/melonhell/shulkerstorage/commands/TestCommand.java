package ru.melonhell.shulkerstorage.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        List<ItemStack> itemStackList = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            itemStackList.add(new ItemStack(Material.values()[i+1]));
        }
//        IGui gui = new ScrollableGui();
//        gui.generate(itemStackList);
//        gui.show(player);
        return false;
    }
}
