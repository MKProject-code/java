package mkproject.maskat.Papi.Bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import mkproject.maskat.Papi.PapiPlugin;
import mkproject.maskat.Papi.Utils.Message;

public class PapiBungee {
	
    // we check like that if the specified server is BungeeCord.
    public static boolean checkIfBungeeEnabled()
    {
        // we check if the server is Spigot/Paper (because of the spigot.yml file)
        if (!PapiPlugin.getPlugin().getServer().getVersion().contains("Spigot") && !PapiPlugin.getPlugin().getServer().getVersion().contains("Paper"))
        {
        	PapiPlugin.getPlugin().getLogger().severe("You probably run CraftBukkit... Please update atleast to spigot for this to work...");
        	PapiPlugin.getPlugin().getLogger().severe("Plugin disabled!");
        	PapiPlugin.getPlugin().getServer().getPluginManager().disablePlugin(PapiPlugin.getPlugin());
            return false;
        }
        
        if (PapiPlugin.getPlugin().getServer().spigot().getConfig().getConfigurationSection("settings").getBoolean("settings.bungeecord"))
        {
        	PapiPlugin.getPlugin().getLogger().severe("This server is not BungeeCord.");
        	PapiPlugin.getPlugin().getLogger().severe("If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell.");
        	PapiPlugin.getPlugin().getLogger().severe("Plugin disabled!");
        	PapiPlugin.getPlugin().getServer().getPluginManager().disablePlugin(PapiPlugin.getPlugin());
            return false;
        }
        return true;
    }
	
	public static void registerOutgoingPluginChannel(JavaPlugin javaPlugin, BungeeChannel BungeChannel) {
		javaPlugin.getServer().getMessenger().registerOutgoingPluginChannel(javaPlugin, BungeChannel.getChannelOutgoing());
	}
	public static void registerIncomingPluginChannel(JavaPlugin javaPlugin, BungeeChannel BungeChannel, PluginMessageListener pluginMessageListener) {
		javaPlugin.getServer().getMessenger().registerIncomingPluginChannel(javaPlugin, BungeChannel.getChannelIncoming(), pluginMessageListener);
	}
	
	public static boolean connectPlayer(Player player, BungeeServer BungeeServer) {
		Message.sendTitle(player, "&aŁączenie...", null, 0, 1, 2);
		BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(PapiPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Message.sendTitle(player, "&aŁączenie...", null, 10, 70, 20);
			}
		}, 0L, 100L);
		
		PapiPlugin.getPlugin().getLogger().info("Player '"+player.getName()+"' try connect to: "+BungeeServer.toString());
		
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(BungeeServer.toString());
			player.sendPluginMessage(PapiPlugin.getPlugin(), "BungeeCord", out.toByteArray());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			task.cancel();
			return false;
		}
		task.cancel();
		return true;
	}
	public static boolean forward(BungeeServer BungeeServer, BungeeChannel BungeChannel) {
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward"); // So BungeeCord knows to forward it
			out.writeUTF("ALL");
			out.writeUTF(BungeChannel.toString()); // The channel name to check if this your data

			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeUTF("Some kind of data here"); // You can do anything you want with msgout
			msgout.writeShort(123);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			PapiPlugin.getPlugin().getServer().sendPluginMessage(PapiPlugin.getPlugin(), "BungeeCord", out.toByteArray());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean forwardPlayer(Player player, BungeeServer BungeeServer, BungeeChannel BungeChannel) {
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("ForwardToPlayer");
			out.writeUTF(BungeeServer.toString());
			out.writeUTF(BungeChannel.toString());

			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeUTF("Some kind of data here");
			msgout.writeShort(123);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			PapiPlugin.getPlugin().getServer().sendPluginMessage(PapiPlugin.getPlugin(), "BungeeCord", out.toByteArray());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
