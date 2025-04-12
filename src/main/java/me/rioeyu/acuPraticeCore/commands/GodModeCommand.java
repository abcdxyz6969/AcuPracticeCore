package me.rioeyu.acuPraticeCore.commands;

import me.rioeyu.acuPraticeCore.AcuPraticeCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GodModeCommand implements CommandExecutor {

    private final AcuPraticeCore plugin;
    private final Map<UUID, Boolean> godModeStatus = new HashMap<>();

    public GodModeCommand(AcuPraticeCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Lệnh này chỉ có thể được sử dụng bởi người chơi!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("acupracticecore.cmd.godmode")) {
            player.sendMessage(ChatColor.RED + "Bạn không có quyền sử dụng lệnh này!");
            return true;
        }

        // Toggle godmode
        boolean isGodMode = !godModeStatus.getOrDefault(player.getUniqueId(), false);
        godModeStatus.put(player.getUniqueId(), isGodMode);

        if (isGodMode) {
            player.sendMessage(ChatColor.GREEN + "Đã bật chế độ bất tử!");
        } else {
            player.sendMessage(ChatColor.RED + "Đã tắt chế độ bất tử!");
        }

        // Cập nhật danh sách người chơi có godmode trong config
        List<String> godmodePlayers = plugin.getConfig().getStringList("godmode.enabled-players");

        if (isGodMode) {
            // Nếu bật godmode, thêm tên vào danh sách nếu chưa có
            if (!godmodePlayers.contains(player.getName())) {
                godmodePlayers.add(player.getName());
            }
        } else {
            // Nếu tắt godmode, xóa tên khỏi danh sách
            godmodePlayers.remove(player.getName());
        }

        // Cập nhật config và lưu
        plugin.getConfig().set("godmode.enabled-players", godmodePlayers);
        plugin.saveConfig();

        return true;
    }

    public boolean isGodMode(Player player) {
        return godModeStatus.getOrDefault(player.getUniqueId(), false);
    }
}