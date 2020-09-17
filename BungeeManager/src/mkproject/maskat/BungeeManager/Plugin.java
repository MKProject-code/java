package mkproject.maskat.BungeeManager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class Plugin extends JavaPlugin implements PluginMessageListener {
	private static Plugin plugin;
	
	public void onEnable() {
        if (!checkIfBungee())
            return;
        
		plugin = this;
        
		//CommandListener executeCommand = new CommandListener();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
//	    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//	    this.getServer().getMessenger().registerIncomingPluginChannel(this, "SkyChannel", this);
//		
//		initCommand("bungee", new CmdBungee(), false);
//		
		Config.initialize();
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	private void initCommand(String command, Object listener, boolean tabCompleter) {
		getCommand(command).setExecutor((CommandExecutor)listener);
		if(tabCompleter)
			getCommand(command).setTabCompleter((TabCompleter)listener);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		System.out.println("hi");
		Bukkit.getLogger().warning("<<< onPluginMessageReceived >>>");
		if (!channel.equals("SkyChannel")) {
			return;
		}
		Bukkit.getLogger().warning("<<< onPluginMessageReceived - SkyChannel >>>");
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		Bukkit.getLogger().warning("<<< onPluginMessageReceived - SkyChannel channel: "+subchannel);
		if (subchannel.equals("SkyChannel")) {
			short len = in.readShort();
			byte[] msgbytes = new byte[len];
			in.readFully(msgbytes);

			DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
			try {
				String somedata = msgin.readUTF();
				Bukkit.getLogger().warning("RESPONSE: "+somedata);
				short somenumber = msgin.readShort();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Read the data in the same way you wrote it
			
		}
	}
	
    // we check like that if the specified server is BungeeCord.
    private boolean checkIfBungee()
    {
        // we check if the server is Spigot/Paper (because of the spigot.yml file)
        if ( !getServer().getVersion().contains( "Spigot" ) && !getServer().getVersion().contains( "Paper" ) )
        {
            getLogger().severe( "You probably run CraftBukkit... Please update atleast to spigot for this to work..." );
            getLogger().severe( "Plugin disabled!" );
            getServer().getPluginManager().disablePlugin( this );
            return false;
        }
        
        if ( getServer().spigot().getConfig().getConfigurationSection("settings").getBoolean( "settings.bungeecord" ) )
        {
            getLogger().severe( "This server is not BungeeCord." );
            getLogger().severe( "If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell." );
            getLogger().severe( "Plugin disabled!" );
            getServer().getPluginManager().disablePlugin( this );
            return false;
        }
        return true;
    }
}
