package me.rioeyu.acuPraticeCore.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SpectatorCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public SpectatorCommand(JavaPlugin plugin) {
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
        if (!player.hasPermission("acupracticecore.cmd.spectator")) {
            player.sendMessage("§cBạn không có quyền sử dụng lệnh này.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cSử dụng: /spectator <tên người chơi>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage("§cNgười chơi không online hoặc không tồn tại.");
            return true;
        }

        // Kiểm tra xem người chơi có đang cố gắng spectate chính mình không
        if (target.equals(player)) {
            player.sendMessage("§cBạn không thể theo dõi chính mình!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }

        // Kiểm tra xem người chơi có đang ở world spawn hay không
        World targetWorld = target.getWorld();
        if (targetWorld.getName().equalsIgnoreCase("world") || targetWorld.getName().equalsIgnoreCase("spawn")) {
            // Người chơi đang ở world spawn, từ chối spectate
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent("§cNgười này không trong trận đấu.")
            );

            // Phát âm thanh villager.no
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }

        // Thiết lập spectator cho người chơi
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(target.getLocation());
        player.sendMessage("§aBạn đang theo dõi §e" + target.getName() + "§a.");

        return true;
    }
}