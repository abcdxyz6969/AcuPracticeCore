package me.rioeyu.acuPraticeCore.commands;

import me.rioeyu.acuPraticeCore.AcuPraticeCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

    private final AcuPraticeCore plugin;

    public SpeedCommand(AcuPraticeCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Lệnh này chỉ có thể được sử dụng bởi người chơi!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("acupracticecore.cmd.speed")) {
            player.sendMessage(ChatColor.RED + "Bạn không có quyền sử dụng lệnh này!");
            return true;
        }

        // Kiểm tra max-value từ config
        float maxSpeedValue = (float) plugin.getConfig().getDouble("speed.max-value", 10.0);

        // Cú pháp mới: /speed [walk/fly] <tốc độ>
        if (args.length == 0) {
            // Hiển thị thông tin trợ giúp
            sendHelpMessage(player, maxSpeedValue);
            return true;
        }

        String speedType;
        float speed;

        try {
            if (args.length == 1) {
                // Cách sử dụng cũ: /speed <tốc độ>
                speed = Float.parseFloat(args[0]);
                speedType = player.isFlying() ? "fly" : "walk";
            } else {
                // Cách sử dụng mới: /speed [walk/fly] <tốc độ>
                if (args[0].equalsIgnoreCase("walk") || args[0].equalsIgnoreCase("fly")) {
                    speedType = args[0].toLowerCase();
                    speed = Float.parseFloat(args[1]);
                } else {
                    // Người dùng nhập /speed <tốc độ> [walk/fly]
                    speed = Float.parseFloat(args[0]);
                    speedType = args[1].toLowerCase();

                    if (!speedType.equals("walk") && !speedType.equals("fly")) {
                        player.sendMessage(ChatColor.RED + "Loại tốc độ không hợp lệ. Sử dụng: fly hoặc walk");
                        return true;
                    }
                }
            }

            // Giới hạn tốc độ từ -maxSpeedValue đến maxSpeedValue
            if (speed < -maxSpeedValue || speed > maxSpeedValue) {
                player.sendMessage(ChatColor.RED + "Tốc độ phải nằm trong khoảng từ -" + maxSpeedValue + " đến " + maxSpeedValue + "!");
                return true;
            }

            // Chuyển đổi tốc độ từ -maxSpeedValue đến maxSpeedValue sang -1 đến 1
            float convertedSpeed = speed / maxSpeedValue;

            // Giới hạn tốc độ trong khoảng Minecraft cho phép (-1 đến 1)
            if (convertedSpeed < -1.0f) convertedSpeed = -1.0f;
            if (convertedSpeed > 1.0f) convertedSpeed = 1.0f;

            // Áp dụng tốc độ mới
            if (speedType.equals("fly")) {
                player.setFlySpeed(convertedSpeed);
                player.sendMessage(ChatColor.GREEN + "Đã đặt tốc độ bay thành " + speed);
            } else {
                player.setWalkSpeed(convertedSpeed);
                player.sendMessage(ChatColor.GREEN + "Đã đặt tốc độ đi bộ thành " + speed);
            }

            // Lưu cài đặt tốc độ mặc định vào config
            if (speedType.equals("fly")) {
                plugin.getConfig().set("speed.default-fly", convertedSpeed);
            } else {
                plugin.getConfig().set("speed.default-walk", convertedSpeed);
            }
            plugin.saveConfig();

        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Tốc độ phải là một số!");
            sendHelpMessage(player, maxSpeedValue);
            return true;
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Tốc độ không hợp lệ! Minecraft giới hạn tốc độ từ -1 đến 1.");
            return true;
        }

        return true;
    }

    private void sendHelpMessage(Player player, float maxSpeed) {
        player.sendMessage(ChatColor.YELLOW + "=== Hướng dẫn lệnh Speed ===");
        player.sendMessage(ChatColor.GREEN + "/speed <số>" + ChatColor.WHITE + " - Đặt tốc độ hiện tại (đi bộ hoặc bay)");
        player.sendMessage(ChatColor.GREEN + "/speed walk <số>" + ChatColor.WHITE + " - Đặt tốc độ đi bộ");
        player.sendMessage(ChatColor.GREEN + "/speed fly <số>" + ChatColor.WHITE + " - Đặt tốc độ bay");
        player.sendMessage(ChatColor.YELLOW + "Tốc độ có thể từ -" + maxSpeed + " đến " + maxSpeed);
        player.sendMessage(ChatColor.YELLOW + "Tốc độ mặc định: walk=0.2, fly=0.1");
    }
}