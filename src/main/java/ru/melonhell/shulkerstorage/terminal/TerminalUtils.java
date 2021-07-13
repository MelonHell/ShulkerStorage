package ru.melonhell.shulkerstorage.terminal;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.melonhell.shulkerstorage.HeadUtils;
import ru.melonhell.shulkerstorage.ShulkerStorage;
import ru.melonhell.shulkerstorage.configs.MainConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class TerminalUtils {

    final MainConfig config;
    final ShulkerStorage plugin;

    public ItemStack getTerminal() {
        return null;
//        String name = ChatColor.translateAlternateColorCodes('&', config.TERMINAL_NAME + "");
//        List<String> lore = new ArrayList<>();
//        for (String s : config.TERMINAL_LORE) {
//            lore.add(ChatColor.translateAlternateColorCodes('&', s + ""));
//        }
//        ItemStack item = HeadUtils.getHead(config.TERMINAL_TEXTURE, name, lore);
//        ItemMeta meta = item.getItemMeta();
//        PersistentDataContainer pdc = meta.getPersistentDataContainer();
//        NamespacedKey key = new NamespacedKey(plugin, "terminal");
//        pdc.set(key, PersistentDataType.INTEGER, 1);
//        item.setItemMeta(meta);
//        return item;
    }

    public boolean isTerminal(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "terminal");
        Integer value = pdc.get(key, PersistentDataType.INTEGER);
        return value != null && value == 1;
    }

    public void setIsTerminal(Block block, BlockState state) {
        block.setMetadata("isTerminal", new FixedMetadataValue(plugin, state));
    }

    public boolean isTerminal(Block block, BlockState state) {
        List<MetadataValue> meta = block.getMetadata("isTerminal");
        return meta.size() > 0 && Objects.equals(meta.get(0).getOwningPlugin(), plugin) && Objects.equals(meta.get(0).value(), state);
    }

    public boolean isTerminalAndRemove(Block block, BlockState state) {
        List<MetadataValue> meta = block.getMetadata("isTerminal");
        if (meta.size() > 0 && Objects.equals(meta.get(0).getOwningPlugin(), plugin)) {
            if (Objects.equals(meta.get(0).value(), state)) return true;
            block.removeMetadata("isTerminal", plugin);
        }
        return false;
    }
}
