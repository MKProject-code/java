package me.maskat.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MySQL
{
	private static Connection con;
	
	public static Connection getConnection() {
	    return MySQL.con;
	}
	
	public static void setConnection(final String host, final String user, final String password, final String database, final String port) {
	    if (host == null || user == null || password == null || database == null) {
	        return;
	    }
	    disconnect(false);
	    try {
	        MySQL.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=" + MySQL_Config.getSSL(), user, password);
	        //Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "SQL connected.");
	    }
	    catch (Exception e) {
	        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Connect Error: " + e.getMessage());
	    }
	}
	
	public static void connect() {
	    connect(true);
	}
	
	private static void connect(final boolean message) {
	    final String host = MySQL_Config.getHost();
	    final String user = MySQL_Config.getUser();
	    final String password = MySQL_Config.getPassword();
	    final String database = MySQL_Config.getDatabase();
	    final String port = MySQL_Config.getPort();
	    if (isConnected()) {
	        if (message) {
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Connect Error: Already connected");
	        }
	    }
	    else if (host.equalsIgnoreCase("")) {
	        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Config Error: Host is blank");
	    }
	    else if (user.equalsIgnoreCase("")) {
	        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Config Error: User is blank");
	    }
	    else if (password.equalsIgnoreCase("")) {
	        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Config Error: Password is blank");
	    }
	    else if (database.equalsIgnoreCase("")) {
	        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Config Error: Database is blank");
	    }
	    else if (port.equalsIgnoreCase("")) {
	        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Config Error: Port is blank");
	    }
	    else {
	        setConnection(host, user, password, database, port);
	    }
	}
	
	public static void disconnect() {
	    disconnect(true);
	}
	
	private static void disconnect(final boolean message) {
	    try {
	        if (isConnected()) {
	            MySQL.con.close();
	            if (message) {
	                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "SQL disconnected.");
	            }
	        }
	        else if (message) {
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Disconnect Error: No existing connection");
	        }
	    }
	    catch (Exception e) {
	        if (message) {
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Disconnect Error: " + e.getMessage());
	        }
	    }
	    MySQL.con = null;
	}
	
	public static void reconnect() {
	    disconnect();
	    connect();
	}
	
	public static boolean isConnected() {
	    if (MySQL.con != null) {
	        try {
	            return !MySQL.con.isClosed();
	        }
	        catch (Exception e) {
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Connection:");
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + e.getMessage());
	        }
	    }
	    return false;
	}
	
	public static boolean update(final String command) {
	    if (command == null) {
	        return false;
	    }
	    connect(false);
	    try {
	        final Statement st = getConnection().createStatement();
	        st.executeUpdate(command);
	        st.close();
	    }
	    catch (Exception e) {
	        final String message = e.getMessage();
	        if (message != null) {
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Update:");
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Command: " + command);
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + message);
	        }
	    }
	    disconnect(false);
	    return true;
	}
	
	public static int update(final String command, boolean returnGeneratedKey) {
		if (command == null) {
			return -1;
		}
		connect(false);
		int generatedKey = -1;
		try {
			PreparedStatement st = getConnection().prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
			st.execute();
			
			ResultSet rs = st.getGeneratedKeys();
			
			if (rs.next()) {
				generatedKey = rs.getInt(1);
			}
			st.close();
		}
		catch (Exception e) {
			final String message = e.getMessage();
			if (message != null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Update:");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Command: " + command);
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + message);
			}
		}
		disconnect(false);
		return generatedKey;
	}
	
	public static ResultSet query(final String command) {
	    if (command == null) {
	        return null;
	    }
	    connect(false);
	    ResultSet rs = null;
	    try {
	        final Statement st = getConnection().createStatement();
	        rs = st.executeQuery(command);
	    }
	    catch (Exception e) {
	        final String message = e.getMessage();
	        if (message != null) {
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Query:");
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Command: " + command);
	            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + message);
	        }
	    }
	    return rs;
	}
}
