package me.maskat.TradeManager.PlayerMenu;

import org.bukkit.entity.Player;

public class TradePlayer {
	private Player player;
	private PlayerMenu playerMenu = null;
	private boolean trading = false;
	private Player destPlayer;
	private boolean accepted = false;
	private boolean decided = false;
	
	private boolean closingMenu = false;

	
	public TradePlayer(Player player, Player destPlayer) {
		this.player = player;
		this.destPlayer = destPlayer;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean isOpenedMenu() {
		return (this.playerMenu == null) ? false : true;
	}
	
	public boolean isClosingMenu() {
		return this.closingMenu;
	}
	
	public boolean isTrading() {
		return this.trading;
	}

	public Player getDestinationPlayer() {
		return this.destPlayer;
	}

	public void doTrading() {
		this.trading = true;
		this.playerMenu = new PlayerMenu(this);
	}

	public PlayerMenu getTradePlayerMenu() {
		return this.playerMenu;
	}
	
	public void closeTradeMenu() {
		this.closingMenu = true;
		TradeManager.endTrade(this);
	}
	
	public void endTradeMenu() {
		TradeManager.endTrade(this);
	}
	
	public boolean isAccepted() {
		return this.accepted;
	}
	
	public boolean isDecided() {
		return this.decided;
	}

	public void setAccepted(boolean accepted) {
		this.decided = false;
		this.accepted = accepted;
		playerMenu.setAccepted(true,accepted);
		TradePlayer tradePlayerDestination = Models.getTradePlayer(this.destPlayer);
		tradePlayerDestination.getTradePlayerMenu().setAccepted(false, accepted);
		
		if(this.accepted && tradePlayerDestination.isAccepted())
		{
			this.endTradeMenu();
		}
	}

	public void setDecide() {
		this.accepted = false;
		this.decided = true;
		this.playerMenu.setDecided(true);
		TradePlayer tradePlayerDestination = Models.getTradePlayer(this.destPlayer);
		tradePlayerDestination.getTradePlayerMenu().setDecided(false);
	}

}
