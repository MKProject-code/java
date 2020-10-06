package me.maskat.wolfsecurity.models;

import java.util.UUID;

import me.maskat.mysql.SQL;
import me.maskat.wolfsecurity.Config;

public class ModelUser {
    private int userid = -1;
    private String username = null;
    private UUID userUUID = null;
    //private String real_username;
	public int assigned_wolfid = -1;

	public void initId(int i) { userid = i; }
	public void initName(String str) { username = str; }
	public void initUUID(UUID uuid) { userUUID = uuid; }
	//public void setRealName(String str) { real_username = str; }
	public void initAssignedWolfId(int i) { assigned_wolfid = i; }
    
    public int getId() { return userid; }
    public String getName() { return username; }
    public UUID getUUID() { return userUUID; }
    //public String getRealName() { return real_username; }
    public int getAssignedWolfId() { return assigned_wolfid; }
    
    
    public void setAssignedWolfId(int wolfid) {
    	assigned_wolfid = wolfid;
		SQL.set("assigned_wolfid", wolfid, "userid", "=", String.valueOf(userid), Config.databaseTableUsers);
    }
	public boolean existWolfId() {
		return assigned_wolfid > 0;
	}
}