package ru.melonhell.shulkerstorage.gui;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@UtilityClass
public class GuiItemCreator {

    public ItemStack getEmptyItem() {
        ItemStack itemStack = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Пусто");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getScrollUpItem() {
        ItemStack itemStack = new ItemStack(Material.WHITE_BANNER);
        BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
        meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
        meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setDisplayName(ChatColor.RESET + "Прокрутка вверх");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "ПКМ: Предыдущая страница"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getScrollDownItem() {
        ItemStack itemStack = new ItemStack(Material.WHITE_BANNER);
        BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
        meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
        meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setDisplayName(ChatColor.RESET + "Прокрутка вниз");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "ПКМ: Следующая страница"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getInfoItem(int slotsCount, int emptySlotsCount) {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Инфо");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Свободно слотов: " + emptySlotsCount + " из " + slotsCount));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getSortItem(SortUtils.SortType sortType, boolean sortReverse) {
        ItemStack itemStack = new ItemStack(Material.COMPARATOR);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Сортировка");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Тип: " + sortType.name(), ChatColor.GRAY + "Наоборот: " + sortReverse));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getFilterItem(FilterUtils.FilterType filterType) {
        ItemStack itemStack = new ItemStack(Material.HOPPER);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Фильтр");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Тип: " + filterType));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
