package mkproject.maskat.Papi.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Utils.Message.ClickAction;
import mkproject.maskat.Papi.Utils.Message.HoverAction;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;

public class JsonBuilder {
    
    /* JsonBuilder by FisheyLP */
 
    private List<String> extras = new ArrayList<String>();
 
    public JsonBuilder(String... text) {
        for(String extra : text)
            parse(extra);
    }
 
    public JsonBuilder parse(String text) {
           String regex = "[&§]{1}([a-fA-Fl-oL-O0-9]){1}";
           text = text.replaceAll(regex, "§$1");
           if(!Pattern.compile(regex).matcher(text).find()) {
              withText(text);
              return this;
           }
           String[] words = text.split(regex);
 
           int index = words[0].length();
           for(String word : words) {
               try {
                   if(index != words[0].length())
               withText(word).withColor("§"+text.charAt(index - 1));
               } catch(Exception e){}
               index += word.length() + 2;
           }
           return this;
       }
 
    public JsonBuilder t(String text) {
    	return withText(text);
    }
    public JsonBuilder withText(String text) {
        extras.add("{text:\"" + text + "\"}");
        return this;
    }
 
    public JsonBuilder c(ChatColor color) {
    	return withColor(color);
    }
    public JsonBuilder withColor(ChatColor color) {
        String c = color.name().toLowerCase();
        addSegment(color.isColor() ? "color:" + c : c + ":true");
        return this;
    }
 
    public JsonBuilder c(String color) {
    	return withColor(color);
    }
    public JsonBuilder withColor(String color) {
        while(color.length() != 1) color = color.substring(1).trim();
        withColor(ChatColor.getByChar(color));
        return this;
    }
 
    public JsonBuilder click(ClickAction runCommand, String value) {
    	return withClickEvent(runCommand, value);
    }
    public JsonBuilder withClickEvent(ClickAction runCommand, String value) {
        addSegment("clickEvent:{action:" + runCommand.toString().toLowerCase()
                + ",value:\"" + value + "\"}");
        return this;
    }
 
    public JsonBuilder hover(HoverAction action, String value) {
    	return withHoverEvent(action, value);
    }
    public JsonBuilder withHoverEvent(HoverAction action, String value) {
        addSegment("hoverEvent:{action:" + action.toString().toLowerCase()
                + ",value:\"" + value + "\"}");
        return this;
    }
 
    private void addSegment(String segment) {
        String lastText = extras.get(extras.size() - 1);
        lastText = lastText.substring(0, lastText.length() - 1)
                + ","+segment+"}";
        extras.remove(extras.size() - 1);
        extras.add(lastText);
    }
 
    public String toString() {
        if(extras.size() <= 1) return extras.size() == 0 ? "{text:\"\"}" : extras.get(0);
        String text = extras.get(0).substring(0, extras.get(0).length() - 1) + ",extra:[";
        extras.remove(0);;
        for (String extra : extras)
            text = text + extra + ",";
        text = text.substring(0, text.length() - 1) + "]}";
        return text;
    }
 
    public void sendJson(Player player) {
    	((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(toString())));
    }
}