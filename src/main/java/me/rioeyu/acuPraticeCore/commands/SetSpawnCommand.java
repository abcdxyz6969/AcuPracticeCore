package me.rioeyu.acuPraticeCore.commands;

import me.rioeyu.acuPraticeCore.AcuPraticeCore;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final AcuPraticeCore plugin;

    public SetSpawnCommand(AcuPraticeCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cLệnh này chỉ có thể được sử dụng bởi người chơi.");
            return true;
        }

        Player player = (Player) sender;
        
        // Kiểm tra quyền
        if (!player.hasPermission("acupracticecore.cmd.setspawn")) {
            player.sendMessage("§cBạn không có quyền sử dụng lệnh này.");
            return true;
        }

        Location location = player.getLocation();
        plugin.getConfig().set("spawn.location", location);
        plugin.saveConfig();

        player.sendMessage("§aĐã đặt vị trí spawn mới tại vị trí hiện tại của bạn.");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
        
        return true;
    }
}