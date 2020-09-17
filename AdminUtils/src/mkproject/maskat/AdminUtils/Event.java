package mkproject.maskat.AdminUtils;

import java.time.LocalDateTime;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;

import mkproject.maskat.AdminUtils.Database.Bans.BanInfo;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {
//	
//	@EventHandler
//	public void onChunkLoad(ChunkLoadEvent e) {
//		for(Entity entity : e.getChunk().getEntities())
//		{
//			if(entity.getType() == EntityType.ARROW)
//				entity.remove();
//		}
//	}
	
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
    	
    	String addrIP = e.getAddress().getHostAddress();
    	
		if(Database.Bans.ignoreCheckBan(e.getName()))
			return;
    	
		BanInfo banInfo = Database.Bans.checkBan(e.getName(), addrIP);
		if(banInfo != null)
		{
			if(banInfo.isPermament() || banInfo.getDatetimeEnd() == null)
			{
				e.disallow(Result.KICK_BANNED, Message.getColorMessage(Message.getColorMessage("&cZostałeś zbanowany na zawsze!"+((banInfo.getReason()!=null && banInfo.getReason().length()>0) ? ("\n&6Powód: &e"+banInfo.getReason()) : ""))));
				return;
			}
			
			if(banInfo.getDatetimeEnd().isAfter(LocalDateTime.now()))
			{
				e.disallow(Result.KICK_BANNED, Message.getColorMessage("&cZostałeś zbanowany! Pozostało jeszcze "+Papi.Function.getRemainingTimeString(banInfo.getDatetimeEnd())+((banInfo.getReason()!=null && banInfo.getReason().length()>0) ? ("\n&6Powód: &e"+banInfo.getReason()) : "")));
				return;
			}
		}
		
		//////////////////////////////////////////////////////////////////////////////
		String addrIP_minusOne = addrIP.substring(0, addrIP.lastIndexOf('.'));
		
		banInfo = Database.Bans.checkBan(e.getName(), addrIP_minusOne+".*");
		if(banInfo != null)
		{
			if(banInfo.isPermament() || banInfo.getDatetimeEnd() == null)
			{
				e.disallow(Result.KICK_BANNED, Message.getColorMessage(Message.getColorMessage("&cZostałeś zbanowany na zawsze!"+((banInfo.getReason()!=null && banInfo.getReason().length()>0) ? ("\n&6Powód: &e"+banInfo.getReason()) : ""))));
				return;
			}
			
			if(banInfo.getDatetimeEnd().isAfter(LocalDateTime.now()))
			{
				e.disallow(Result.KICK_BANNED, Message.getColorMessage("&cZostałeś zbanowany! Pozostało jeszcze "+Papi.Function.getRemainingTimeString(banInfo.getDatetimeEnd())+((banInfo.getReason()!=null && banInfo.getReason().length()>0) ? ("\n&6Powód: &e"+banInfo.getReason()) : "")));
				return;
			}
		}
		
		//////////////////////////////////////////////////////////////////////////////
		String addrIP_minusTwo = addrIP_minusOne.substring(0, addrIP_minusOne.lastIndexOf('.'));
		
		banInfo = Database.Bans.checkBan(e.getName(), addrIP_minusTwo+".*.*");
		if(banInfo != null)
		{
			if(banInfo.isPermament() || banInfo.getDatetimeEnd() == null)
			{
				e.disallow(Result.KICK_BANNED, Message.getColorMessage(Message.getColorMessage("&cZostałeś zbanowany na zawsze!"+((banInfo.getReason()!=null && banInfo.getReason().length()>0) ? ("\n&6Powód: &e"+banInfo.getReason()) : ""))));
				return;
			}
			
			if(banInfo.getDatetimeEnd().isAfter(LocalDateTime.now()))
			{
				e.disallow(Result.KICK_BANNED, Message.getColorMessage("&cZostałeś zbanowany! Pozostało jeszcze "+Papi.Function.getRemainingTimeString(banInfo.getDatetimeEnd())+((banInfo.getReason()!=null && banInfo.getReason().length()>0) ? ("\n&6Powód: &e"+banInfo.getReason()) : "")));
				return;
			}
		}
		
		//////////////////////////////////////////////////////////////////////////////
		String addrIP_minusThree = addrIP_minusTwo.substring(0, addrIP_minusTwo.lastIndexOf('.'));
		
		banInfo = Database.Bans.checkBan(e.getName(), addrIP_minusThree+".*.*.*");
		if(banInfo != null)
		{
			if(banInfo.isPermament() || banInfo.getDatetimeEnd() == null)
			{
				e.disallow(Result.KICK_BANNED, Message.getColorMessage(Message.getColorMessage("&cZostałeś zbanowany na zawsze!"+((banInfo.getReason()!=null && banInfo.getReason().length()>0) ? ("\n&6Powód: &e"+banInfo.getReason()) : ""))));
				return;
			}
			
			if(banInfo.getDatetimeEnd().isAfter(LocalDateTime.now()))
			{
				e.disallow(Result.KICK_BANNED, Message.getColorMessage("&cZostałeś zbanowany! Pozostało jeszcze "+Papi.Function.getRemainingTimeString(banInfo.getDatetimeEnd())+((banInfo.getReason()!=null && banInfo.getReason().length()>0) ? ("\n&6Powód: &e"+banInfo.getReason()) : "")));
				return;
			}
		}
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PlayerJoinEvent e) {

		
		LocalDateTime muteDatetimeEnd = Database.Mutes.checkMuted(e.getPlayer());
		if(muteDatetimeEnd != null && muteDatetimeEnd.isAfter(LocalDateTime.now()))
			Papi.Model.getPlayer(e.getPlayer()).setMuted(muteDatetimeEnd);
	}
}
