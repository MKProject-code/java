package mkproject.maskat.Papi.MySQL;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mkproject.maskat.Papi.PapiPlugin;
import mkproject.maskat.Papi.Utils.Message;

public class PapiSQL
{
    public static boolean tableExists(final String table) {
        try {
            final Connection connection = PapiMySQL.getConnection();
            if (connection == null) {
                return false;
            }
            final DatabaseMetaData metadata = connection.getMetaData();
            if (metadata == null) {
                return false;
            }
            final ResultSet rs = metadata.getTables(null, null, table, null);
            if (rs.next()) {
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
//    public static boolean insertData(final String columns, final String values, final String table) {
//        return MySQL.update("INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");");
//    }
    
    public static int insertData(final String columns, final String values, final String table) {
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");");
    	
        int result = PapiMySQL.update("INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");", true);
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (int): "+result);
		return result;
    }
    
    public static boolean deleteData(final String column, final String logic_gate, String data, final String table) {
        if (data != null) {
            data = "'" + data.replace("'", "\\'") + "'";
        }
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: DELETE FROM " + table + " WHERE " + column + logic_gate + data + ";");
        
        boolean result = PapiMySQL.update("DELETE FROM " + table + " WHERE " + column + logic_gate + data + ";");
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): "+result);
		return result;
    }
    
    public static boolean exists(final String column, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + column + "=" + data + ";");
            if (rs.next()) {
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    public static boolean exists(String[] where_arguments, final String table) {
    	String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return false;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        try {
            final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + arguments + ";");
            if (rs.next()) {
            	return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    public static boolean exists(final String where_arguments, final String table) {
    	try {
    		Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: SELECT * FROM " + table + " WHERE " + where_arguments + ";");
    		final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + where_arguments + ";");
    		if (rs.next()) {
    			Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): True");
    			return true;
    		}
    	}
    	catch (Exception ex) {}
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): False");
    	return false;
    }
    public static boolean exists(final String column, final String logic_gate, Object data, final String table) {
        if (data != null) {
            data = "'" + data.toString().replace("'","\\'") + "'";
        }
    	try {
    		Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
    		final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
    		if (rs.next()) {
    			Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): True");
    			return true;
    		}
    	}
    	catch (Exception ex) {}
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): False");
    	return false;
    }
    public static Object get(final String selected, final String where_arguments, final String table) {
    	try {
    		Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: SELECT * FROM " + table + " WHERE " + where_arguments + ";");
    		final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + where_arguments + ";");
    		if (rs.next()) {
    			Object result = rs.getObject(selected);
    			Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (Object): "+result);
    			return result;
    		}
    	}
    	catch (Exception ex) {}
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (Object): null");
    	return null;
    }
    public static Object getOrderBy(final String selected, final String where_arguments, final String orderByColumn, final String OrderType, final String table) {
    	try {
    		Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: SELECT * FROM " + table + " WHERE " + where_arguments + " ORDER BY `"+orderByColumn+"` "+OrderType+";");
    		final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + where_arguments + " ORDER BY `"+orderByColumn+"` "+OrderType+";");
    		if (rs.next()) {
    			Object result = rs.getObject(selected);
    			Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (Object): "+result);
    			return result;
    		}
    	}
    	catch (Exception ex) {}
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (Object): null");
    	return null;
    }
    public static boolean deleteTable(final String table) {
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: DROP TABLE " + table + ";");
    	boolean result = PapiMySQL.update("DROP TABLE " + table + ";");
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): "+result);
        return result;
    }
    
    public static boolean truncateTable(final String table) {
        return PapiMySQL.update("TRUNCATE TABLE " + table + ";");
    }
    
    public static boolean createTable(final String table, final String columns) {
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: CREATE TABLE IF NOT EXISTS " + table + " (" + columns + ");");
        boolean result = PapiMySQL.update("CREATE TABLE IF NOT EXISTS " + table + " (" + columns + ");");
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): "+result);
        return result;
    }
    
    public static boolean upsert(final String selected, Object object, final String column, String data, final String table) {
        if (object != null) {
            object = "'" + object + "'";
        }
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + column + "=" + data + ";");
            if (rs.next()) {
                PapiMySQL.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + "=" + data + ";");
            }
            else {
                insertData(column + ", " + selected, "'" + data + "', '" + object + "'", table);
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    public static boolean set(final String selected, Object object, final String column, final String logic_gate, Object data, final String table) {
        if (!(object instanceof Boolean) && object != null) {
            object = "'" + object + "'";
        }
        if (data != null) {
            data = "'" + data + "'";
        }
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + logic_gate + data + ";");
        
        boolean result = PapiMySQL.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + logic_gate + data + ";");
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): "+result);
		return result;
    }
    
    public static boolean setMulti(String changeSet, final String column, final String logic_gate, Object data, final String table) {
        if (data != null) {
            data = "'" + data.toString().replace("'","\\'") + "'";
        }
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: UPDATE " + table + " SET " + changeSet + " WHERE " + column + logic_gate + data + ";");
        
        boolean result = PapiMySQL.update("UPDATE " + table + " SET " + changeSet + " WHERE " + column + logic_gate + data + ";");
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): "+result);
		return result;
    }
    
    public static boolean setMultiOrderBy(int limit, String changeSet, final String column, final String logic_gate, Object data, final String orderByColumn, final String OrderType, final String table) {
    	if (data != null) {
    		data = "'" + data.toString().replace("'","\\'") + "'";
    	}
    	
    	String sql = "UPDATE " + table + " SET " + changeSet + " WHERE " + column + logic_gate + data;
    	if(orderByColumn.length() > 0)
    		sql += " ORDER BY `"+orderByColumn + "` DESC";
    	if(limit > 0)
    		sql += " LIMIT "+limit;
    	
    	Message.debugMessage(PapiPlugin.getPlugin(), sql + ";");
    	boolean result = PapiMySQL.update(sql  + ";");
    	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (boolean): "+result);
    	return result;
    }
    
    public static boolean set(final String selected, Object object, final String[] where_arguments, final String table) {
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return false;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        if (object != null) {
            object = "'" + object + "'";
        }
        return PapiMySQL.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + arguments + ";");
    }
    
    public static Object get(final String selected, final String[] where_arguments, final String table) {
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return false;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        try {
            final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + arguments + ";");
            if (rs.next()) {
                return rs.getObject(selected);
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    public static ArrayList<Object> listGet(final String selected, final String[] where_arguments, final String table) {
        final ArrayList<Object> array = new ArrayList<Object>();
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return array;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        try {
            final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + arguments + ";");
            while (rs.next()) {
                array.add(rs.getObject(selected));
            }
        }
        catch (Exception ex) {}
        return array;
    }
    
    public static ResultSet getResultSetAll(final int limit, final String select, final String table) {
        try {
        	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: SELECT "+select+" FROM " + table + (limit > 0  ? " LIMIT "+String.valueOf(limit) : "") + ";");
        	final ResultSet rs = PapiMySQL.query("SELECT "+select+" FROM " + table + (limit > 0  ? " LIMIT "+String.valueOf(limit) : "") + ";");
            if (rs.next()) {
            	rs.beforeFirst();
                return rs;
            }
        }
        catch (Exception ex) {}
        return null;
    }

	public static ResultSet getResultSetAllOrderBy(int limit, List<String> selects, String orderByColumn, String orderType, String table) {
        try {
        	Message.debugMessage(PapiPlugin.getPlugin(), "SELECT "+String.join(",", selects)+" FROM " + table + " ORDER BY `"+orderByColumn+"` "+orderType+ (limit > 0  ? " LIMIT "+String.valueOf(limit) : "")+";");
        	final ResultSet rs = PapiMySQL.query("SELECT "+String.join(",", selects)+" FROM " + table + " ORDER BY `"+orderByColumn+"` "+orderType+ (limit > 0  ? " LIMIT "+String.valueOf(limit) : "")+";");
            if (rs.next()) {
            	rs.beforeFirst();
                return rs;
            }
        }
        catch (Exception ex) {}
        return null;
	}
	public static ResultSet getResultSetAllOrderBy(int limit, List<String> selects, String getWhereObject, List<String> orderByColumn, List<String> OrderType, String table) {
		String orders = "";
		for(int i=0;i<orderByColumn.size();i++) {
			orders += "`"+orderByColumn.get(i)+"` "+OrderType.get(i) + ", ";
		}
		
		if(orders.length() > 0)
			orders = orders.substring(0, orders.length()-2);
		
        try {
        	Message.debugMessage(PapiPlugin.getPlugin(), "SELECT "+String.join(",", selects)+" FROM " + table + " WHERE " + getWhereObject + " ORDER BY "+orders+ (limit > 0  ? " LIMIT "+String.valueOf(limit) : "")+";");
        	final ResultSet rs = PapiMySQL.query("SELECT "+String.join(",", selects)+" FROM " + table + " WHERE " + getWhereObject + " ORDER BY "+orders+ (limit > 0  ? " LIMIT "+String.valueOf(limit) : "")+";");
            if (rs.next()) {
            	rs.beforeFirst();
                return rs;
            }
        }
        catch (Exception ex) {}
        return null;
	}
	public static ResultSet getResultSetAllOrderBy(int limit, List<String> selects, String getWhereObject, String orderByColumn, String orderType, String table) {
        try {
        	Message.debugMessage(PapiPlugin.getPlugin(), "SELECT "+String.join(",", selects)+" FROM " + table + " WHERE " + getWhereObject + " ORDER BY `"+orderByColumn+"` "+orderType+ (limit > 0  ? " LIMIT "+String.valueOf(limit) : "")+";");
        	final ResultSet rs = PapiMySQL.query("SELECT "+String.join(",", selects)+" FROM " + table + " WHERE " + getWhereObject + " ORDER BY `"+orderByColumn+"` "+orderType+ (limit > 0  ? " LIMIT "+String.valueOf(limit) : "")+";");
            if (rs.next()) {
            	rs.beforeFirst();
                return rs;
            }
        }
        catch (Exception ex) {}
        return null;
	}
    
    public static ResultSet getResultSetAll(final int limit, final String select, final String where_arguments, final String table) {
    	try {
    		Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: SELECT "+select+" FROM " + table + " WHERE " + where_arguments + (limit > 0  ? " LIMIT "+String.valueOf(limit) : "") + ";");
    		final ResultSet rs = PapiMySQL.query("SELECT "+select+" FROM " + table + " WHERE " + where_arguments + (limit > 0  ? " LIMIT "+String.valueOf(limit) : "") + ";");
    		if (rs.next()) {
    			rs.beforeFirst();
    			return rs;
    		}
    	}
    	catch (Exception ex) {}
    	return null;
    }
    
    public static ResultSet getResultSetWhere(final int limit, final String select, final String column, final String logic_gate, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
        	final ResultSet rs = PapiMySQL.query("SELECT "+select+" FROM " + table + " WHERE " + column + logic_gate + data + (limit > 0  ? " LIMIT "+String.valueOf(limit) : "") + ";");
            if (rs.next()) {
            	rs.beforeFirst();
                return rs;
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    public static Object get(final String selected, final String column, final String logic_gate, String data, final String table) {
        if (data != null) {
            data = "'" + data.toString().replace("'","\\'") + "'";
        }
        try {
        	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: SELECT "+selected+" FROM " + table + " WHERE " + column + logic_gate + data + ";");
            final ResultSet rs = PapiMySQL.query("SELECT "+selected+" FROM " + table + " WHERE " + column + logic_gate + data + ";");
            if (rs.next()) {
    			Object result = rs.getObject(selected);
    			Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (Object): "+result);
    			return result;
            }
        }
        catch (Exception ex) {}
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result (Object): null");
        return null;
    }
    
	public static ResultSet query(String sql) {
        try {
        	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Execute query: "+sql);
        	final ResultSet rs = PapiMySQL.query(sql);
            if (rs.next()) {
            	rs.beforeFirst();
            	Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result fetch size: "+rs.getFetchSize());
                return rs;
            }
        }
        catch (Exception ex) {}
        Message.debugMessage(PapiPlugin.getPlugin(), " --- SQL DEBUG --- Query result: null");
        return null;
	}
    
    public static ArrayList<Object> listGet(final String selected, final String column, final String logic_gate, String data, final String table) {
        final ArrayList<Object> array = new ArrayList<Object>();
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
            while (rs.next()) {
                array.add(rs.getObject(selected));
            }
        }
        catch (Exception ex) {}
        return array;
    }
    
    public int countRows(final String table) {
        int i = 0;
        if (table == null) {
            return i;
        }
        final ResultSet rs = PapiMySQL.query("SELECT * FROM " + table + ";");
        try {
            while (rs.next()) {
                ++i;
            }
        }
        catch (Exception ex) {}
        return i;
    }
}
