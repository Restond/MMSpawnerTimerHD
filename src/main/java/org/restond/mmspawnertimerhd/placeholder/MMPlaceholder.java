package org.restond.mmspawnertimerhd.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.restond.mmspawnertimerhd.MMSpawnerTimer;
import org.restond.mmspawnertimerhd.model.SpawnerData;

public class MMPlaceholder extends PlaceholderExpansion {
    private final MMSpawnerTimer plugin;

    public MMPlaceholder(MMSpawnerTimer plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "mmspawner";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "Restond";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        SpawnerData data = getSpawnerDataFromParams(params);

        // cooldown 剩余秒数
        if (params.startsWith("cooldown_remaining_")) {
            if (data == null) return plugin.getMessage("not-found");
            int remaining = data.getCooldownRemainingSeconds();
            if (remaining <= 0) return plugin.getMessage("ready");
            return String.valueOf(remaining);
        }

        // cooldown 格式化时间
        if (params.startsWith("cooldown_time_")) {
            if (data == null) return plugin.getMessage("not-found");
            int remaining = data.getCooldownRemainingSeconds();
            if (remaining <= 0) return plugin.getMessage("ready");
            return SpawnerData.formatTime(remaining, plugin.getTimeFormat());
        }

        // warmup 剩余秒数
        if (params.startsWith("warmup_remaining_") || params.startsWith("remaining_")) {
            if (data == null) return plugin.getMessage("not-found");
            int remaining = data.getWarmupRemainingSeconds();
            if (remaining <= 0) return plugin.getMessage("ready");
            return String.valueOf(remaining);
        }

        // warmup 格式化时间
        if (params.startsWith("warmup_time_") || params.startsWith("time_")) {
            if (data == null) return plugin.getMessage("not-found");
            int remaining = data.getWarmupRemainingSeconds();
            if (remaining <= 0) return plugin.getMessage("ready");
            return SpawnerData.formatTime(remaining, plugin.getTimeFormat());
        }

        // 状态
        if (params.startsWith("status_")) {
            if (data == null) return plugin.getMessage("not-found");
            if (!data.isWarmupReady()) return plugin.getMessage("warmup");
            if (!data.isCooldownReady()) return plugin.getMessage("cooldown");
            return plugin.getMessage("ready");
        }

        // 是否就绪
        if (params.startsWith("ready_")) {
            if (data == null) return "false";
            return String.valueOf(data.isWarmupReady());
        }

        // warmup 总秒数
        if (params.startsWith("warmup_")) {
            if (data == null) return plugin.getMessage("not-found");
            return String.valueOf(data.getWarmupSeconds());
        }

        // cooldown 总秒数
        if (params.startsWith("cooldown_")) {
            if (data == null) return plugin.getMessage("not-found");
            return String.valueOf(data.getCooldownSeconds());
        }

        return null;
    }

    private SpawnerData getSpawnerDataFromParams(String params) {
        String spawnerName = params.replaceFirst("^(status_|ready_|warmup_remaining_|warmup_time_|cooldown_remaining_|cooldown_time_|warmup_|cooldown_)", "");
        return plugin.getSpawnerMonitor().getSpawnerData(spawnerName);
    }
}
