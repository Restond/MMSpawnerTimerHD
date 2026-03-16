package org.restond.mmspawnertimerhd;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.restond.mmspawnertimerhd.command.reload;
import org.restond.mmspawnertimerhd.listener.MMListener;
import org.restond.mmspawnertimerhd.manager.MMSpawnerMonitor;
import org.restond.mmspawnertimerhd.placeholder.MMPlaceholder;

public final class MMSpawnerTimer extends JavaPlugin {

    private MMSpawnerMonitor spawnerMonitor;
    private String timeFormat;
    private String messageReady;
    private String messageWarmup;
    private String messageCooldown;
    private String messageNotFound;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("未找到 PlaceholderAPI，插件已禁用！");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
            getLogger().warning("未找到 MythicMobs，部分功能可能不可用");
        }

        saveDefaultConfig();
        loadConfig();

        this.spawnerMonitor = new MMSpawnerMonitor(this);
        this.spawnerMonitor.reloadFromMythicMobs();

        Bukkit.getPluginManager().registerEvents(new MMListener(this), this);
        new MMPlaceholder(this).register();

        getCommand("mmspawntimer").setExecutor(new reload(this));

        getLogger().info("MMSpawnerTimerHD v" + getDescription().getVersion() + " 已成功加载！");
    }

    @Override
    public void onDisable() {
        getLogger().info("MMSpawnerTimerHD 已卸载");
    }

    public void loadConfig() {
        timeFormat = getConfig().getString("time-format", "mm:ss");

        messageWarmup = getConfig().getString("messages.warmup", "预热中");
        messageCooldown = getConfig().getString("messages.cooldown", "冷却中");
        messageReady = getConfig().getString("messages.ready", "已刷新");
        messageNotFound = getConfig().getString("messages.not-found", "N/A");
    }

    public MMSpawnerMonitor getSpawnerMonitor() {
        return spawnerMonitor;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public String getMessage(String key) {
        switch (key) {
            case "ready":
                return messageReady;
            case "warmup":
                return messageWarmup;
            case "cooldown":
                return messageCooldown;
            case "not-found":
                return messageNotFound;
            default:
                return "N/A";
        }
    }
}
