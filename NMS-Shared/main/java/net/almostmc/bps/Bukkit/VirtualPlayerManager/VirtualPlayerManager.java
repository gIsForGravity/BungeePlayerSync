package net.almostmc.bps.Bukkit.VirtualPlayerManager;

import org.bukkit.Location;

public interface VirtualPlayerManager {
    void TeleportPlayer(Location newLoc); // Interface so that plugin can be used on multiple versions of Minecraft.
}
