package mkproject.maskat.Papi;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;


public class PapiVault {
    protected static Economy econ = null;
    protected static Permission perms = null;
    protected static Chat chat = null;
    
    protected static Economy getEconomy() {
        return econ;
    }
    
    protected static Permission getPermissions() {
        return perms;
    }
    
    protected static Chat getChat() {
        return chat;
    }
}
