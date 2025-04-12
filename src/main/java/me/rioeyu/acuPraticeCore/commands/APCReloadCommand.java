package me.rioeyu.acuPraticeCore.commands;

import me.rioeyu.acuPraticeCore.AcuPraticeCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class APCReloadCommand implements CommandExecutor {

    private final AcuPraticeCore plugin;

    public APCReloadCommand(AcuPraticeCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("acupracticecore.cmd.reload")) {
            sender.sendMessage(ChatColor.RED + "Bạn không có quyền sử dụng lệnh này!");
            return true;
        }

        // Tải lại cấu hình từ file
        plugin.reloadConfig();

        // Đảm bảo tất cả cấu hình mặc định được áp dụng sau khi tải lại
        plugin.initializeDefaultConfig();

        sender.sendMessage(ChatColor.GREEN + "Đã tải lại cấu hình AcuPraticeCore!");
        return true;
    }
}