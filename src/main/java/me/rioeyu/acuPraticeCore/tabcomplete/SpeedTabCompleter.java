package me.rioeyu.acuPraticeCore.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        if (command.getName().equalsIgnoreCase("speed")) {
            if (args.length == 1) {
                List<String> suggestions = new ArrayList<>();
                // Thêm loại tốc độ
                suggestions.add("walk");
                suggestions.add("fly");
                // Thêm các giá trị tốc độ phổ biến
                suggestions.add("0.5");
                suggestions.add("1");
                suggestions.add("2");
                suggestions.add("3");
                suggestions.add("5");
                suggestions.add("10");
                return suggestions;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("walk") || args[0].equalsIgnoreCase("fly")) {
                    // Nếu tham số đầu tiên là walk hoặc fly, gợi ý các giá trị tốc độ
                    return Arrays.asList("0.5", "1", "2", "3", "5", "10");
                } else {
                    // Nếu tham số đầu tiên là giá trị tốc độ, gợi ý loại tốc độ
                    return Arrays.asList("walk", "fly");
                }
            }
        }

        return new ArrayList<>();
    }
}