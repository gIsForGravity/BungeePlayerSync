package net.almostmc.bps.Bukkit;

import net.almostmc.bps.Bukkit.VirtualPlayerManager.VirtualPlayerManager;
import net.almostmc.bps.Bukkit.VirtualPlayerManager.VirtualPlayerManager_1152;
import net.almostmc.bps.Bukkit.VirtualPlayerManager.VirtualPlayerManager_1161;
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

    public VirtualPlayer(UUID uuid, Location spawnLoc) { // Used to create a fake player
        this.uuid = uuid;
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid); // Get the player that owns that uuid
        username = op.getName();

        vPlayers.put(uuid, this);

        switch (BukkitPlugin.apiVersion) { // Spawn player using Bukkit api version
            case "v1_16_R1":
                vpm = VirtualPlayerManager_1161.SpawnPlayer(op, spawnLoc);
                break;
            case "v1_15_R1":
                vpm = VirtualPlayerManager_1152.SpawnPlayer(op, spawnLoc);
                break;
        }
    }

    public static VirtualPlayer GetVirtualPlayer(UUID uuid) {
        return vPlayers.get(uuid);
    }

    public void Move(Location newLocation) { // Move VirtualPlayer to new location
        vpm.TeleportPlayer(newLocation);
    }

    public void Destroy() {
        vPlayers.remove(uuid);
    }

    public static VirtualPlayer GetVPlayer(UUID uuid) { return vPlayers.get(uuid); }
    public static VirtualPlayer GetVPlayer(String uuid) { return vPlayers.get(UUID.fromString(uuid)); }
}
