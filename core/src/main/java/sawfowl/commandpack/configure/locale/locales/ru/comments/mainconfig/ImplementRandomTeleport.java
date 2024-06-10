package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RandomTeleport;

@ConfigSerializable
public class ImplementRandomTeleport implements RandomTeleport {

	public ImplementRandomTeleport() {}

	@Setting("Attempts")
	private String attempts = "The number of attempts to find the position. Increasing the value can lead to server crashes.";
	@Setting("WorldSelector")
	private String worldSelector = "Specifies the target world for teleportation by the player's current world.\nIf the player's current world is not listed in this worlds map as a source world, the player will be teleported to the same world he is in.";
	@Setting("StartFromWorldSpawn")
	private String startFromWorldSpawn = "If true, the search for a random position will be performed from the world spawn point.\nIf false, the search will be performed from the current coordinates of the player.";
	@Setting("MinRadius")
	private String minRadius = "Minimum teleportation distance.";
	@Setting("Radius")
	private String radius = "Maximum teleportation distance.";
	@Setting("MaxY")
	private String maxY = "Maximum height for finding a position.";
	@Setting("MinY")
	private String minY = "Minimum height for finding a position.";
	@Setting("ProhibitedBiomes")
	private String prohibitedBiomes = "Biomes specified in this list will not be available for teleportation by random coordinates.";
	@Setting("OnlySurface")
	private String onlySurface = "If true, the player will always move to the surface.";
	@Setting("ProhibitedLiquids")
	private String prohibitedLiquids = "If true, the search for the correct position will skip fluid blocks.";
	@Setting("ProhibitedBlocks")
	private String prohibitedBlocks = "Blocks specified in this list will not be available for teleportation by random coordinates.";
	@Setting("World")
	private String world = "The identifier of the target world.";

	@Override
	public String getAttempts() {
		return attempts;
	}

	@Override
	public String getWorldSelector() {
		return worldSelector;
	}

	@Override
	public String getStartFromWorldSpawn() {
		return startFromWorldSpawn;
	}

	@Override
	public String getMinRadius() {
		return minRadius;
	}

	@Override
	public String getRadius() {
		return radius;
	}

	@Override
	public String getMaxY() {
		return maxY;
	}

	@Override
	public String getMinY() {
		return minY;
	}

	@Override
	public String getProhibitedBiomes() {
		return prohibitedBiomes;
	}

	@Override
	public String getOnlySurface() {
		return onlySurface;
	}

	@Override
	public String getProhibitedLiquids() {
		return prohibitedLiquids;
	}

	@Override
	public String getProhibitedBlocks() {
		return prohibitedBlocks;
	}

	@Override
	public String getWorld() {
		return world;
	}

}
