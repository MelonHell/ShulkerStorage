package ru.melonhell.shulkerstorage.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.melonhell.shulkerstorage.gui.IGui;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Storage {
    private final Block block1;
    private final Block block2;
    private final List<IGui> activeGuis = new ArrayList<>();

    public List<Block> getBlocksInside() {

        List<Block> blockList = new ArrayList<>();

        int minX = Math.min(block1.getX(), block2.getX());
        int minY = Math.min(block1.getY(), block2.getY());
        int minZ = Math.min(block1.getZ(), block2.getZ());

        int maxX = Math.max(block1.getX(), block2.getX());
        int maxY = Math.max(block1.getY(), block2.getY());
        int maxZ = Math.max(block1.getZ(), block2.getZ());

        // Check valid
        if (maxX - minX < 2 || maxY - minY < 2 || maxZ - minZ < 2) return blockList;

        for (int x = minX + 1; x < maxX; x++) {
            for (int y = minY + 1; y < maxY; y++) {
                for (int z = minZ + 1; z < maxZ; z++) {
                    Block b = block1.getWorld().getBlockAt(x, y, z);
                    blockList.add(b);
                }
            }
        }

        return blockList;
    }

    private List<Inventory> getInventories() {
        List<Inventory> inventoryList = new ArrayList<>();
        List<Block> blocks = getBlocksInside();
        for (Block block : blocks) {
            BlockState state = block.getState();
            if (state instanceof ShulkerBox) {
                ShulkerBox shulkerBox = (ShulkerBox) state;
                Inventory inventory = shulkerBox.getInventory();
                inventoryList.add(inventory);
            }
        }
        return inventoryList;
    }

    private List<ItemStack> getItemStacks() {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (Inventory inventory : getInventories()) {
            for (ItemStack itemStack : inventory) {
                if (itemStack != null) itemStackList.add(itemStack);
            }
        }
        return itemStackList;
    }

//    private boolean hasMaterial(List<StorageItem> storageItemList, Material material) {
//        for (StorageItem storageItem : storageItemList) {
//            if (storageItem.getType().equals(material)) return true;
//        }
//        return false;
//    }

    private boolean addIfHasStorageItem(List<StorageItem> storageItemList, ItemStack itemStack) {
        for (StorageItem storageItem : storageItemList) {
            if (storageItem.getItemStack().isSimilar(itemStack)) {
                storageItem.setAmount(storageItem.getAmount() + itemStack.getAmount());
                return true;
            }
        }
        return false;
    }

    private void addToStorageItemList(List<StorageItem> storageItemList, ItemStack itemStack) {
        boolean foo = addIfHasStorageItem(storageItemList, itemStack);
        if (!foo) storageItemList.add(new StorageItem(itemStack, itemStack.getAmount()));
    }

    public List<StorageItem> getStorageItems() {
        List<StorageItem> storageItemList = new ArrayList<>();
        for (ItemStack itemStack : getItemStacks()) {
            addToStorageItemList(storageItemList, itemStack);
        }
        return storageItemList;
    }

    public ItemStack pullItemStack(StorageItem storageItem) {
        ItemStack neededItemStack = null;
        for (Inventory inventory : getInventories()) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack == null) continue;
                if (itemStack.isSimilar(storageItem.getItemStack())) {
                    if (neededItemStack == null) {
                        if (itemStack.getAmount() == storageItem.getAmount()) {
                            inventory.setItem(i, null);
                            return itemStack;
                        } else if (itemStack.getAmount() > storageItem.getAmount()) {
                            ItemStack invItem = itemStack.clone();
                            invItem.setAmount(itemStack.getAmount() - storageItem.getAmount());
                            inventory.setItem(i, invItem);
                            ItemStack retItem = itemStack.clone();
                            retItem.setAmount(storageItem.getAmount());
                            return retItem;
                        } else if (itemStack.getAmount() < storageItem.getAmount()) {
                            inventory.setItem(i, null);
                            neededItemStack = itemStack;
                        }
                    } else {
                        if (neededItemStack.isSimilar(itemStack)) {
                            if (itemStack.getAmount() == storageItem.getAmount() - neededItemStack.getAmount()) {
                                inventory.setItem(i, null);
                                neededItemStack.setAmount(neededItemStack.getAmount() + itemStack.getAmount());
                                return neededItemStack;
                            } else if (itemStack.getAmount() > storageItem.getAmount() - neededItemStack.getAmount()) {
                                ItemStack invItem = itemStack.clone();
                                invItem.setAmount(itemStack.getAmount() - (storageItem.getAmount() - neededItemStack.getAmount()));
                                inventory.setItem(i, invItem);
                                neededItemStack.setAmount(storageItem.getAmount());
                                return neededItemStack;
                            } else if (itemStack.getAmount() < storageItem.getAmount() - neededItemStack.getAmount()) {
                                inventory.setItem(i, null);
                                neededItemStack.setAmount(neededItemStack.getAmount() + itemStack.getAmount());
                            }
                        }
                    }
                }
            }
        }
        return neededItemStack;
    }

    public ItemStack putItemStack(ItemStack putItemStack1) {
        ItemStack putItemStack = putItemStack1.clone();
        if (putItemStack.getType().name().endsWith("SHULKER_BOX")) return putItemStack;
        for (Inventory inventory : getInventories()) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack == null) continue;
                if (itemStack.getAmount() < itemStack.getMaxStackSize() && itemStack.isSimilar(putItemStack)) {
                    if (putItemStack.getAmount() <= itemStack.getMaxStackSize() - itemStack.getAmount()) {
                        itemStack.setAmount(itemStack.getAmount() + putItemStack.getAmount());
                        return null;
                    } else {
                        putItemStack.setAmount(putItemStack.getAmount() - (itemStack.getMaxStackSize() - itemStack.getAmount()));
                        itemStack.setAmount(itemStack.getMaxStackSize());
                    }
                }
            }
        }
        for (Inventory inventory : getInventories()) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack == null) {
                    inventory.setItem(i, putItemStack);
                    return null;
                }
            }
        }
        return putItemStack;
    }
}
