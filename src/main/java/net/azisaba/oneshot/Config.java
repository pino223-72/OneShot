package net.azisaba.oneshot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class Config {
    private YamlConfiguration config;

    private String weapon;
    private World world;
    private Location room;
    private Location spawnA;
    private Location spawnB;
    private Location winner;

    public void load() {
        saveDefaultConfigIfNotExists();

        weapon = config.getString("weapon", "pythonOS");
        world = Bukkit.getWorld(config.getString("world", "lobby"));
        room = this.getLocation("room");
        spawnA = this.getLocation("spawnA");
        spawnB = this.getLocation("spawnB");
        winner = this.getLocation("winner");
    }

    public void saveDefaultConfigIfNotExists() {
        File configFile = new File(OneShot.getInstance().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            InputStream defaultStream = OneShot.getInstance().getResource("config.yml");
            if (defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
                config.setDefaults(defaultConfig);

                try {
                    config.save(configFile);
                } catch (IOException ex) {
                    OneShot.getInstance().getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
                }
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private Location getLocation(String path) {
        return new Location(
                world,
                config.getDouble(path + ".x", 0.0),
                config.getDouble(path + ".y", 0.0),
                config.getDouble(path + ".z", 0.0),
                config.getInt(path + ".yaw", 0),
                config.getInt(path + ".pitch", 0)
        );
    }

    public String getWeapon() {
        return weapon;
    }

    public World getWorld() {
        return world;
    }

    public Location getRoom() {
        return room;
    }

    public Location getSpawnA() {
        return spawnA;
    }

    public Location getSpawnB() {
        return spawnB;
    }

    public Location getWinner() {
        return winner;
    }
}
