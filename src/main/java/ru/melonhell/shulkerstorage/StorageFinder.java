package ru.melonhell.shulkerstorage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import ru.melonhell.shulkerstorage.configs.Config;
import ru.melonhell.shulkerstorage.storage.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorageFinder {

    private final Config config;

    public StorageFinder(Config config) {
        this.config = config;
    }


    private boolean isFrame(Block block) {
        Material type = block.getType();
        List<List<Material>> allMaterials = Arrays.asList(config.FRAME_BOTTOM_EDGE, config.FRAME_BOTTOM_FACE, config.FRAME_BOTTOM_VERTEX, config.FRAME_SIDE_EDGE, config.FRAME_SIDE_FACE, config.FRAME_TOP_EDGE, config.FRAME_TOP_FACE, config.FRAME_TOP_VERTEX);
        for (List<Material> allMaterial : allMaterials) {
            if (allMaterial.contains(type)) return true;
        }
        return false;
    }

    public boolean isValid(Storage storage) {
        Block block1 = storage.getBlock1();
        Block block2 = storage.getBlock2();
        World world = block1.getWorld();

        int minX = Math.min(block1.getX(), block2.getX());
        int minY = Math.min(block1.getY(), block2.getY());
        int minZ = Math.min(block1.getZ(), block2.getZ());
        int maxX = Math.max(block1.getX(), block2.getX());
        int maxY = Math.max(block1.getY(), block2.getY());
        int maxZ = Math.max(block1.getZ(), block2.getZ());

        if (maxX - minX < 2 || maxY - minY < 2 || maxZ - minZ < 2) return false;

        Bukkit.getLogger().info("maxX - minX + 1 = " + (maxX - minX + 1));
        Bukkit.getLogger().info("config.LIMITS_WIDTH = " + config.LIMITS_WIDTH);
        if (maxX - minX + 1 > config.LIMITS_WIDTH) return false;
        if (maxY - minY + 1 > config.LIMITS_HEIGHT) return false;
        if (maxZ - minZ + 1 > config.LIMITS_WIDTH) return false;

        // faceBottom
        Bukkit.getLogger().info("faceBottom");
        for (int x = minX + 1; x < maxX; x++) {
            for (int z = minZ + 1; z < maxZ; z++) {
                if (!config.FRAME_BOTTOM_FACE.contains(world.getBlockAt(x, minY, z).getType())) return false;
            }
        }
        // vertexBottom
        Bukkit.getLogger().info("vertexBottom");
        if (!config.FRAME_BOTTOM_VERTEX.contains(world.getBlockAt(minX, minY, minZ).getType())) return false;
        if (!config.FRAME_BOTTOM_VERTEX.contains(world.getBlockAt(minX, minY, maxZ).getType())) return false;
        if (!config.FRAME_BOTTOM_VERTEX.contains(world.getBlockAt(maxX, minY, minZ).getType())) return false;
        if (!config.FRAME_BOTTOM_VERTEX.contains(world.getBlockAt(maxX, minY, maxZ).getType())) return false;
        // edgeBottom
        Bukkit.getLogger().info("edgeBottom");
        for (int x = minX + 1; x < maxX; x++) {
            if (!config.FRAME_BOTTOM_EDGE.contains(world.getBlockAt(x, minY, minZ).getType())) return false;
            if (!config.FRAME_BOTTOM_EDGE.contains(world.getBlockAt(x, minY, maxZ).getType())) return false;
        }
        for (int z = minZ + 1; z < maxZ; z++) {
            if (!config.FRAME_BOTTOM_EDGE.contains(world.getBlockAt(minX, minY, z).getType())) return false;
            if (!config.FRAME_BOTTOM_EDGE.contains(world.getBlockAt(maxX, minY, z).getType())) return false;
        }
        // faceSide
        Bukkit.getLogger().info("faceSide");
        for (int y = minY + 1; y < maxY; y++) {
            for (int x = minX + 1; x < maxX; x++) {
                if (!config.FRAME_SIDE_FACE.contains(world.getBlockAt(x, y, minZ).getType())) return false;
                if (!config.FRAME_SIDE_FACE.contains(world.getBlockAt(x, y, maxZ).getType())) return false;
            }
            for (int z = minZ + 1; z < maxZ; z++) {
                if (!config.FRAME_SIDE_FACE.contains(world.getBlockAt(minX, y, z).getType())) return false;
                if (!config.FRAME_SIDE_FACE.contains(world.getBlockAt(maxX, y, z).getType())) return false;
            }
        }
        // edgeSide
        Bukkit.getLogger().info("edgeSide");
        for (int y = minY + 1; y < maxY; y++) {
            if (!config.FRAME_SIDE_EDGE.contains(world.getBlockAt(minX, y, minZ).getType())) return false;
            if (!config.FRAME_SIDE_EDGE.contains(world.getBlockAt(minX, y, maxZ).getType())) return false;
            if (!config.FRAME_SIDE_EDGE.contains(world.getBlockAt(maxX, y, minZ).getType())) return false;
            if (!config.FRAME_SIDE_EDGE.contains(world.getBlockAt(maxX, y, maxZ).getType())) return false;
        }
        // faceTop
        Bukkit.getLogger().info("faceTop");
        for (int x = minX + 1; x < maxX; x++) {
            for (int z = minZ + 1; z < maxZ; z++) {
                if (!config.FRAME_TOP_FACE.contains(world.getBlockAt(x, maxY, z).getType())) return false;
            }
        }
        // vertexTop
        Bukkit.getLogger().info("vertexTop");
        if (!config.FRAME_TOP_VERTEX.contains(world.getBlockAt(minX, maxY, minZ).getType())) return false;
        if (!config.FRAME_TOP_VERTEX.contains(world.getBlockAt(minX, maxY, maxZ).getType())) return false;
        if (!config.FRAME_TOP_VERTEX.contains(world.getBlockAt(maxX, maxY, minZ).getType())) return false;
        if (!config.FRAME_TOP_VERTEX.contains(world.getBlockAt(maxX, maxY, maxZ).getType())) return false;
        // edgeTop
        Bukkit.getLogger().info("edgeTop");
        for (int x = minX + 1; x < maxX; x++) {
            if (!config.FRAME_TOP_EDGE.contains(world.getBlockAt(x, maxY, minZ).getType())) return false;
            if (!config.FRAME_TOP_EDGE.contains(world.getBlockAt(x, maxY, maxZ).getType())) return false;
        }
        for (int z = minZ + 1; z < maxZ; z++) {
            if (!config.FRAME_TOP_EDGE.contains(world.getBlockAt(minX, maxY, z).getType())) return false;
            if (!config.FRAME_TOP_EDGE.contains(world.getBlockAt(maxX, maxY, z).getType())) return false;
        }
        Bukkit.getLogger().info("valid!");
        return true;
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

        Storage storage = new Storage(block1, block2);

        if (!isValid(storage)) return null;

        return storage;
    }

}
