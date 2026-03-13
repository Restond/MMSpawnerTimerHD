package org.restond.mmspawnertimerhd.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.restond.mmspawnertimerhd.MMSpawnerTimer;
import org.restond.mmspawnertimerhd.model.SpawnerData;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MMSpawnerMonitor {
    private final MMSpawnerTimer plugin;
    private final Map<String, SpawnerData> spawnerDataMap = new HashMap<>();
    private BukkitRunnable updateTask;

    public MMSpawnerMonitor(MMSpawnerTimer plugin) {
        this.plugin = plugin;
    }

    public void recordSpawn(String name, int cooldown) {
        SpawnerData existing = spawnerDataMap.get(name);
        if (existing == null) {
            spawnerDataMap.put(name, new SpawnerData(name, cooldown));
            plugin.getLogger().info("新增刷怪点: " + name + " (冷却: " + cooldown + "秒)");
        } else {
            existing.setCooldownSeconds(cooldown);
            existing.resetTimer();
        }
    }

    public void startMonitoring() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateCountdown();
            }
        };
        updateTask.runTaskTimer(plugin, 20L, 20L);
        plugin.getLogger().info("刷怪点监控已启动");
    }

    public void stopMonitoring() {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
            plugin.getLogger().info("刷怪点监控已停止");
        }
    }

    public SpawnerData getSpawnerData(String name) {
        return spawnerDataMap.get(name);
    }

    public void updateCountdown() {
        for (SpawnerData data : spawnerDataMap.values()) {
            data.updateRemainingSeconds();
        }
    }

    public void saveData() {
        File file = new File(plugin.getDataFolder(), "data.yml");
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<String, SpawnerData> entry : spawnerDataMap.entrySet()) {
            String name = entry.getKey();
            SpawnerData data = entry.getValue();

            config.set("spawners." + name + ".lastSpawn", data.getLastSpawnTime());
            config.set("spawners." + name + ".cooldown", data.getCooldownSeconds());
        }

        try {
            config.save(file);
            plugin.getLogger().info("刷怪点数据已保存 (" + spawnerDataMap.size() + " 个)");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "保存刷怪点数据失败: " + e.getMessage(), e);
        }
    }

    public void loadData() {
        File file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists()) {
            plugin.getLogger().info("未找到数据文件，跳过加载");
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.contains("spawners")) {
            int loaded = 0;
            for (String name : config.getConfigurationSection("spawners").getKeys(false)) {
                long lastSpawn = config.getLong("spawners." + name + ".lastSpawn");
                int cooldown = config.getInt("spawners." + name + ".cooldown");

                SpawnerData data = new SpawnerData(name, cooldown);
                data.setLastSpawnTime(lastSpawn);
                spawnerDataMap.put(name, data);
                loaded++;
            }
            plugin.getLogger().info("已加载 " + loaded + " 个刷怪点数据");
        }
    }
}
