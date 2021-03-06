package me.maskat.ArenaPVP;

import me.maskat.ArenaManager.ArenaPlugin.ArenaInstance;
import me.maskat.ArenaManager.ArenaPlugin.ArenaPlugin;

public class PvP_ArenaPlugin implements ArenaPlugin {

	@Override
	public ArenaInstance getNewInstance() {
		return new PvP_ArenaInstance();
	}
//
//	
//	private enum ScoreBoardTeam {
//		HIDDEN_TAG,
//	}
//	
//	private Team getScoreboardTeam(ScoreBoardTeam scoreBoardTeam) {
//		Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
//		Team sbTeam = score.getTeam(scoreBoardTeam.name());
//		
//		if(sbTeam == null) {
//			sbTeam = score.registerNewTeam(scoreBoardTeam.name());
//			if(scoreBoardTeam==ScoreBoardTeam.HIDDEN_TAG)
//				sbTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
//		}
//		return sbTeam;
//	}
//
//	Map<String, Integer> teamsPoints = new HashMap<>();
//	
//	@Override
//	public void onPrepareArenaAsync(PrepareArenaAsyncEvent e) {
//		for(ArpTeam team : e.getTeams())
//			teamsPoints.put(team.getTeamType(), 0);
//	}
//
//	@Override
//	public void onPreparePlayers(PreparePlayersEvent e) {
//		e.teleportPlayersToRandomLocationsInTeams(true);
//		e.setPlayersGamemodeInTeams(GameMode.ADVENTURE);
//		
//		for(ArpTeam arpTeam : e.getTeams())
//		{
//			for(Player player : arpTeam.getPlayers())
//			{
//				getScoreboardTeam(ScoreBoardTeam.HIDDEN_TAG).addEntry(player.getName());
//				
//				player.getInventory().clear();
//				player.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));
//				player.getInventory().setItem(1, new ItemStack(Material.GOLDEN_APPLE, 1));
//				player.updateInventory();
//			}
//		}
//		
//		e.doStartGame(5);
//	}
//	
//	@Override
//	public void onStartGame(StartGameEvent e) {
//		//e.unfreezePlayers();
//		for(ArpTeam arpTeam : e.getTeams())
//		{
//			for(Player player : arpTeam.getPlayers())
//			{
//				Message.sendTitle(player, "&1Runda "+e.getPlayedRound(), null);
//			}
//		}
//	}
//
//	@Override
//	public void onEndGame(EndGameEvent e) {
//		
//	}
//
//	@Override
//	public void onPlayerDeathInArena(PlayerDeathInArenaEvent e) {
//		e.setRespawnPlayerInArena(false);
//		e.setRespawnWithoutDeathScreen(true);
//		
//		e.getDrops().clear();
//		e.setDroppedExp(0);
//		
//		int teamPoints = teamsPoints.get(e.getPlayerTeam().getTeamType());
//		if(teamPoints < 2)
//		{
//			teamsPoints.put(e.getPlayerTeam().getTeamType(), teamPoints+1);
//			e.setNextRound(true, 5);
//		}
//	}
//	
//
//	@Override
//	public void onPlayerRespawnInArena(PlayerRespawnInArenaEvent e) {
//		e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD, 1));
//	}
//
//	@Override
//	public void onPlayerDamageInArena(PlayerDamageInArenaEvent e) {
//		
//	}
//
//	@Override
//	public void onPlayerLeaveArena(PlayerLeaveArenaEvent e) {
//		getScoreboardTeam(ScoreBoardTeam.HIDDEN_TAG).removeEntry(e.getPlayer().getName());
//	}
}
