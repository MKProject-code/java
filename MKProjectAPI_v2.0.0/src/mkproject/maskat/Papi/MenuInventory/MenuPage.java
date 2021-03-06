package mkproject.maskat.Papi.MenuInventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.PapiPlugin;
import mkproject.maskat.Papi.Utils.Message;

@Deprecated
public class MenuPage implements InventoryHolder {

	private Inventory inv;
	private Map<InventorySlot, Object> slotStoreObjectMap = new HashMap<>();
	
	private JavaPlugin pluginExecutor;
	private Object pageUniqueId;
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
	public MenuPage(JavaPlugin pluginExecutor, int numberOfRows, final String colorTitle, Object pageUniqueId) {
		this.pluginExecutor = pluginExecutor;
		
		if(numberOfRows < 1)
			numberOfRows = 1;
		else if(numberOfRows > 6)
			numberOfRows = 6;
		
		this.inv = Bukkit.createInventory(this, numberOfRows*9, Message.getColorMessage(colorTitle));
        
        this.pageUniqueId = pageUniqueId;
	}
	
	public void setItem(final InventorySlot menuInvSlot, final Material material, final String colorNameAndLore)
	{
        inv.setItem(menuInvSlot.getValue(), MenuUtils.createGuiItem(material, Message.getColorMessage(colorNameAndLore)));
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
	public void setItemLoreLine(final InventorySlot menuInvSlot, final int line, final String colorLore)
	{
		ItemStack itemStack = inv.getItem(menuInvSlot.getValue());
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> loreList = itemMeta.getLore();
		loreList.set(line, Message.getColorMessage(colorLore));
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
		this.slotStoreObjectMap.put(menuInvSlot, slotStoreObject);
        this.setItem(menuInvSlot, material, colorNameAndLore);
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
		
		PapiMenuInventoryClickEvent event = new PapiMenuInventoryClickEvent(pluginExecutor, this, InventorySlot.valueOf(e.getRawSlot()), player, slotStoreObjectMap, pageUniqueId);
		
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if (!event.isCancelled()) {
			
		}
	}
}
