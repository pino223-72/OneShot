package net.azisaba.oneshot;

import net.azisaba.oneshot.commands.OneShotCommand;
import net.azisaba.oneshot.game.Game;
import net.azisaba.oneshot.game.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class OneShot extends JavaPlugin {
    private static OneShot instance;
    private Config config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        config = new Config();
        config.load();

        getCommand("oneshot").setExecutor(new OneShotCommand());
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Config getMainConfig() {
        return config;
    }

    public static OneShot getInstance() {
        return instance;
    }
}
