// 
// Decompiled by Procyon v0.5.36
// 

package Trade;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    private static Main m;
    private Configuration configuration;
    private TradeHandler tradeHandler;
    
    public void onEnable() {
        Main.m = this;
        ItemStackUtils.loadUtils();
        this.configuration = new Configuration(false);
        this.getCommand("trade").setExecutor((CommandExecutor)new Commands());
        Bukkit.getPluginManager().registerEvents((Listener)(this.tradeHandler = new TradeHandler()), (Plugin)this);
    }
    
    public void onDisable() {
    }
    
    public static Main getInstance() {
        return Main.m;
    }
    
    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public TradeHandler getTradeHandler() {
        return this.tradeHandler;
    }
}
