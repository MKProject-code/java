package mkproject.maskat.WorldManager.WorldGenerators;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

public class Empty {
	protected static void setEmptyChunkGenerator(WorldCreator worldCreator) {
	    worldCreator.generator(new ChunkGenerator() {
	        @Override
	        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
	            return createChunkData(world);
	        }
	    });
	    worldCreator.type(WorldType.FLAT);
	}
}
