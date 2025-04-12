package me.rioeyu.acuPraticeCore.listeners;

import me.rioeyu.acuPraticeCore.AcuPraticeCore;
import me.rioeyu.acuPraticeCore.commands.GodModeCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class GodModeListener implements Listener {

    private final AcuPraticeCore plugin;

    public GodModeListener(AcuPraticeCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (plugin.getServer().getPluginManager().getPlugin("AcuPraticeCore") != null) {
            GodModeCommand godModeCommand = new GodModeCommand(plugin);
            if (godModeCommand.isGodMode(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Khôi phục trạng thái godmode từ config
        List<String> godmodePlayers = plugin.getConfig().getStringList("godmode.enabled-players");
        if (godmodePlayers.contains(player.getName())) {
            GodModeCommand godModeCommand = new GodModeCommand(plugin);
            // Gọi onCommand để thiết lập lại godmode
            godModeCommand.onCommand(player, null, "godmode", new String[0]);
        }
    }
}