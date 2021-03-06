package me.maskat.ArenaManager.ArenaPlugin;

public interface ArenaInstance {
	public void onPrepareArenaAsync(PrepareArenaAsyncEvent event);
	public void onPreparePlayersToArenaEvent(PreparePlayersToArenaEvent event);
	public void onStartArenaGame(StartArenaGameEvent event);
	public void onEndArenaGame(EndArenaGameEvent event);
	public void onAbortArenaGameEvent(AbortArenaGameEvent event);
//	public void onCancelGame(ModelArena modelArena);
	public void onPlayerDeathInArena(PlayerDeathInArenaEvent event);
	public void onPlayerRespawnInArena(PlayerRespawnInArenaEvent event);
	public void onPlayerDamageInArena(PlayerDamageInArenaEvent event);
//	public void onPlayerClickObject(ModelArena modelArena, ModelArenaObjectsGroup modelArenaObjectsGroup, ModelArenaObject modelArenaObject);
//	public void onPlayerStayInObject(ModelArena modelArena, ModelArenaObjectsGroup modelArenaObjectsGroup, ModelArenaObject modelArenaObject);
	public void onPlayerLeaveArena(PlayerLeaveArenaEvent event);
}
