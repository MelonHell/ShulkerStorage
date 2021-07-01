package ru.melonhell.shulkerstorage.configs;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.melonhell.shulkerstorage.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {

    private Main plugin;

    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public List<Material> FRAME_BOTTOM_VERTEX;
    public List<Material> FRAME_BOTTOM_EDGE;
    public List<Material> FRAME_BOTTOM_FACE;
    public List<Material> FRAME_SIDE_EDGE;
    public List<Material> FRAME_SIDE_FACE;
    public List<Material> FRAME_TOP_VERTEX;
    public List<Material> FRAME_TOP_EDGE;
    public List<Material> FRAME_TOP_FACE;
    public int LIMITS_WIDTH;
    public int LIMITS_HEIGHT;

    private List<Material> getMaterialList(String path) {
        if (!customConfig.isList(path)) {
            String materialName = customConfig.getString(path);
            if (materialName == null) return new ArrayList<>();
            return Collections.singletonList(Material.getMaterial(materialName));
        } else {
            List<Material> materialList = new ArrayList<>();
            for (String s : customConfig.getStringList(path)) {
                materialList.add(Material.getMaterial(s));
            }
            return materialList;
        }
    }

    public void reload() throws IOException {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!customConfigFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = new InputStreamReader(plugin.getResource("config.yml"), "UTF8");
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        customConfig.setDefaults(defConfig);

        FRAME_BOTTOM_VERTEX = getMaterialList("frame_materials.bottom.vertex");
        FRAME_BOTTOM_EDGE = getMaterialList("frame_materials.bottom.edge");
        FRAME_BOTTOM_FACE = getMaterialList("frame_materials.bottom.face");
        FRAME_SIDE_EDGE = getMaterialList("frame_materials.side.edge");
        FRAME_SIDE_FACE = getMaterialList("frame_materials.side.face");
        FRAME_TOP_VERTEX = getMaterialList("frame_materials.top.vertex");
        FRAME_TOP_EDGE = getMaterialList("frame_materials.top.edge");
        FRAME_TOP_FACE = getMaterialList("frame_materials.top.face");

        LIMITS_WIDTH = customConfig.getInt("limits.width");
        LIMITS_HEIGHT = customConfig.getInt("limits.height");

    }

    public Config (Main plugin) {
       this.plugin = plugin;
        try {
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
