package sawfowl.commandpack.configure.configs.commands;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class RandomTeleportConfig {

	@Setting("WorldSelector")
	@Comment("Specifies the target world for teleportation by the player's current world.\nIf the player's current world is not listed in this worlds map as a source world, the player will be teleported to the same world he is in.")
	private Map<String, String> map = createDefault();

	public ServerWorld getTargetWorld(ServerWorld source) {
		return Sponge.server().worldManager().world(getTargetWorldId(source.key().asString())).orElse(source);
	}

	private ResourceKey getTargetWorldId(String sourceWorldId) {
		return ResourceKey.resolve(map.getOrDefault(sourceWorldId, sourceWorldId));
	}

	private Map<String, String> createDefault() {
		Map<String, String> map = new HashMap<>();
		map.put("minecraft:overworld", "minecraft:overworld");
		map.put("minecraft:the_nether", "minecraft:the_nether");
		map.put("minecraft:the_end", "minecraft:the_end");
		return map;
	}

}
