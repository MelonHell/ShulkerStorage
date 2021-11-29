package ru.melonhell.shulkerstorage.gui;

import org.bukkit.Material;
import ru.melonhell.shulkerstorage.storage.StorageItem;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {

    public enum FilterType {
        ALL,
        BLOCK,
        ITEM,
        ARMOR,
        WEAPON,
        TOOL,
        JEWEL,
        POTION,
        FOOD,
    }

    private static String jewelFilter(String jewels) {
        List<String> list = new ArrayList<>();
        for (String s : jewels.toLowerCase().split(", ")) {
            list.add(String.format("%1$s, raw_%1$s, %1$s_ore, _%1$s_ore, %1$s_ingot, %1$s_nugget, %1$s_block", s));
        }
        return String.join(", ", list);
    }

    private static boolean checkItem(Material material, String filter) {
        String materialName = material.name().toLowerCase();
        for (String s : filter.toLowerCase().split(", ")) {
            if (s.startsWith("_")) {
                if (materialName.endsWith(s)) return true;
            } else {
                if (materialName.equals(s)) return true;
            }
        }
        return false;
    }

    public static List<StorageItem> filter(List<StorageItem> storageItemList, FilterType filterType) {
        List<StorageItem> newList = new ArrayList<>();
        for (StorageItem storageItem : storageItemList) {
            Material material = storageItem.getItemStack().getType();
            switch (filterType) {
                case ALL:
                    newList.add(storageItem);
                    break;
                case ARMOR:
                    if (checkItem(material, "_helmet, _chestplate, _leggings, _boots, elytra"))
                        newList.add(storageItem);
                    break;
                case WEAPON:
                    if (checkItem(material, "_sword, bow, crossbow, trident, shield"))
                        newList.add(storageItem);
                    break;
                case TOOL:
                    if (checkItem(material, "_axe, _pickaxe, _hoe, _shovel, flint_and_steel, _on_a_stick, fishing_rod, shears"))
                        newList.add(storageItem);
                    break;
                case JEWEL:
                    if (checkItem(material, jewelFilter("coal, iron, gold, diamond, emerald, redstone, lapis, netherite, copper") + ", lapis_lazuli, ancient_debris, netherite_scrap"))
                        newList.add(storageItem);
                    break;
                case POTION:
                    if (checkItem(material, "potion, _potion, glass_bottle"))
                        newList.add(storageItem);
                    break;
                case FOOD:
                    if (material.isEdible())
                        newList.add(storageItem);
                    break;
                case BLOCK:
                    if (material.isSolid())
                        newList.add(storageItem);
                    break;
                case ITEM:
                    if (!material.isSolid())
                        newList.add(storageItem);
                    break;
            }
        }
        return newList;
    }
}
