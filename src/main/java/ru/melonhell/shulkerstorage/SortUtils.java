package ru.melonhell.shulkerstorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils {

    public enum SortType {
        ID,
        AMOUNT,
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
        }
        if (reverse) Collections.reverse(storageItemList);
    }
}