package mkproject.maskat.BungeeController;

import java.util.Collection;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeController extends Plugin implements Listener {
	@Override
	public void onEnable() {
        //this.getProxy().registerChannel("BungeeCord");
        this.getProxy().registerChannel("papi:globalchat");
        this.getProxy().getPluginManager().registerListener(this, this);
    }
	
	public void sendCustomData(ServerInfo server, String data1, int data2)
	{
	    Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
	    // perform a check to see if globally are no players
	    if (networkPlayers == null || networkPlayers.isEmpty())
	    {
	        return;
	    }
	    ByteArrayDataOutput out = ByteStreams.newDataOutput();
	    out.writeUTF("MySubChannel"); // the channel could be whatever you want
	    out.writeUTF(data1); // this data could be whatever you want
	    out.writeInt(data2); // this data could be whatever you want
	 
	    // we send the data to the server
	    // using ServerInfo the packet is being queued if there are no players in the server
	    // using only the server to send data the packet will be lost if no players are in it
	    server.sendData("papi:globalchat", out.toByteArray());
	    getLogger().info("Sent plugin message to spigot server: "+server.getName());
	}
    
    @EventHandler
    public void onPluginMessageEvent(PluginMessageEvent e) {
        if (!e.getTag().equalsIgnoreCase("papi:globalchat"))
            return;
        
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("OtherSubchannel"))
        {
            // the receiver is a ProxiedPlayer when a server talks to the proxy
            if (e.getReceiver() instanceof ProxiedPlayer)
            {
                ProxiedPlayer receiver = (ProxiedPlayer)e.getReceiver();
                // do things
            }
            // the receiver is a server when the proxy talks to a server
            if (e.getReceiver() instanceof Server)
            {
                Server receiver = (Server)e.getReceiver();
                // do things
            }
        }
    }
   
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(ChatEvent e) {
    	if(e.isCancelled())
    		return;
    	
    	this.getLogger().info("[ChatEvent] "+e.getSender()+": "+e.getMessage());
        for (ServerInfo server : getProxy().getServers().values())
        	sendCustomData(server, "Calling for spigot!", 12);
    }
}