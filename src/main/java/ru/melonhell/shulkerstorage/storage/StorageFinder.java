package ru.melonhell.shulkerstorage.storage;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import ru.melonhell.shulkerstorage.Debug;
import ru.melonhell.shulkerstorage.configs.MainConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
public class StorageFinder {

    private final MainConfig config;
    private final List<Storage> storageList = new ArrayList<>();

    public boolean isFrame(Block block) {
        Material type = block.getType();
        List<List<Material>> allMaterials = Arrays.asList(config.getMaterialBottomEdge(), config.getMaterialBottomFace(), config.getMaterialBottomVertex(), config.getMaterialSideEdge(), config.getMaterialSideFace(), config.getMaterialTopEdge(), config.getMaterialTopFace(), config.getMaterialTopVertex());
        for (List<Material> allMaterial : allMaterials) {
            for (Material material : allMaterial) {
                Debug.info("block = " + type + ", test = " + material);
                if (Objects.equals(type, material)) return true;
            }

//            if (allMaterial.contains(type)) return true;
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

        Debug.info("maxX - minX + 1 = " + (maxX - minX + 1));
        Debug.info("config.LIMITS_WIDTH = " + config.getLimitsWidth());
        if (maxX - minX + 1 > config.getLimitsWidth()) return false;
        if (maxY - minY + 1 > config.getLimitsHeight()) return false;
        if (maxZ - minZ + 1 > config.getLimitsWidth()) return false;

        // faceBottom
        Debug.info("faceBottom");
        for (int x = minX + 1; x < maxX; x++) {
            for (int z = minZ + 1; z < maxZ; z++) {
                if (!config.getMaterialBottomFace().contains(world.getBlockAt(x, minY, z).getType())) return false;
            }
        }
        // vertexBottom
        Debug.info("vertexBottom");
        if (!config.getMaterialBottomVertex().contains(world.getBlockAt(minX, minY, minZ).getType())) return false;
        if (!config.getMaterialBottomVertex().contains(world.getBlockAt(minX, minY, maxZ).getType())) return false;
        if (!config.getMaterialBottomVertex().contains(world.getBlockAt(maxX, minY, minZ).getType())) return false;
        if (!config.getMaterialBottomVertex().contains(world.getBlockAt(maxX, minY, maxZ).getType())) return false;
        // edgeBottom
        Debug.info("edgeBottom");
        for (int x = minX + 1; x < maxX; x++) {
            if (!config.getMaterialBottomEdge().contains(world.getBlockAt(x, minY, minZ).getType())) return false;
            if (!config.getMaterialBottomEdge().contains(world.getBlockAt(x, minY, maxZ).getType())) return false;
        }
        for (int z = minZ + 1; z < maxZ; z++) {
            if (!config.getMaterialBottomEdge().contains(world.getBlockAt(minX, minY, z).getType())) return false;
            if (!config.getMaterialBottomEdge().contains(world.getBlockAt(maxX, minY, z).getType())) return false;
        }
        // faceSide
        Debug.info("faceSide");
        for (int y = minY + 1; y < maxY; y++) {
            for (int x = minX + 1; x < maxX; x++) {
                if (!config.getMaterialSideFace().contains(world.getBlockAt(x, y, minZ).getType())) return false;
                if (!config.getMaterialSideFace().contains(world.getBlockAt(x, y, maxZ).getType())) return false;
            }
            for (int z = minZ + 1; z < maxZ; z++) {
                if (!config.getMaterialSideFace().contains(world.getBlockAt(minX, y, z).getType())) return false;
                if (!config.getMaterialSideFace().contains(world.getBlockAt(maxX, y, z).getType())) return false;
            }
        }
        // edgeSide
        Debug.info("edgeSide");
        for (int y = minY + 1; y < maxY; y++) {
            if (!config.getMaterialSideEdge().contains(world.getBlockAt(minX, y, minZ).getType())) return false;
            if (!config.getMaterialSideEdge().contains(world.getBlockAt(minX, y, maxZ).getType())) return false;
            if (!config.getMaterialSideEdge().contains(world.getBlockAt(maxX, y, minZ).getType())) return false;
            if (!config.getMaterialSideEdge().contains(world.getBlockAt(maxX, y, maxZ).getType())) return false;
        }
        // faceTop
        Debug.info("faceTop");
        for (int x = minX + 1; x < maxX; x++) {
            for (int z = minZ + 1; z < maxZ; z++) {
                if (!config.getMaterialTopFace().contains(world.getBlockAt(x, maxY, z).getType())) return false;
            }
        }
        // vertexTop
        Debug.info("vertexTop");
        if (!config.getMaterialTopVertex().contains(world.getBlockAt(minX, maxY, minZ).getType())) return false;
        if (!config.getMaterialTopVertex().contains(world.getBlockAt(minX, maxY, maxZ).getType())) return false;
        if (!config.getMaterialTopVertex().contains(world.getBlockAt(maxX, maxY, minZ).getType())) return false;
        if (!config.getMaterialTopVertex().contains(world.getBlockAt(maxX, maxY, maxZ).getType())) return false;
        // edgeTop
        Debug.info("edgeTop");
        for (int x = minX + 1; x < maxX; x++) {
            if (!config.getMaterialTopEdge().contains(world.getBlockAt(x, maxY, minZ).getType())) return false;
            if (!config.getMaterialTopEdge().contains(world.getBlockAt(x, maxY, maxZ).getType())) return false;
        }
        for (int z = minZ + 1; z < maxZ; z++) {
            if (!config.getMaterialTopEdge().contains(world.getBlockAt(minX, maxY, z).getType())) return false;
            if (!config.getMaterialTopEdge().contains(world.getBlockAt(maxX, maxY, z).getType())) return false;
        }
        Debug.info("valid!");
        return true;
    }

    public Block findFrameBlock(Block terminal) {
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
        return frameBlock;
    }

    public Storage findStorage(Block frameBlock) {
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

        for (Storage storage : storageList) {
            if (storage.getBlock1().getLocation().equals(block1.getLocation()) && storage.getBlock2().getLocation().equals(block2.getLocation())) {
                if (isValid(storage)) return storage;
            }
        }

        Storage storage = new Storage(block1, block2);
        if (!isValid(storage)) return null;
        storageList.add(storage);
        return storage;
    }

}
