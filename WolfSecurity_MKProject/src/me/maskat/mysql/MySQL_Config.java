package me.maskat.mysql;

public class MySQL_Config
{
    private static String host = "host";
    private static String port = "port";
    private static String user = "user";
    private static String password = "password";
    private static String database = "database";
    private static boolean ssl = true;
    
    public static void init(String h, String p, String usr, String pwrd, String db, boolean s) {
        host = h;
        port = p;
        user = usr;
        password = pwrd;
        database = db;
        ssl = s;
    }
//    public static void reload() {
//        MySQLRegister.plugin.reloadConfig();
//        create();
//    }
    
    public static String getHost() {
        return host;
    }
    
    public static boolean getSSL() {
        return ssl;
    }
    
    public static String getUser() {
    	return user;
    }
    
    public static String getPassword() {
    	return password;
    }
    
    public static String getDatabase() {
        return database;
    }
    
    public static String getPort() {
        return port;
    }
}