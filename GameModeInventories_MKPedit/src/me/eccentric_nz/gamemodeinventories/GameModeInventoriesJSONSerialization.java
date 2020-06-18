package me.eccentric_nz.gamemodeinventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.gamemodeinventories.JSON.JSONArray;
import me.eccentric_nz.gamemodeinventories.JSON.JSONException;
import me.eccentric_nz.gamemodeinventories.JSON.JSONObject;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

public class GameModeInventoriesJSONSerialization {
   public static Map<String, Object> toMap(JSONObject object) throws JSONException {
      Map<String, Object> map = new HashMap();
      Iterator keys = object.keys();

      while(keys.hasNext()) {
         String key = (String)keys.next();
         map.put(key, fromJson(object.get(key)));
      }

      return map;
   }

   private static Object fromJson(Object json) throws JSONException {
      if (json == JSONObject.NULL) {
         return null;
      } else if (json instanceof JSONObject) {
         return toMap((JSONObject)json);
      } else {
         return json instanceof JSONArray ? toList((JSONArray)json) : json;
      }
   }

   public static List<Object> toList(JSONArray array) throws JSONException {
      List<Object> list = new ArrayList();

      for(int i = 0; i < array.length(); ++i) {
         list.add(fromJson(array.get(i)));
      }

      return list;
   }

   public static String toString(ItemStack[] inv) {
      List<String> result = new ArrayList();
      List<ConfigurationSerializable> items = new ArrayList();
      items.addAll(Arrays.asList(inv));
      items.forEach((cs) -> {
         if (cs == null) {
            result.add("null");
         } else {
            result.add((new JSONObject(serialize(cs))).toString());
         }

      });
      JSONArray json_array = new JSONArray(result);
      return json_array.toString();
   }

   public static ItemStack[] toItemStacks(String s) {
      JSONArray json = new JSONArray(s);
      List<ItemStack> contents = new ArrayList();

      for(int i = 0; i < json.length(); ++i) {
         String piece = json.getString(i);
         if (piece.equalsIgnoreCase("null")) {
            contents.add((Object)null);
         } else {
            try {
               ItemStack item = (ItemStack)deserialize(toMap(new JSONObject(piece)));
               contents.add(item);
            } catch (JSONException var6) {
               System.err.println("There was a JSON error: " + var6.getMessage());
            }
         }
      }

      ItemStack[] items = new ItemStack[contents.size()];

      for(int x = 0; x < contents.size(); ++x) {
         items[x] = (ItemStack)contents.get(x);
      }

      return items;
   }

   public static Map<String, Object> serialize(ConfigurationSerializable cs) {
      Map<String, Object> serialized = recreateMap(cs.serialize());
      serialized.entrySet().forEach((entry) -> {
         if (entry.getValue() instanceof ConfigurationSerializable) {
            entry.setValue(serialize((ConfigurationSerializable)entry.getValue()));
         }

      });
      serialized.put("==", ConfigurationSerialization.getAlias(cs.getClass()));
      return serialized;
   }

   public static Map<String, Object> recreateMap(Map<String, Object> original) {
      Map<String, Object> map = new HashMap();
      original.entrySet().forEach((entry) -> {
         map.put(entry.getKey(), entry.getValue());
      });
      return map;
   }

   public static ConfigurationSerializable deserialize(Map<String, Object> map) {
      map.entrySet().forEach((entry) -> {
         if (entry.getValue() instanceof Map && ((Map)entry.getValue()).containsKey("==")) {
            entry.setValue(deserialize((Map)entry.getValue()));
         }

      });
      return ConfigurationSerialization.deserializeObject(map);
   }
}
