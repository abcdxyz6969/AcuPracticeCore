package me.rioeyu.acuPraticeCore.commands;

import me.rioeyu.acuPraticeCore.AcuPraticeCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    private final AcuPraticeCore plugin;

    public FeedCommand(AcuPraticeCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Kiểm tra quyền sử dụng lệnh
        if (!sender.hasPermission("acupracticecore.cmd.feed")) {
            sender.sendMessage(ChatColor.RED + "Bạn không có quyền sử dụng lệnh này!");
            return true;
        }

        // Nếu không có tham số, người dùng tự feed bản thân (nếu là player)
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Lệnh này cần chỉ định một người chơi khi sử dụng từ console!");
                return true;
            }

            Player player = (Player) sender;
            feedPlayer(player, player);
            return true;
        }

        // Kiểm tra quyền feed người khác
        if (args.length == 1) {
            if (!sender.hasPermission("acupracticecore.cmd.feed.others")) {
                sender.sendMessage(ChatColor.RED + "Bạn không có quyền feed người chơi khác!");
                return true;
            }

            // Tìm người chơi theo tên
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Không tìm thấy người chơi: " + args[0]);
                return true;
            }

            // Feed người chơi đã chỉ định
            feedPlayer(sender, target);
            return true;
        }

        // Feed tất cả người chơi (nếu có quyền)
        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
            if (!sender.hasPermission("acupracticecore.cmd.feed.all")) {
                sender.sendMessage(ChatColor.RED + "Bạn không có quyền feed tất cả người chơi!");
                return true;
            }

            int feedCount = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setFoodLevel(20); // 20 là mức độ đói tối đa
                player.setSaturation(20f); // Đặt saturation cao để không đói nhanh
                player.sendMessage(ChatColor.GREEN + "Bạn đã được làm đầy thanh đói bởi " + sender.getName());
                feedCount++;
            }

            sender.sendMessage(ChatColor.GREEN + "Đã feed " + feedCount + " người chơi!");
            return true;
        }

        // Hiển thị cách sử dụng nếu cú pháp không đúng
        sender.sendMessage(ChatColor.YELLOW + "Sử dụng: /" + label + " [player|all]");
        return true;
    }

    private void feedPlayer(CommandSender sender, Player target) {
        // Kiểm tra xem mức đói đã đầy chưa
        if (target.getFoodLevel() == 20) {
            if (sender == target) {
                sender.sendMessage(ChatColor.YELLOW + "Thanh đói của bạn đã đầy rồi!");
            } else {
                sender.sendMessage(ChatColor.YELLOW + "Thanh đói của " + target.getName() + " đã đầy rồi!");
            }
            return;
        }

        // Feed người chơi
        target.setFoodLevel(20); // 20 là mức độ đói tối đa
        target.setSaturation(20f); // Đặt saturation cao để không đói nhanh

        // Thông báo
        if (sender == target) {
            target.sendMessage(ChatColor.GREEN + "Bạn đã làm đầy thanh đói của mình!");
        } else {
            target.sendMessage(ChatColor.GREEN + "Thanh đói của bạn đã được làm đầy bởi " + sender.getName());
            sender.sendMessage(ChatColor.GREEN + "Đã làm đầy thanh đói cho " + target.getName());
        }
    }
}