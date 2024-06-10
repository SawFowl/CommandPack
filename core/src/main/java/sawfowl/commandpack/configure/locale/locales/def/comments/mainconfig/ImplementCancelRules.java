package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.DelayData.CancelRules;

@ConfigSerializable
public class ImplementCancelRules implements CancelRules {

	public ImplementCancelRules() {}

	@Setting("AllowMoving")
	private String allowMoving = "Cancelling the execution a command when the player moves.";
	@Setting("AllowOtherCommand")
	private String allowOtherCommand = "Cancelling the execution a command when a player uses another command.";

	@Override
	public String getAllowMoving() {
		return allowMoving;
	}

	@Override
	public String getAllowOtherCommand() {
		return allowOtherCommand;
	}

}
