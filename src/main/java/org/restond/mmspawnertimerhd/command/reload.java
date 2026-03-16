package org.restond.mmspawnertimerhd.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.restond.mmspawnertimerhd.MMSpawnerTimer;

public class reload implements CommandExecutor {
    private final MMSpawnerTimer plugin;

    public reload (MMSpawnerTimer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args ) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("mmspawntimer.reload")) {
                sender.sendMessage(ChatColor.RED + "没有权限");
                return true;
            }

            plugin.loadConfig();
            plugin.getSpawnerMonitor().reloadFromMythicMobs();
            sender.sendMessage(ChatColor.GREEN + "MMSpawnerTimerHD 配置已重载");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "/mmspawntimer reload - 重载配置");
        return true;
    }
}
