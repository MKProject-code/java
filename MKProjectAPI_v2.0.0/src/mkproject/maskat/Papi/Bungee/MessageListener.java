package mkproject.maskat.Papi.Bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener  {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		Bukkit.getLogger().warning("<<< Papi: onPluginMessageReceived >>> Channel: " + channel);
//		if (!channel.equals("GlobalChatChannel")) {
//			return;
//		}
//		Bukkit.getLogger().warning("<<< onPluginMessageReceived >>>");
//		ByteArrayDataInput in = ByteStreams.newDataInput(message);
//		String subchannel = in.readUTF();
//		Bukkit.getLogger().warning("<<< onPluginMessageReceived - Subchannel: "+subchannel);
//		if (subchannel.equals("SkyChannel")) {
//			short len = in.readShort();
//			byte[] msgbytes = new byte[len];
//			in.readFully(msgbytes);
//
//			DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
//			try {
//				String somedata = msgin.readUTF();
//				Bukkit.getLogger().warning("RESPONSE: "+somedata);
//				short somenumber = msgin.readShort();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} // Read the data in the same way you wrote it
//		}
	}

}
