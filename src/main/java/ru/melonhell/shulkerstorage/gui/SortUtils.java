package ru.melonhell.shulkerstorage.gui;

import ru.melonhell.shulkerstorage.storage.StorageItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils {

    public enum SortType {
        ID,
        AMOUNT,
        SLOTS,
        NAME,
    }

    public static void sort(List<StorageItem> storageItemList, SortType sortType, boolean reverse) {
        switch (sortType) {
            case ID:
                storageItemList.sort(Comparator.comparing(storageItem -> storageItem.getItemStack().getType()));
                break;
            case AMOUNT:
                storageItemList.sort(Comparator.comparingInt(storageItem -> -storageItem.getAmount()));
                break;
            case NAME:
                storageItemList.sort(Comparator.comparing(storageItem -> storageItem.getItemStack().getType().name()));
                break;
            case SLOTS:
                storageItemList.sort(Comparator.comparingInt(storageItem -> -storageItem.getSlots()));
                break;
        }
        if (reverse) Collections.reverse(storageItemList);
    }
}
