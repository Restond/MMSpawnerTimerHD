package org.restond.mmspawnertimerhd.listener;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.restond.mmspawnertimerhd.MMSpawnerTimer;

public class MMListener implements Listener {
    private final MMSpawnerTimer plugin;

    public MMListener(MMSpawnerTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawnerSpawn(MythicMobSpawnEvent event) {
        Entity entity = event.getEntity();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isDead() || !entity.isValid()) {
                    plugin.getLogger().warning("实体已无效，跳过处理");
                    return;
                }

                ActiveMob activeMob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(entity);
                if (activeMob == null) {
                    plugin.getLogger().warning("延迟后 activeMob 仍为 null");
                    return;
                }

                MythicSpawner spawner = activeMob.getSpawner();
                if (spawner == null) {
                    plugin.getLogger().warning("spawner 为 null，可能不是刷怪点生成");
                    return;
                }

                String spawnerName = spawner.getName();
                int cooldown = spawner.getCooldownSeconds();

                plugin.getSpawnerMonitor().recordSpawn(spawnerName, cooldown);
            }
        }.runTaskLater(plugin, 1L);
    }
}
