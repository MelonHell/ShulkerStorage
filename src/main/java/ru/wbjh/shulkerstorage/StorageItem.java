package ru.wbjh.shulkerstorage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
@AllArgsConstructor
public class StorageItem {
    private Material type;
    private int amount;
}
