package me.maskat.compasspoint;

import java.util.Set;
import java.util.TreeSet;

import org.bukkit.entity.Player;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

public class PartiesApi {
	private static PartiesAPI api = Parties.getApi();
	
	public static String getPartyName(Player player) {
		return api.getPartyPlayer(player.getUniqueId()).getPartyName(); // Get the player
	}
	
	public static Set<PartyPlayer> getPartyOnlineMembers(String partyName) {
		if (!partyName.isEmpty()) {
		  Party party = api.getParty(partyName); // Get the party by its name
		  if (party != null) {
		    return party.getOnlineMembers(true); // Get the party description
		  }
		}
		return new TreeSet<PartyPlayer>();
	}
	
}
