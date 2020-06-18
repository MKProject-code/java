package mkproject.maskat.Papi;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.world.World;

public class PapiWorldEdit {
//	protected static boolean pasteSchematic(String fileName, Location loc, boolean pasteAir) {
//		return pasteSchematic(fileName, loc, pasteAir, false) == null ? false : true;
//    }
	public static EditSession pasteSchematic(String fileName, Location loc, boolean pasteAir, boolean allowUndo) {
		//Bukkit.broadcastMessage("PapiWorldEdit.pasteSchematic");
        File file = new File(fileName);
        if (file.exists() == false) {
        	PapiPlugin.getPlugin().getLogger().warning("**** Schematic paste error ****");
        	PapiPlugin.getPlugin().getLogger().warning("**** File not found: " + fileName);
            return null;
        }
        
        World weWorld = new BukkitWorld(loc.getWorld());
        
        EditSession editSession = null;
        try {
			//EditSession editSession = ClipboardFormats.findByFile(file).load(file).paste(weWorld, BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()), allowUndo, pasteAir, (Transform) null);
        	editSession = ClipboardFormats.findByFile(file).load(file).paste(weWorld, BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()), allowUndo, pasteAir, (Transform) null);
        	editSession.flushQueue();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        
        return editSession;
    }
	
	public static WorldEditPlugin getPlugin() {
		return PapiPlugin.getWorldEditPlugin();
	}
}
