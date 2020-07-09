package net.almostmc.bps.Bukkit;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.almostmc.bps.BukkitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class Messenger implements PluginMessageListener { // Manages RPCs and stuff
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("Forward")) {
            String version = in.readUTF();
            if (version.equals(BukkitPlugin.pluginVersion)) {
                switch (in.readUTF()) { // Command
                    case "sendLocationMessage": {
                        // SendLocationMessage RPC
                        UUID playerUUID = UUID.fromString(in.readUTF());
                        Location playerLocation = new Location(Bukkit.getWorlds().get(0), // World 0
                                in.readDouble(), in.readDouble(), in.readDouble(), // X, Y, and Z
                                in.readFloat(), in.readFloat());
                        VirtualPlayer.GetVirtualPlayer(playerUUID).Move(playerLocation);
                    } break;

                    case "sendCreationMessage": {
                        // SendCreationMessage
                        UUID playerUUID = UUID.fromString(in.readUTF());
                        Location playerLocation = new Location(Bukkit.getWorlds().get(0), // World 0
                                in.readDouble(), in.readDouble(), in.readDouble(), // X, Y, and Z
                                in.readFloat(), in.readFloat());
                        new VirtualPlayer(playerUUID, playerLocation);
                    } break;
                }
            }
        }
    }

    /**
     * Sends location of Player to other servers using player's current location.
     * @param player the player who's location will be sent to other servers
     */
    public static void sendLocationMessage(Player player) {
        sendLocationMessage(player.getUniqueId().toString(), player.getLocation());
    }

    public static void sendLocationMessage(Player player, Location location) {
        sendLocationMessage(player.getUniqueId().toString(), location);
    }

    public static void sendLocationMessage(String uuid, Location location) {
        Object[] args = new Object[6]; // Used for message arguments

        args[0] = uuid; // Tell server the UUID of fake player
        args[1] = location.getX(); // Tell remote server location and rotation of fake player
        args[2] = location.getY();
        args[3] = location.getZ();
        args[4] = location.getYaw();
        args[5] = location.getPitch();

        sendBungeeRPC("sendLocationMessage", args); // Run RPC sendLocationMessage
    }

    // Create new fake player on remote server
    public static void sendCreationMessage(String uuid, Location location) {
        Object[] args = new Object[6]; // Used for message arguments

        args[0] = uuid; // Tell server the UUID of fake player
        args[1] = location.getX(); // Tell remote server location and rotation of fake player
        args[2] = location.getY();
        args[3] = location.getZ();
        args[4] = location.getYaw();
        args[5] = location.getPitch();

        sendBungeeRPC("sendCreationMessage", args); // Run RPC sendCreationMessage
    }

    private static void sendBungeeRPC(String rpc, Object[] args) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward"); // Bungeecord 'Forward' command
        out.writeUTF("ALL"); // Send to ALL servers (except this one)
        out.writeUTF(BukkitPlugin.pluginVersion); // Current version of BungeePlayerSync
        out.writeUTF(rpc); // Tell other server which RPC to run

        for (Object o : args) {
            if (o instanceof String) out.writeUTF((String) o);
            if (o instanceof Double) out.writeDouble((Double) o);
            if (o instanceof Float) out.writeFloat((Float) o);
        }

        Player transport = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        transport.sendPluginMessage(BukkitPlugin.instance, "BungeeCord", out.toByteArray());
    }
}
