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

        if (params.startsWith("remaining_")) {
            if (data == null) return plugin.getMessage("not-found");
            if (data.isReady()) return plugin.getMessage("ready");
            return String.valueOf(data.getRemainingSeconds());
        }

        if (params.startsWith("time_")) {
            if (data == null) return plugin.getMessage("not-found");
            if (data.isReady()) return plugin.getMessage("ready");
            return data.getFormattedTime(plugin.getTimeFormat());
        }

        if (params.startsWith("status_")) {
            if (data == null) return plugin.getMessage("not-found");
            return data.isReady() ? plugin.getMessage("ready") : plugin.getMessage("counting");
        }

        if (params.startsWith("ready_")) {
            if (data == null) return "false";
            return String.valueOf(data.isReady());
        }

        if (params.equals("config_text")) {
            return plugin.getCountdownText();
        }

        return null;
    }

    private SpawnerData getSpawnerDataFromParams(String params) {
        String spawnerName = params.replaceFirst("^(remaining_|time_|status_|ready_)", "");
        return plugin.getSpawnerMonitor().getSpawnerData(spawnerName);
    }
}
