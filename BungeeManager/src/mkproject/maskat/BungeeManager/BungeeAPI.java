package mkproject.maskat.BungeeManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import mkproject.maskat.Papi.Utils.Message;

public class BungeeAPI {
	
	public enum Channel {
		GlobalChat("GlobalChatChannel");
		
		private String channel;
		 
	    Channel(String channel) {
	    	this.channel = channel;
		}
	 
	    public String getString() {
	        return channel;
	    }
	}
	
	public static void registerOutgoingPluginChannel(JavaPlugin javaPlugin, String channel) {
		javaPlugin.getServer().getMessenger().registerOutgoingPluginChannel(javaPlugin, channel);
	}
	public static void registerIncomingPluginChannel(JavaPlugin javaPlugin, String channel, PluginMessageListener pluginMessageListener) {
		javaPlugin.getServer().getMessenger().registerIncomingPluginChannel(javaPlugin, channel, pluginMessageListener);
	}
	
	public static boolean connectPlayer(Player player, String server) {
		Message.sendTitle(player, "&aŁączenie...", null, 0, 1, 2);
		BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Message.sendTitle(player, "&aŁączenie...", null, 0, 40, 2);
			} 
		}, 0L, 42L);
		
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(server);
			player.sendPluginMessage(Plugin.getPlugin(), "BungeeCord", out.toByteArray());
		}
		catch(Exception ex) {
			task.cancel();
			return false;
		}
		task.cancel();
		return true;
	}
	public static boolean forward(String server, String channel) {
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward"); // So BungeeCord knows to forward it
			out.writeUTF("ALL");
			out.writeUTF(channel); // The channel name to check if this your data

			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeUTF("Some kind of data here"); // You can do anything you want with msgout
			msgout.writeShort(123);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			Plugin.getPlugin().getServer().sendPluginMessage(Plugin.getPlugin(), "BungeeCord", out.toByteArray());
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
	public static boolean forwardPlayer(Player player, String server, String channel) {
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("ForwardToPlayer");
			out.writeUTF(server);
			out.writeUTF(channel);

			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeUTF("Some kind of data here");
			msgout.writeShort(123);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			Plugin.getPlugin().getServer().sendPluginMessage(Plugin.getPlugin(), "BungeeCord", out.toByteArray());
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
}
