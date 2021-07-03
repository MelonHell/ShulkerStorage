package ru.melonhell.shulkerstorage;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class RegionUtils {
    public static boolean checkAccess(Player player, Block block) {

        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, player);
        Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
        return !blockBreakEvent.isCancelled();

//        BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(Material.AIR), player, true, EquipmentSlot.HAND);
//        Bukkit.getServer().getPluginManager().callEvent(blockPlaceEvent);
//        return !blockPlaceEvent.isCancelled();
    }
}
