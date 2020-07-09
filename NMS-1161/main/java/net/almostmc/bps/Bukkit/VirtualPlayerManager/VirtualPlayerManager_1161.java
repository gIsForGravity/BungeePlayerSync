package net.almostmc.bps.Bukkit.VirtualPlayerManager;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VirtualPlayerManager_1161 implements VirtualPlayerManager {

    private EntityPlayer npc;
    private WorldServer world;

    public static VirtualPlayerManager SpawnPlayer(OfflinePlayer op, Location originalLoc) { // Returns VirtualPlayerManager interface so plugin can be used on multiple versions of Minecraft
        VirtualPlayerManager_1161 vpm = new VirtualPlayerManager_1161(); // Create Virtual Player Manager

        // Get server and world that fake player will be on
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();

        // Create fake player and set its location
        vpm.npc = new EntityPlayer(server, world, new GameProfile(op.getUniqueId(), op.getName()), new PlayerInteractManager(world));
        vpm.npc.setLocation(originalLoc.getX(), originalLoc.getY(), originalLoc.getZ(), originalLoc.getYaw(), originalLoc.getPitch());

        // Send fake player to all connected players
        for (Player all : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) all).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, vpm.npc));
        }

        return vpm; // Return VirtualPlayerManager as an interface instead of a class
    }

    public void TeleportPlayer(Location newLoc) { // Teleports this player manager to another location
        ((Player) npc).teleport(newLoc); // Cast EntityPlayer to Bukkit Player and teleport it
    }

    private VirtualPlayerManager_1161() {} // VPM creation only allowed through SpawnPlayer(...)
}
