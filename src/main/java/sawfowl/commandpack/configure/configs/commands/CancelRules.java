package sawfowl.commandpack.configure.configs.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CancelRules {

	public CancelRules() {}
	public CancelRules(boolean allowMoving, boolean allowOtherCommand) {
		this.allowMoving = allowMoving;
		this.allowOtherCommand = allowOtherCommand;
	}

	@Setting("AllowMoving")
	@Comment("Cancelling the execution a command when the player moves.")
	private boolean allowMoving = false;
	@Setting("AllowOtherCommand")
	@Comment("Cancelling the execution a command when a player uses another command.")
	private boolean allowOtherCommand = false;

	public boolean isAllowMoving() {
		return allowMoving;
	}

	public boolean isAllowOtherCommand() {
		return allowOtherCommand;
	}

	@Override
	public String toString() {
		return "CancelRules [AllowMoving=" + allowMoving + ", AllowOtherCommand=" + allowOtherCommand + "]";
	}

}
