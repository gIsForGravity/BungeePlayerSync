package net.almostmc.bps.Bukkit;

import net.almostmc.bps.Bukkit.VirtualPlayerManager.VirtualPlayerManager;
import net.almostmc.bps.Bukkit.VirtualPlayerManager.VirtualPlayerManager_116_R1;
import net.almostmc.bps.BukkitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

public class VirtualPlayer {
    private static HashMap<UUID, VirtualPlayer> vPlayers = new HashMap<>();

    public final String username;
    public final UUID uuid;

    private VirtualPlayerManager vpm;

    public VirtualPlayer(UUID uuid, Location spawnLoc) {
        this.uuid = uuid;
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        username = op.getName();

        vPlayers.put(uuid, this);

        switch (BukkitPlugin.apiVersion) {
            case "v1_16_R1":
                vpm = VirtualPlayerManager_116_R1.SpawnPlayer(op, spawnLoc);
                break;
        }
    }

    public void Move(Location newLocation) {
        vpm.TeleportPlayer(newLocation);
    }

    public void Destroy() {
        vPlayers.remove(uuid);
    }

    public static VirtualPlayer GetVPlayer(UUID uuid) { return vPlayers.get(uuid); }
    public static VirtualPlayer GetVPlayer(String uuid) { return vPlayers.get(UUID.fromString(uuid)); }
}
