package sawfowl.commandpack.configure.locale.locales.def.commands.serverstat;

import java.util.stream.Stream;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Worlds;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementWorlds implements Worlds {

	public ImplementWorlds() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3&lWorlds information");
	@Setting("WorldInfo")
	private Component worldInfo = TextUtils.deserializeLegacy("&aWorld&f: &e" + Placeholders.WORLD + "&a. Chunks loaded&f: &e" + Placeholders.CHUNKS_SIZE + "&a. Entities&f: &e" + Placeholders.ENTITIES_SIZE + "&a. TPS&f: &e" + Placeholders.VALUE + "&7(" + Placeholders.TIME + "&3ms&7)&a.");

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getWorldInfo(ServerWorld world, Component tps, Component ticks) {
		return Text.of(worldInfo).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.CHUNKS_SIZE, Stream.of(world.loadedChunks()).count()).replace(Placeholders.ENTITIES_SIZE, world.entities().size()).replace(Placeholders.VALUE, tps).replace(Placeholders.TIME, ticks).get();
	}

}
