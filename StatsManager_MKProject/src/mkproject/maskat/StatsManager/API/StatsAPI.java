package mkproject.maskat.StatsManager.API;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.StatsManager.Database;
import mkproject.maskat.StatsManager.Model.Model;
import mkproject.maskat.StatsManager.Model.StatsPlayer;
import mkproject.maskat.StatsManager.Model.StatsPlayerOffline;

public class StatsAPI {
	public static StatsPlayer getStatsPlayer(Player player) {
		return Model.getStatsPlayer(player);
	}
	
	public static List<StatsPlayerOffline> getTopPlayersKills() {
		ResultSet rs = Papi.MySQL.getResultSetAllOrderBy(5, List.of(Database.Users.REAL_NAME,Database.Users.KILLS,Database.Users.DEATHS,Database.Users.LAST_KILLER), Database.Users.KILLS, Papi.SQL.OrderType.DESC, Database.Users.TABLE);
		
		List<StatsPlayerOffline> statsPlayersOffline = new ArrayList<>();
		if(rs!=null)
		{
			try {
				while(rs.next()) {
					statsPlayersOffline.add(new StatsPlayerOffline(
							rs.getString(Database.Users.REAL_NAME),
							rs.getInt(Database.Users.KILLS),
							rs.getInt(Database.Users.DEATHS),
							rs.getString(Database.Users.LAST_KILLER)
						));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return statsPlayersOffline;
	}
}
