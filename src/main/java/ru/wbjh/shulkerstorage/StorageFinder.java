package ru.wbjh.shulkerstorage;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class StorageFinder {

    private boolean isFrame(Block block) {
        Material type = block.getType();
        if (type.equals(Material.IRON_BLOCK)) return true;
        if (type.equals(Material.GLASS)) return true;
        return false;
    }

    public Storage findStorage(Block terminal) {
        Block frameBlock = null;
        List<Block> relatives = new ArrayList<>();
        relatives.add(terminal.getRelative(BlockFace.NORTH));
        relatives.add(terminal.getRelative(BlockFace.SOUTH));
        relatives.add(terminal.getRelative(BlockFace.WEST));
        relatives.add(terminal.getRelative(BlockFace.EAST));

        for (Block relative : relatives) {
            if (isFrame(relative)) {
                frameBlock = relative;
                break;
            }
        }

        if (frameBlock == null) return null;

        Block block1 = frameBlock;
        while (isFrame(block1)) {
            block1 = block1.getRelative(BlockFace.DOWN);
        }
        block1 = block1.getRelative(BlockFace.UP);
        while (isFrame(block1)) {
            block1 = block1.getRelative(BlockFace.WEST);
        }
        block1 = block1.getRelative(BlockFace.EAST);
        while (isFrame(block1)) {
            block1 = block1.getRelative(BlockFace.SOUTH);
        }
        block1 = block1.getRelative(BlockFace.NORTH);

        Block block2 = frameBlock;
        while (isFrame(block2)) {
            block2 = block2.getRelative(BlockFace.UP);
        }
        block2 = block2.getRelative(BlockFace.DOWN);
        while (isFrame(block2)) {
            block2 = block2.getRelative(BlockFace.EAST);
        }
        block2 = block2.getRelative(BlockFace.WEST);
        while (isFrame(block2)) {
            block2 = block2.getRelative(BlockFace.NORTH);
        }
        block2 = block2.getRelative(BlockFace.SOUTH);

        return new Storage(block1, block2);
    }

}
