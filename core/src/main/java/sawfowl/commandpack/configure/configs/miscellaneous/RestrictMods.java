package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class RestrictMods {

	public RestrictMods(){}

	@Setting("Enable")
	private boolean enable = false;
	@Setting("Blacklist")
	@LocalisedComment(path = {"Comments", "MainConfig", "RestrictMods", "Blacklist"}, plugin = "commandpack")
	private boolean blackList = true;
	@Setting("Clients")
	@LocalisedComment(path = {"Comments", "MainConfig", "RestrictMods", "Clients"}, plugin = "commandpack")
	private List<String> clients = Stream.of("anycheatclient").toList();
	@Setting("Mods")
	@LocalisedComment(path = {"Comments", "MainConfig", "RestrictMods", "ModsList"}, plugin = "commandpack")
	private List<String> mods = new ArrayList<>();
	@Setting("Packets")
	@LocalisedComment(path = {"Comments", "MainConfig", "RestrictMods", "Packets"}, plugin = "commandpack")
	private Map<String, String> packets = IntStream.range(0, 1).boxed().collect(Collectors.toMap(entry -> "modid:packetid", entry -> "ModName"));

	public boolean isEnable() {
		return enable;
	}

	public boolean isAllowedPlayerMod(String modId) {
		return blackList ? !mods.contains(modId) : mods.contains(modId);
	}

	public List<String> getDisAllowedMods(String packetData) {
		return blackList ?
			packets.entrySet().stream().filter(entry -> packetData.contains(entry.getKey())).map(entry -> entry.getValue()).toList()
			:
			packets.entrySet().stream().filter(entry -> !packetData.contains(entry.getKey())).map(entry -> entry.getValue()).toList();
	}

	public boolean isAllowedClient(String client) {
		return blackList ? clients.contains(client) : !clients.contains(client);
	}

}
