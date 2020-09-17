package mkproject.maskat.Papi.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mkproject.maskat.Papi.PapiHeadBase64;
import mkproject.maskat.Papi.PapiPlugin;
import mkproject.maskat.Papi.Utils.Message;

public class PapiMenuPage implements InventoryHolder {

	private Inventory inv;
	private Map<InventorySlot, Object> slotStoreObjectMap = new HashMap<>();
	
	private Collection<Player> playersOpened = new ArrayList<>();
	
	private PapiMenu papiMenuExecutor;
	private boolean emptySlotsSendEvent;
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
	public PapiMenuPage(PapiMenu papiMenuExecutor, int numberOfRows, final String colorTitle) {
		this.PapiMenuPageConstructor(papiMenuExecutor, numberOfRows, colorTitle, false);
	}
	public PapiMenuPage(PapiMenu papiMenuExecutor, int numberOfRows, final String colorTitle, final boolean emptySlotsSendEvent) {
		this.PapiMenuPageConstructor(papiMenuExecutor, numberOfRows, colorTitle, emptySlotsSendEvent);
	}
	
	private void PapiMenuPageConstructor(PapiMenu papiMenuExecutor, int numberOfRows, final String colorTitle, final boolean emptySlotsSendEvent) {
		this.papiMenuExecutor = papiMenuExecutor;
		this.emptySlotsSendEvent = emptySlotsSendEvent;
		
		if(numberOfRows < 1)
			numberOfRows = 1;
		else if(numberOfRows > 6)
			numberOfRows = 6;
		
		this.inv = Bukkit.createInventory(this, numberOfRows*9, Message.getColorMessage(colorTitle));
	}
	
	public boolean openMenu(Player player)
	{
		if(player.isOnline()) {
			playersOpened.add(player);
			player.openInventory(this.inv);
			return true;
		}
		else
			return false;
	}
	
	public void closeMenu(Player player)
	{
		playersOpened.remove(player);
		if(player.isOnline() && player.getOpenInventory().getTopInventory() == inv)
				player.closeInventory();
	}
	
	public void closeMenuForAll()
	{
		if(playersOpened.size()<=0)
			return;
		
		for(Player playerOpened : playersOpened) {
			if(playerOpened.isOnline() && playerOpened.getOpenInventory().getTopInventory() == inv)
				playerOpened.closeInventory();
		}
		this.playersOpened = new ArrayList<>();
	}
	
	public boolean isAlreadyPlayerOpenedMenu(Player player)
	{
		if(player.getOpenInventory().getTopInventory() == inv)
			return playersOpened.contains(player);
		return false;
	}
	
	public Collection<Player> getPlayersOpenedMenu()
	{
		return playersOpened;
	}
	
	public void clearItems()
	{
		this.inv.clear();
	}
	
	public void removeItem(final InventorySlot menuInvSlot)
	{
		this.setItem(menuInvSlot, new ItemStack(Material.AIR));
		
		//TODO: check if below code work! Probably is better solution
		//this.inv.clear(menuInvSlot.getValue());
	}
	
	public void setItem(final InventorySlot menuInvSlot, final Material material, final String colorNameAndLore)
	{
		setItem(menuInvSlot, material, false, colorNameAndLore);
	}
	public void setItem(final InventorySlot menuInvSlot, final Material material, final boolean withGlow, final String colorNameAndLore)
	{
        inv.setItem(menuInvSlot.getValue(), MenuUtils.createGuiItem(material, withGlow, Message.getColorMessage(colorNameAndLore)));
	}
	
	public void setItem(final InventorySlot menuInvSlot, final ItemStack itemStack)
	{
		inv.setItem(menuInvSlot.getValue(), itemStack);
	}
	
	public ItemStack getItem(final InventorySlot menuInvSlot)
	{
		return inv.getItem(menuInvSlot.getValue());
	}
	
	public void setItemNameAndLore(final InventorySlot menuInvSlot, final String colorNameAndLore)
	{
		inv.setItem(menuInvSlot.getValue(), MenuUtils.createGuiItem(this.getItem(menuInvSlot).getType(), Message.getColorMessage(colorNameAndLore)));
	}
	
	public void setItemMaterial(final InventorySlot menuInvSlot, final Material material)
	{
		ItemStack itemStack = inv.getItem(menuInvSlot.getValue());
		itemStack.setType(material);
		inv.setItem(menuInvSlot.getValue(), itemStack);
	}
	public void setItemName(final InventorySlot menuInvSlot, final String colorName)
	{
		ItemStack itemStack = inv.getItem(menuInvSlot.getValue());
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(Message.getColorMessage(colorName));
		itemStack.setItemMeta(itemMeta);
		inv.setItem(menuInvSlot.getValue(), itemStack);
	}
	public void setItemLore(final InventorySlot menuInvSlot, final String colorLore)
	{
		ItemStack itemStack = inv.getItem(menuInvSlot.getValue());
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setLore(Arrays.asList(Message.getColorMessage(colorLore).split("\n")));
		itemStack.setItemMeta(itemMeta);
		inv.setItem(menuInvSlot.getValue(), itemStack);
	}
	public void setItemLore(final InventorySlot menuInvSlot, final List<String> colorLoreList)
	{
		for(int i=0;i<colorLoreList.size();i++)
			colorLoreList.set(i, Message.getColorMessage(colorLoreList.get(i)));
		
		ItemStack itemStack = inv.getItem(menuInvSlot.getValue());
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setLore(colorLoreList);
		itemStack.setItemMeta(itemMeta);
		inv.setItem(menuInvSlot.getValue(), itemStack);
	}
	public void setItemLoreLine(final InventorySlot menuInvSlot, int line, final String colorLore)
	{
		ItemStack itemStack = inv.getItem(menuInvSlot.getValue());
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> loreList = itemMeta.getLore();
		if(loreList==null)
			loreList = new ArrayList<>();
		if(loreList.size() < line)
		{
			for(int i=loreList.size();i<line;i++)
				loreList.add("");
		}
		loreList.set(line-1, Message.getColorMessage(colorLore));
		itemMeta.setLore(loreList);
		itemStack.setItemMeta(itemMeta);
		inv.setItem(menuInvSlot.getValue(), itemStack);
	}
	public void addItemLoreLine(final InventorySlot menuInvSlot, final String colorLore)
	{
		ItemStack itemStack = inv.getItem(menuInvSlot.getValue());
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> loreList = itemMeta.getLore();
		if(loreList==null)
			loreList = new ArrayList<>();
		loreList.add(Message.getColorMessage(colorLore));
		itemMeta.setLore(loreList);
		itemStack.setItemMeta(itemMeta);
		inv.setItem(menuInvSlot.getValue(), itemStack);
	}
	public String getItemName(final InventorySlot menuInvSlot)
	{
		return inv.getItem(menuInvSlot.getValue()).getItemMeta().getDisplayName();
	}
	public List<String> getItemLore(final InventorySlot menuInvSlot)
	{
		return inv.getItem(menuInvSlot.getValue()).getItemMeta().getLore();
	}
	public Material getItemMaterial(final InventorySlot menuInvSlot)
	{
		return inv.getItem(menuInvSlot.getValue()).getType();
	}

	public void setItem(final InventorySlot menuInvSlot, final Material material, final String colorNameAndLore, Object slotStoreObject)
	{
		this.setItem(menuInvSlot, material, false, colorNameAndLore, slotStoreObject);
	}
	public void setItem(final InventorySlot menuInvSlot, final Material material, final boolean withGlow, final String colorNameAndLore, Object slotStoreObject)
	{
		this.slotStoreObjectMap.put(menuInvSlot, slotStoreObject);
		this.setItem(menuInvSlot, material, withGlow, colorNameAndLore);
	}
	
	public void setItem(final InventorySlot menuInvSlot, final ItemStack itemStack, Object slotStoreObject)
	{
		this.slotStoreObjectMap.put(menuInvSlot, slotStoreObject);
		this.setItem(menuInvSlot, itemStack);
	}
	
	public void setItemHeadAsync(final InventorySlot menuInvSlot, final Player player, final String colorNameAndLore)
	{
		inv.setItem(menuInvSlot.getValue(), MenuUtils.createGuiItem(Material.PLAYER_HEAD, Message.getColorMessage(colorNameAndLore)));
		Bukkit.getScheduler().runTaskAsynchronously(PapiPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				inv.setItem(menuInvSlot.getValue(), MenuUtils.createGuiItemHead(player, Message.getColorMessage(colorNameAndLore)));
			}
		});
	}
	
	public void setItemHeadAsync(final InventorySlot menuInvSlot, final Player player, final String colorNameAndLore, Object slotStoreObject)
	{
		this.slotStoreObjectMap.put(menuInvSlot, slotStoreObject);
		this.setItemHeadAsync(menuInvSlot, player, colorNameAndLore);
	}

	public void onInventoryClick(final InventoryClickEvent e, final Player player) {
		if(!this.emptySlotsSendEvent && e.getCurrentItem() == null)
			return;
		
		InventorySlot invSlot = InventorySlot.valueOf(e.getRawSlot());
		if(invSlot == null)
			return;
		PapiMenuClickEvent event = new PapiMenuClickEvent(papiMenuExecutor, this, invSlot, player, slotStoreObjectMap, e);
		papiMenuExecutor.onMenuClick(event);
		
		e.setCancelled(event.isCancelled());
	}
	public void onInventoryClose(final InventoryCloseEvent e, final Player player) {
		PapiMenuCloseEvent event = new PapiMenuCloseEvent(papiMenuExecutor, this, player, slotStoreObjectMap);
		papiMenuExecutor.onMenuClose(event);
	}

	public void setItemHeadAsync(final InventorySlot menuInvSlot, final Material materialBeforeLoad, final PapiHeadBase64 papiHeadBase64, final String colorNameAndLore, Object slotStoreObject)
	{
		this.setItemHeadAsync(menuInvSlot, materialBeforeLoad, papiHeadBase64.getValue(), false, colorNameAndLore, slotStoreObject);
	}
	public void setItemHeadAsync(final InventorySlot menuInvSlot, final Material materialBeforeLoad, final PapiHeadBase64 papiHeadBase64, final boolean withGlow, final String colorNameAndLore, Object slotStoreObject)
	{
		this.setItemHeadAsync(menuInvSlot, materialBeforeLoad, papiHeadBase64.getValue(), withGlow, colorNameAndLore, slotStoreObject);
	}
	public void setItemHeadAsync(final InventorySlot menuInvSlot, final Material materialBeforeLoad, final PapiHeadBase64 papiHeadBase64, final String colorNameAndLore)
	{
		this.setItemHeadAsync(menuInvSlot, materialBeforeLoad, papiHeadBase64.getValue(), false, colorNameAndLore);
	}
	public void setItemHeadAsync(final InventorySlot menuInvSlot, final Material materialBeforeLoad, final PapiHeadBase64 papiHeadBase64, final boolean withGlow, final String colorNameAndLore)
	{
		this.setItemHeadAsync(menuInvSlot, materialBeforeLoad, papiHeadBase64.getValue(), withGlow, colorNameAndLore);
	}
	public void setItemHeadAsync(final InventorySlot menuInvSlot, final Material materialBeforeLoad, final String headBase64, final boolean withGlow, final String colorNameAndLore, Object slotStoreObject)
	{
		this.slotStoreObjectMap.put(menuInvSlot, slotStoreObject);
		this.setItemHeadAsync(menuInvSlot, materialBeforeLoad, headBase64, withGlow, colorNameAndLore);
	}
	public void setItemHeadAsync(final InventorySlot menuInvSlot, final Material materialBeforeLoad, final String headBase64, final boolean withGlow, final String colorNameAndLore)
	{
		ItemStack itemStack = inv.getItem(menuInvSlot.getValue());
		if(itemStack != null)
		{
			Material invSlotCurrentMaterial = itemStack.getType();
			if(invSlotCurrentMaterial != materialBeforeLoad && invSlotCurrentMaterial != Material.PLAYER_HEAD)
				inv.setItem(menuInvSlot.getValue(), MenuUtils.createGuiItem(materialBeforeLoad, true, Message.getColorMessage(colorNameAndLore)));
			else
			{
				ItemMeta itemMeta = itemStack.getItemMeta();
				String[] msgArray = Message.getColorMessage(colorNameAndLore).split("\n");
				if(msgArray.length >= 1)
					itemMeta.setDisplayName(msgArray[0]);
				if(msgArray.length >= 2)
					itemMeta.setLore(Arrays.asList(Arrays.copyOfRange(msgArray, 1, msgArray.length)));
				itemStack.setItemMeta(itemMeta);
				inv.setItem(menuInvSlot.getValue(), itemStack);
			}
		}
		else
			inv.setItem(menuInvSlot.getValue(), MenuUtils.createGuiItem(materialBeforeLoad, true, Message.getColorMessage(colorNameAndLore)));
		
		Bukkit.getScheduler().runTaskAsynchronously(PapiPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				inv.setItem(menuInvSlot.getValue(), MenuUtils.createGuiItemHead(headBase64, true, Message.getColorMessage(colorNameAndLore)));
			}
		});
	}

	protected void removePlayerOpenedMenu(Player player) {
		this.playersOpened.remove(player);
	}

}
