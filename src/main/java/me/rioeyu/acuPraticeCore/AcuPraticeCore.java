package me.rioeyu.acuPraticeCore;

import me.rioeyu.acuPraticeCore.commands.*;
import me.rioeyu.acuPraticeCore.listeners.*;
import me.rioeyu.acuPraticeCore.tabcomplete.FeedTabCompleter;
import me.rioeyu.acuPraticeCore.tabcomplete.SpeedTabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class AcuPraticeCore extends JavaPlugin {

    @Override
    public void onEnable() {
        // Lưu cấu hình mặc định nếu chưa tồn tại
        saveDefaultConfig();

        // Đảm bảo config được khởi tạo với tất cả giá trị mặc định
        initializeDefaultConfig();

        // Kiểm tra và tạo thư mục config nếu chưa tồn tại
        File configFolder = new File(getDataFolder(), "");
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }

        // Đăng ký các lệnh
        getCommand("spectator").setExecutor(new SpectatorCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("apcreload").setExecutor(new APCReloadCommand(this));
        getCommand("godmode").setExecutor(new GodModeCommand(this));
        getCommand("speed").setExecutor(new SpeedCommand(this));
        getCommand("speed").setTabCompleter(new SpeedTabCompleter());
        getCommand("feed").setExecutor(new FeedCommand(this));
        getCommand("feed").setTabCompleter(new FeedTabCompleter());

        // Đăng ký các sự kiện
        getServer().getPluginManager().registerEvents(new SpectatorListener(this), this);
        getServer().getPluginManager().registerEvents(new GodModeListener(this), this);

        getLogger().info("AcuPraticeCore has been enabled!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        getLogger().info("AcuPraticeCore has been disabled!");
    }

    public void initializeDefaultConfig() {
        FileConfiguration config = getConfig();

        // Đảm bảo tất cả các mục config đều có giá trị mặc định

        // Cấu hình spawn
        if (!config.isSet("spawn.world")) {
            config.set("spawn.world", "world");
            config.set("spawn.x", 0.0);
            config.set("spawn.y", 64.0);
            config.set("spawn.z", 0.0);
            config.set("spawn.yaw", 0.0);
            config.set("spawn.pitch", 0.0);
        }

        // Cấu hình godmode
        if (!config.isSet("godmode.enabled-players")) {
            config.set("godmode.enabled-players", new ArrayList<String>());
        }

        // Cấu hình tốc độ
        if (!config.isSet("speed.default-walk")) {
            config.set("speed.default-walk", 0.2);
            config.set("speed.default-fly", 0.1);
            config.set("speed.max-value", 10);
        }

        // Cấu hình chức năng khác
        if (!config.isSet("features.spectator.enabled")) {
            config.set("features.spectator.enabled", true);
            config.set("features.teleport-on-join", false);
        }

        // Lưu cấu hình
        saveConfig();
    }
}