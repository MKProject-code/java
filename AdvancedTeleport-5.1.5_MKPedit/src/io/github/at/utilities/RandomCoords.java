package io.github.at.utilities;

//import com.wimbli.WorldBorder.BorderData;
import io.github.at.config.Config;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

//import static io.github.at.main.CoreClass.worldBorder;

public class RandomCoords {

    public static double getRandomCoords(double min, double max){
        Random r = new Random();
        return r.nextInt((int)Math.round(max - min)+1)+min ;
    }

    public static Location generateCoords(Player player, World world) {
        double x = getRandomCoords(Config.minX(), Config.maxX());
        double z = getRandomCoords(Config.minZ(), Config.maxZ());
//        if (Config.useWorldBorder() && worldBorder != null) {
//            BorderData border = com.wimbli.WorldBorder.Config.Border(world.getName());
//            // If a border has been set
//            if (border != null) {
//                x = getRandomCoords(border.getX() - border.getRadiusX(), border.getX() + border.getRadiusX());
//                z = getRandomCoords(border.getZ() - border.getRadiusZ(), border.getZ() + border.getRadiusZ());
//            }
//        }

        int y = world.getEnvironment() == World.Environment.NETHER ? 0 : 255;
        return new Location(world, x, y, z);
    }

}
