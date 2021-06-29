package ru.wbjh.shulkerstorage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Storage {
    private final Block block1;
    private final Block block2;

    public List<Block> getBlocksInside() {

        List<Block> blockList = new ArrayList<>();

        Location loc1 = block1.getLocation();
        Location loc2 = block2.getLocation();

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        // Check valid
        if (maxX - minX < 2 || maxY - minY < 2 || maxZ - minZ < 2) return blockList;

        for (int x = minX + 1; x < maxX; x++) {
            for (int y = minY + 1; y < maxY; y++) {
                for (int z = minZ + 1; z < maxZ; z++) {
                    blockList.add(block1.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blockList;
    }

    public List<ItemStack> getItemStacks() {
        List<ItemStack> itemStackList = new ArrayList<>();
        List<Block> blocks = getBlocksInside();
        for (Block block : blocks) {
            if (block instanceof ShulkerBox) {
                ShulkerBox shulkerBox = (ShulkerBox) block;
                for (ItemStack itemStack : shulkerBox.getInventory()) {
                    itemStackList.add(itemStack);
                }
            }
        }
        return itemStackList;
    }

}
