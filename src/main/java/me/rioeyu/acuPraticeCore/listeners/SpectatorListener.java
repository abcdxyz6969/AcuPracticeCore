package me.rioeyu.acuPraticeCore.listeners;

import me.rioeyu.acuPraticeCore.AcuPraticeCore;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpectatorListener implements Listener {

    private final AcuPraticeCore plugin;

    public SpectatorListener(AcuPraticeCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Nếu người chơi đang ở chế độ Spectator khi thoát
        if (player.getGameMode() == GameMode.SPECTATOR) {
            // Lưu vị trí trước khi vào spectator (nếu có)
            if (player.hasMetadata("previous_location")) {
                player.teleport((org.bukkit.Location) player.getMetadata("previous_location").get(0).value());
                player.removeMetadata("previous_location", plugin);
            }
            
            // Đặt lại game mode thành Survival trước khi thoát
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}