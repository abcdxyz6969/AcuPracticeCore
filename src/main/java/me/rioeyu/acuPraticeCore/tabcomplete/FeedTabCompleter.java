package me.rioeyu.acuPraticeCore.tabcomplete;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedTabCompleter implements TabCompleter {
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (command.getName().equalsIgnoreCase("feed")) {
            if (args.length == 1) {
                if (sender.hasPermission("acupracticecore.cmd.feed.others")) {
                    // Thêm "all" vào danh sách gợi ý nếu có quyền feed tất cả
                    if (sender.hasPermission("acupracticecore.cmd.feed.all")) {
                        completions.add("all");
                    }
                    
                    // Thêm tên tất cả người chơi online vào danh sách gợi ý
                    completions.addAll(Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .collect(Collectors.toList()));
                    
                    // Lọc các gợi ý dựa trên đầu vào của người dùng
                    if (!args[0].isEmpty()) {
                        return completions.stream()
                                .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                }
                return completions;
            }
        }
        
        return completions;
    }
}