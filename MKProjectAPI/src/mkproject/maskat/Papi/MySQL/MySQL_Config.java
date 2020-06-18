package mkproject.maskat.Papi.MySQL;

public class MySQL_Config
{
    private static String host = "host";
    private static String port = "port";
    private static String user = "user";
    private static String password = "password";
    private static String database = "database";
    private static boolean ssl = true;
    private static boolean debug = false;
    
    public static void init(String hst, String prt, String usr, String psswrd, String db, boolean sl, boolean deb) {
        host = hst;
        port = prt;
        user = usr;
        password = psswrd;
        database = db;
        ssl = sl;
        debug = deb;
    }
    
    protected static String getHost() {
        return host;
    }
    
    protected static boolean isSSL() {
        return ssl;
    }
    
    protected static String getUser() {
    	return user;
    }
    
    protected static String getPassword() {
    	return password;
    }
    
    protected static String getDatabase() {
        return database;
    }
    
    protected static String getPort() {
        return port;
    }
    
    protected static boolean isDebug() {
    	return debug;
    }
}