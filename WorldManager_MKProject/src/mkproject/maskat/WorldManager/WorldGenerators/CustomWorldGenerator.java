package mkproject.maskat.WorldManager.WorldGenerators;

import org.bukkit.WorldCreator;

public class CustomWorldGenerator {
	public static void setEmpty(WorldCreator worldCreator) {
		Empty.setEmptyChunkGenerator(worldCreator);
	}
}
