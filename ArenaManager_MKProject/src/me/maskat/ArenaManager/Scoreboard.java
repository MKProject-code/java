//package me.maskat.ArenaManager;
//
//import org.bukkit.Bukkit;
//import org.bukkit.scoreboard.Objective;
//import org.bukkit.scoreboard.RenderType;
//import org.bukkit.scoreboard.Score;
//import org.bukkit.scoreboard.ScoreboardManager;
//import org.bukkit.scoreboard.Team;
//
//public class Scoreboard {
//	Scoreboard board = null;
//	
//	
//	ScoreboardManager scoremanager = Bukkit.getScoreboardManager();
//	Scoreboard board = scoremanager.getNewScoreboard();
//	Team team = board.registerNewTeam("teamname");
//	
//    //Adding players
//    team.addEntry("entity");
//     
//    //Removing players
//    //team.removePlayer(player);
//     
//    //Adding prefixes (shows up in player list before the player's name, supports ChatColors)
//    team.setPrefix("prefix");
//     
//    //Adding suffixes (shows up in player list after the player's name, supports ChatColors)
//    team.setSuffix("suffix");
//     
//    //Setting the display name
//    team.setDisplayName("display name");
//     
//    //Making invisible players on the same team have a transparent body
//    team.setCanSeeFriendlyInvisibles(true);
//     
//    //Making it so players can't hurt others on the same team
//    team.setAllowFriendlyFire(false);
//    
//    Objective objective = board.registerNewObjective("test", "dummy", "ziomek", RenderType.HEARTS);
//    
//    //Setting where to display the scoreboard/objective (either SIDEBAR, PLAYER_LIST or BELOW_NAME)
//    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//     
//    //Setting the display name of the scoreboard/objective
//    objective.setDisplayName("Display Name");
//    
//    Score score = objective.getScore(manager.getPlayer().getName());
//    score.setScore(42); //Integer only!
//    
//    score = objective.getScore(ChatColor.GREEN + "Kills:"); //Get a fake offline player
//    score.setScore(1);
//    
//    manager.getPlayer().setScoreboard(board);
//	
//	return false;
//}
