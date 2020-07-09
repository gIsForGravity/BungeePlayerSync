package net.almostmc.bps;

import net.almostmc.bps.Bukkit.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {
    public static BukkitPlugin instance;
    public static final String pluginVersion = "BPS-0.1.0_1";

    public static String apiVersion;

    @Override
    public void onEnable() {
        instance = this;
        apiVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Messenger());
    }
}
