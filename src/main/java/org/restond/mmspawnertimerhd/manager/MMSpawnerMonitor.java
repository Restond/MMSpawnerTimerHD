package org.restond.mmspawnertimerhd.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.restond.mmspawnertimerhd.MMSpawnerTimer;
import org.restond.mmspawnertimerhd.model.SpawnerData;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MMSpawnerMonitor {
    private final MMSpawnerTimer plugin;
    private final Map<String, SpawnerData> spawnerDataMap = new HashMap<>();

    public MMSpawnerMonitor(MMSpawnerTimer plugin) {
        this.plugin = plugin;
    }

    /**
     * 获取MM刷怪点文件信息
     */
    public void reloadFromMythicMobs() {
        File spawnersFolder = new File(plugin.getDataFolder().getParentFile(), "MythicMobs/Spawners");

        if (!spawnersFolder.exists() || !spawnersFolder.isDirectory()) {
            plugin.getLogger().warning("MythicMobs Spawners 文件夹不存在：" + spawnersFolder.getPath());
            return;
        }

        File[] ymlFiles = spawnersFolder.listFiles(((dir, name) -> name.endsWith(".yml")));
        if (ymlFiles == null || ymlFiles.length == 0) {
            plugin.getLogger().info("未找到任何刷怪点配置文件");
            return;
        }

        int loaded = 0;
        for (File file : ymlFiles) {
            String spawnerName = file.getName().replace(".yml", "");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            int cooldown = config.getInt("Cooldown", 0);
            int warmup = config.getInt("Warmup", 0);

            loadSpawner(spawnerName, cooldown, warmup);
            loaded++;
        }
        plugin.getLogger().info("已加载 " + loaded + " 个刷怪点配置");
    }

    /**
     * 记录怪物刷新
     */
    public void recordSpawn(String name, int cooldown, int warmup) {
        SpawnerData existing = spawnerDataMap.get(name);
        if (existing == null) {
            SpawnerData data = new SpawnerData(name, cooldown, warmup);
            data.setWarmupSeconds(warmup);
            spawnerDataMap.put(name, data);
            plugin.getLogger().info("新增刷怪点: " + name + " (冷却: " + cooldown + "秒)");
        } else {
            existing.setCooldownSeconds(cooldown);
            existing.setWarmupSeconds(warmup);
            existing.recordSpawn();
        }
    }

    /**
     * 记录怪物死亡（开始 warmup 倒计时）
     */
    public void recordDeath(String spawnerName) {
        SpawnerData data = spawnerDataMap.get(spawnerName);
        if (data != null) {
            data.recordDeath();
            plugin.getLogger().info("刷怪点 " + spawnerName + " 怪物已死亡，开始预热倒计时");
        }
    }

    /**
     * 从配置文件加载刷怪点信息（不重置计时器）
     */
    public void loadSpawner(String name, int cooldown, int warmup) {
        SpawnerData existing = spawnerDataMap.get(name);
        if (existing == null) {
            SpawnerData data = new SpawnerData(name ,cooldown, warmup);
            data.setWarmupSeconds(warmup);
            spawnerDataMap.put(name, data);
            plugin.getLogger().info("加载刷怪点: " + name + "Warmup: " + warmup + "Cooldown: " + cooldown);
        } else {
            existing.setCooldownSeconds(cooldown);
            existing.setWarmupSeconds(warmup);
        }
    }

    public SpawnerData getSpawnerData(String spawnerName) {
        return spawnerDataMap.get(spawnerName);
    }
}
