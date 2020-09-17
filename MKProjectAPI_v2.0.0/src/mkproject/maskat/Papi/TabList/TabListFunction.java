package mkproject.maskat.Papi.TabList;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;
import net.minecraft.server.v1_16_R1.ChatComponentText;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.MinecraftServer;
import net.minecraft.server.v1_16_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R1.PlayerInteractManager;
import net.minecraft.server.v1_16_R1.WorldServer;
import net.minecraft.server.v1_16_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class TabListFunction {	
	protected static void clearTabPlayers(Player player)
	{
		Collection<? extends Player> playersBukkit = Bukkit.getOnlinePlayers();
		// Create array for the nms player objects
		EntityPlayer[] playersNMS = new EntityPlayer[playersBukkit.size()];
		// Add the handle (NMS EntityPlayer object) of every player to the array
		int current = 0;
		for (Player playerOnline : playersBukkit) {
		    playersNMS[current] = ((CraftPlayer) playerOnline).getHandle();
		    current++;
		}
		
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, playersNMS);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	protected static void clearTab(Player player, int id) {
		updateTab(player, id, null);
	}
	protected static void updateTab(Player player, int id, String name) {
		updateTab(player, id, name, -1);
	}
	protected static void updateTab(Player player, int id, String name, int ping)
	{
		// Create profile with a random UUID
		// Important: this name will be used for sorting the tab list on
		// the client side. Use the listName variable in the EntityPlayer
		// class to change the name that is shown! Then you can use
		// this name to order all the entities on the list!
		
		boolean existTabRow = true;
		GameProfile profile = Papi.Model.getPlayer(player).getTabListGameProfilesMap().get(id);
		if(profile == null) {
			profile = new GameProfile(UUID.randomUUID(), getId(id));
			Papi.Model.getPlayer(player).getTabListGameProfilesMap().put(id, profile);
			existTabRow = false;
		}
		// Create profile with a random UUID
		
		//skin icon => ONLY PREMIUM SERVER
		//profile.getProperties().put("textures", new Property("textures", texture, signature));
		
		// Get all server information
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer worldserver = ((CraftWorld) Papi.Server.getSurvivalWorld()).getHandle();
		PlayerInteractManager playerinteractmanager = new PlayerInteractManager(worldserver);
		
		EntityPlayer playerNMS = new EntityPlayer(server, worldserver, profile, playerinteractmanager);
		
		if(name == null)
			playerNMS.listName = new ChatComponentText("");
		else
			playerNMS.listName = new ChatComponentText(Message.getColorMessage(name));
		
		if(ping < 0)
			playerNMS.ping = Integer.MAX_VALUE;
		else
			playerNMS.ping = ping;
		
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(existTabRow ? EnumPlayerInfoAction.UPDATE_DISPLAY_NAME : EnumPlayerInfoAction.ADD_PLAYER, playerNMS);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	private static String getId(int id) {
		return String.format("%03d", id);
	}
}
