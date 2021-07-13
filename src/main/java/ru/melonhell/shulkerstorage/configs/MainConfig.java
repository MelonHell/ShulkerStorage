package ru.melonhell.shulkerstorage.configs;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigValue;
import ru.melonhell.shulkerstorage.Debug;

import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Getter
public class MainConfig {

    private final ConfigManager config;

    @ConfigValue("frame_materials.bottom.vertex")
    private List<Material> materialBottomVertex = ConfigManager.list(Material.class, Material.NETHERITE_BLOCK);
    @ConfigValue("frame_materials.bottom.edge")
    private List<Material> materialBottomEdge = ConfigManager.list(Material.class, Material.LAPIS_BLOCK);
    @ConfigValue("frame_materials.bottom.face")
    private List<Material> materialBottomFace = ConfigManager.list(Material.class, Material.LAPIS_BLOCK);

    @ConfigValue("frame_materials.side.edge")
    private List<Material> materialSideEdge = ConfigManager.list(Material.class, Material.IRON_BLOCK);
    @ConfigValue("frame_materials.side.face")
    private List<Material> materialSideFace = ConfigManager.list(Material.class, Material.GLASS);

    @ConfigValue("frame_materials.top.vertex")
    private List<Material> materialTopVertex = ConfigManager.list(Material.class, Material.REDSTONE_BLOCK);
    @ConfigValue("frame_materials.top.edge")
    private List<Material> materialTopEdge = ConfigManager.list(Material.class, Material.IRON_BLOCK);
    @ConfigValue("frame_materials.top.face")
    private List<Material> materialTopFace = ConfigManager.list(Material.class, Material.GLASS);

    @ConfigValue("limits.width")
    private int limitsWidth = 7;
    @ConfigValue("limits.height")
    private int limitsHeight = 7;

    public MainConfig(Plugin plugin) {
        config = new ConfigManager(plugin)
                .addConverter(Material.class, Material::getMaterial, Material::toString)
                .register(this).saveDefaults().load();
    }
}
