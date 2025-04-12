package me.rioeyu.acuPraticeCore.commands;

import me.rioeyu.acuPraticeCore.AcuPraticeCore;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SpawnCommand implements CommandExecutor {

    private final AcuPraticeCore plugin;

    public SpawnCommand(AcuPraticeCore plugin) {
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
        if (!player.hasPermission("acupracticecore.cmd.spawn")) {
            player.sendMessage("§cBạn không có quyền sử dụng lệnh này.");
            return true;
        }

        Location spawnLoc = plugin.getConfig().getLocation("spawn.location");
        if (spawnLoc == null) {
            player.sendMessage("§cChưa có vị trí spawn được đặt. Hãy sử dụng /setspawn để đặt spawn.");
            return true;
        }

        // Lấy cấu hình teleport delay từ config
        int delay = plugin.getConfig().getInt("spawn.teleport-delay", -1);
        String messageType = plugin.getConfig().getString("spawn.message-type", "chat").toLowerCase();
        String message = plugin.getConfig().getString("spawn.message", "§aBạn đã được dịch chuyển đến spawn.");

        if (delay <= 0) {
            // Teleport ngay lập tức
            player.teleport(spawnLoc);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            sendMessage(player, messageType, message);
        } else {
            // Thông báo chờ
            sendMessage(player, messageType, "§eBạn sẽ được dịch chuyển đến spawn sau " + delay + " giây.");

            // Tạo task teleport trễ
            new BukkitRunnable() {
                private int countdown = delay;

                @Override
                public void run() {
                    if (!player.isOnline()) {
                        this.cancel();
                        return;
                    }

                    if (countdown <= 0) {
                        player.teleport(spawnLoc);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        sendMessage(player, messageType, message);
                        this.cancel();
                        return;
                    }

                    // Hiển thị countdown
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            new TextComponent("§eTeleport trong §c" + countdown + "§e giây...")
                    );

                    countdown--;
                }
            }.runTaskTimer(plugin, 0L, 20L); // Chạy mỗi giây (20 ticks)
        }

        return true;
    }

    private void sendMessage(Player player, String type, String message) {
        if (type.equals("actionbar")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        } else {
            player.sendMessage(message);
        }
    }
}