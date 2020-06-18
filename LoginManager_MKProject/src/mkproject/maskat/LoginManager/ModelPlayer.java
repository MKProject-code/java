package mkproject.maskat.LoginManager;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import mkproject.maskat.Papi.Papi;

public class ModelPlayer {
	private boolean registered;
	private boolean registered_byAuthme;
	private boolean executedFirstCmd;
	private ItemStack[] beforeInventoryContents;
	private ItemStack[] beforeInventoryArmorContents;
	private Location beforeJoinSpawnLocation;
	private Entity beforeVehicle;
	private Location beforeVehicleLocation;
	//private Class<Entity> beforeVehicleClass;
	private float beforeExp;
	private GameMode beforeGameMode;
	private boolean beforeDead;
	private Vector beforeVelocity;
//	private PlayerInventory beforeInventory;
	private Collection<PotionEffect> beforeActivePotionEffects;
	private boolean isAferLogin;
	private boolean isQuitBeforeLogin;
	private boolean isVehicleScheduler;
	private boolean isTeleportedWhenIsLogged;
	
	public ModelPlayer(Player p) {
		registered = Papi.MySQL.exists(
				Papi.SQL.getWhereObject(Database.Users.NAME, "=", p.getName().toLowerCase()),
				Database.Users.TABLE);
		
		registered_byAuthme = false;
		
		if(registered == false) {
			if(Papi.MySQL.exists(
					Papi.SQL.getWhereObject("username", "=", p.getName().toLowerCase()),
					"authme"))
			{
				registered = true;
				registered_byAuthme = true;
			}
		}
		
		executedFirstCmd = false;
		beforeVehicle = null;
		beforeGameMode = null;
		beforeDead = false;
		isAferLogin = false;
		isQuitBeforeLogin = false;
		isVehicleScheduler = false;
		isTeleportedWhenIsLogged = false;
	}
	public boolean isTeleportedWhenIsLogged() {
		return isTeleportedWhenIsLogged;
	}
	public void setTeleportedWhenIsLogged(boolean b) {
		isTeleportedWhenIsLogged = b;
	}
	protected boolean isVehicleScheduler() {
		return isVehicleScheduler;
	}
	protected void setVehicleScheduler(boolean b) {
		isVehicleScheduler = b;
	}
	protected boolean isQuitBeforeLogin() {
		return isQuitBeforeLogin;
	}
	protected void setQuitBeforeLogin(boolean b) {
		isQuitBeforeLogin = b;
	}
	
	protected boolean isAferLogin() {
		return isAferLogin;
	}
	
	protected void setAferLogin(boolean b) {
		isAferLogin = b;
	}
	
	protected boolean isRegistered() { 
		return registered;
	}
	protected boolean isRegisteredByAuthMe() { 
		return registered_byAuthme;
	}

	public boolean isExecutedFirstCmd() {
		return executedFirstCmd;
	}

	public void registerExecuteCmd() {
		executedFirstCmd = true;
	}

	public void setInventoryContents(ItemStack[] inventory) {
		beforeInventoryContents = inventory;
	}
	
	public void setInventoryArmorContents(ItemStack[] inventory) {
		beforeInventoryArmorContents = inventory;
	}
	
	public ItemStack[] getInventoryContents() {
		return beforeInventoryContents;
	}
	
	public ItemStack[] getInventoryArmorContents() {
		return beforeInventoryArmorContents;
	}
	
	public void setJoinSpawnLocation(Location location) {
		beforeJoinSpawnLocation = location;
	}
	
	public Location getJoinSpawnLocation() {
		return beforeJoinSpawnLocation;
	}

	public void setVehicle(Entity vehicle) {
		beforeVehicle = vehicle;
	}
	
	public Entity getVehicle() {
		return beforeVehicle;
	}

	public void setVehicleLocation(Location location) {
		beforeVehicleLocation = location;
	}
	
	public Location getVehicleLocation() {
		return beforeVehicleLocation;
	}

//	public void setVehicleClass(Class<Entity> newInstance) {
//		beforeVehicleClass = newInstance;
//	}
//	
//	public Class<Entity> getVehicleClass() {
//		return beforeVehicleClass;
//	}

	public void setExp(float exp) {
		beforeExp = exp;
	}
	public float getExp() {
		return beforeExp;
	}
	public void setGameMode(GameMode gameMode) {
		beforeGameMode = gameMode;
	}
	public GameMode getGameMode() {
		return beforeGameMode;
	}

	public void setDead(boolean b) {
		beforeDead = b;
	}
	
	public boolean isDead() {
		return beforeDead;
	}

	public void setVelocity(Vector velocity) {
		beforeVelocity = velocity;
	}
	public Vector getVelocity() {
		return beforeVelocity;
	}

	public void setActivePotionEffects(Collection<PotionEffect> activePotionEffects) {
		beforeActivePotionEffects = activePotionEffects;
	}
	public Collection<PotionEffect> getActivePotionEffects() {
		return beforeActivePotionEffects;
	}

//	public void setInventory(PlayerInventory inventory) {
//		beforeInventory = inventory;
//	}
//	public PlayerInventory getInventory() {
//		return beforeInventory;
//	}
}
