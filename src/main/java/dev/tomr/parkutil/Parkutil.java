package dev.tomr.parkutil;

import dev.tomr.parkutil.data.DataDriver;
import dev.tomr.parkutil.data.MySql;
import dev.tomr.parkutil.data.Sqlite;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.sql.SQLException;

@Plugin(name = "ParkUtil", version = "1.0.0")
@Description(value = "Util Plugin for other Park plugins")
@LoadOrder(value = PluginLoadOrder.POSTWORLD)
@Author(value = "Tom")
@LogPrefix(value = "ParkUtil")
@ApiVersion(value = ApiVersion.Target.v1_13)
public final class Parkutil extends JavaPlugin {
    @Getter
    private static Parkutil parkutil;
    @Getter
    private DataDriver dataDriver;

    @Override
    public void onEnable() {
        parkutil = this;
        FileConfiguration config = this.getConfig();
        this.saveDefaultConfig();
        switch (config.getString("store.type")) {
            case "sqlite":
                if (config.getString("sqlite.filename") != null) {
                    dataDriver = new Sqlite(config.getString("sqlite.filename"), this.getDataFolder().getAbsolutePath());
                } else {
                    disablePlugin();
                }
                break;
            case "mysql":
                if (config.getString("mysql.host") != null &&
                    config.getString("mysql.user") != null &&
                    config.getString("mysql.pass") != null &&
                    config.getString("mysql.database") != null) {
                    dataDriver = new MySql(
                            config.getString("mysql.host"),
                            config.getString("mysql.user"),
                            config.getString("mysql.pass"),
                            config.getString("mysql.database"));
                } else {
                    disablePlugin();
                }
                break;
            default:
                disablePlugin();
                break;
        }
    }

    @Override
    public void onDisable() {
        if (dataDriver != null) {
            try {
                dataDriver.closeConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void disablePlugin() {
        Bukkit.getLogger().severe("ParkUtil was not given a datasource! Disabling ParkUtil");
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
