package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.RandomTeleport;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementRandomTeleport implements RandomTeleport {

	public ImplementRandomTeleport() {}

	@Setting("PositionSearchErrorStaff")
	Component positionSearchErrorStaff = TextUtils.deserializeLegacy("&cFailed to find a position in the world '" + Placeholders.WORLD + "' for teleportation. The search attempt limit has been reached: " + Placeholders.LIMIT + ".");
	@Setting("PositionSearchError")
	Component positionSearchError = TextUtils.deserializeLegacy("&cUnable to find teleportation position. Try again.");
	@Setting("SuccessStaff")
	Component successStaff = TextUtils.deserializeLegacy("&aPlayer &e" + Placeholders.PLAYER + "&a teleported to location &e<" + Placeholders.WORLD + "> " + Placeholders.LOCATION + "&a.");
	@Setting("Success")
	Component success = TextUtils.deserializeLegacy("&aYou teleported to location &e<" + Placeholders.WORLD + "> " + Placeholders.LOCATION + "&a.");
	@Setting("Wait")
	Component wait = TextUtils.deserializeLegacy("&eThe search for a proper location has started. Please wait.");
	@Setting("Cancelled")
	Component cancelled = TextUtils.deserializeLegacy("&eTeleportation has been canceled. Try again.");

	@Override
	public Component getPositionSearchErrorStaff(ServerWorld world, int limit) {
		return Text.of(positionSearchErrorStaff).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.LIMIT, limit).get();
	}

	@Override
	public Component getPositionSearchError() {
		return positionSearchError;
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player, ServerLocation location) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.WORLD, location.world().key().asString()).replace(Placeholders.LOCATION, location.blockPosition().toString()).get();
	}

	@Override
	public Component getSuccess(ServerLocation location) {
		return Text.of(success).replace(Placeholders.WORLD, location.world().key().asString()).replace(Placeholders.LOCATION, location.blockPosition().toString()).get();
	}

	@Override
	public Component getWait() {
		return wait;
	}

	@Override
	public Component getCancelled() {
		return cancelled;
	}

}
