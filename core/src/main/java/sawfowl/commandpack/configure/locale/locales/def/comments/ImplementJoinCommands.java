package sawfowl.commandpack.configure.locale.locales.def.comments;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.JoinCommands;

@ConfigSerializable
public class ImplementJoinCommands implements JoinCommands {

	public ImplementJoinCommands() {}

	@Setting("FirstJoin")
	private String firstJoin = "These commands are executed only when the player first login to the server.";
	@Setting("Regularly")
	private String regularly = "These commands are executed every time a player login to the server, except for the first login.";

	@Override
	public String getFirstJoin() {
		return firstJoin;
	}

	@Override
	public String getRegularly() {
		return regularly;
	}

}
