package mkproject.maskat.AdminUtils.Cmds;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;


public class CmdRename implements CommandExecutor, TabCompleter {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);
		
		manager.registerArgTabComplete(0, List.of("name","lore"));
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("name|lore", "<text (use '%nl%' as a new line)>"));
		
		if(manager.isPlayer())
			if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
				return manager.doReturn();
		
		manager.registerArgUsage(1, "name","<text>");
		manager.registerArgUsage(1, "lore","<text (use '%nl%' as a new line)>");
		
		if(manager.hasArgStart(2))
		{
			if(manager.hasArg(1, "name")) // /rename name <text>
				this.setItemName(manager, manager.getStringArgStart(2));
			else if(manager.hasArg(1, "lore")) // /rename lore <text>
				this.setItemLore(manager, manager.getStringArgStart(2));
		}
		
		return manager.doReturn();
	}
	
	// --------- /rename name <new_item_name>
	private void setItemName(CommandManager manager, String newTitle) {
		try
		{
			ItemStack itemStack = manager.getPlayer().getInventory().getItemInMainHand();
			
			ItemMeta itemMeta = itemStack.getItemMeta();
			
			itemMeta.setDisplayName(Message.getColorMessage(newTitle));
			
			itemStack.setItemMeta(itemMeta);
			
			manager.setReturnMessage("&a&oZmieniłeś nazwę przedmiotu w ręce na:\n&e&o"+newTitle);
		}
		catch(Exception ex)
		{
			manager.setReturnMessage("&c&oNie udało sie zmienić nazwy dla aktualnie trzymanego przedmiotu");
			return;
		}
	}
	
	// --------- /rename lore <new_item_name>
	private void setItemLore(CommandManager manager, String newLores) {
		try
		{
			String[] newLoresArr = Message.getColorMessage(newLores).split("%nl%");
			
			ItemStack itemStack = manager.getPlayer().getInventory().getItemInMainHand();
			
			ItemMeta itemMeta = itemStack.getItemMeta();
			
			itemMeta.setLore(Arrays.asList(newLoresArr));
			
			itemStack.setItemMeta(itemMeta);
			
			manager.setReturnMessage("&a&oZmieniłeś opis przedmiotu w ręce na:\n&e&o"+String.join("\n", newLoresArr));
		}
		catch(Exception ex)
		{
			manager.setReturnMessage("&c&oNie udało sie zmienić opisu dla aktualnie trzymanego przedmiotu");
			return;
		}
	}
}
