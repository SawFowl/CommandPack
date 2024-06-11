package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RandomTeleport;

@ConfigSerializable
public class ImplementRandomTeleport implements RandomTeleport {

	public ImplementRandomTeleport() {}

	@Setting("Attempts")
	private String attempts = "Количество попыток найти позицию. Увеличение этого значения может привести к сбоям в работе сервера.";
	@Setting("WorldSelector")
	private String worldSelector = "Указывает целевой мир для телепортации из текущего мира игрока.\nЕсли текущий мир игрока не указан в этой карте миров как исходный, игрок будет телепортирован в тот же мир, в котором он находится.";
	@Setting("StartFromWorldSpawn")
	private String startFromWorldSpawn = "Если true, то поиск случайной позиции будет осуществляться от точки спавна мира.\nЕсли false, то поиск будет осуществляться от текущих координат игрока.";
	@Setting("MinRadius")
	private String minRadius = "Минимальная дистанция телепортации.";
	@Setting("Radius")
	private String radius = "Максимальная дистанция телепортации.";
	@Setting("MaxY")
	private String maxY = "Максимальная высота для поиска позиции.";
	@Setting("MinY")
	private String minY = "Минимальная высота для поиска позиции.";
	@Setting("ProhibitedBiomes")
	private String prohibitedBiomes = "Биомы, указанные в этом списке, не будут доступны для телепортации по случайным координатам.";
	@Setting("OnlySurface")
	private String onlySurface = "Если значение равно true, игрок всегда будет телепортирован на поверхность.";
	@Setting("ProhibitedLiquids")
	private String prohibitedLiquids = "Если значение равно true, поиск правильной позиции будет пропускать блоки жидкости.";
	@Setting("ProhibitedBlocks")
	private String prohibitedBlocks = "Блоки, указанные в этом списке, не будут доступны для телепортации по случайным координатам.";
	@Setting("World")
	private String world = "Идентификатор целевого мира.";

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
