package sawfowl.commandpack.configure.configs.commands;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CancelRules implements sawfowl.commandpack.api.data.commands.settings.CancelRules {

	public CancelRules() {}
	public CancelRules(boolean allowMoving, boolean allowOtherCommand) {
		this.allowMoving = allowMoving;
		this.allowOtherCommand = allowOtherCommand;
	}

	public Builder builder() {
		return new Builder();
	}

	@Setting("AllowMoving")
	@Comment("Cancelling the execution a command when the player moves.")
	private boolean allowMoving = false;
	@Setting("AllowOtherCommand")
	@Comment("Cancelling the execution a command when a player uses another command.")
	private boolean allowOtherCommand = false;

	@Override
	public boolean isAllowMoving() {
		return allowMoving;
	}

	@Override
	public boolean isAllowOtherCommand() {
		return allowOtherCommand;
	}

	@Override
	public String toString() {
		return "CancelRules [AllowMoving=" + allowMoving + ", AllowOtherCommand=" + allowOtherCommand + "]";
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("AllowMoving"), allowMoving)
				.set(DataQuery.of("AllowOtherCommand"), allowOtherCommand)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements sawfowl.commandpack.api.data.commands.settings.CancelRules.Builder {

		@Override
		public Builder allowMoving(boolean value) {
			allowMoving = value;
			return this;
		}

		@Override
		public Builder allowOtherCommand(boolean value) {
			allowOtherCommand = value;
			return this;
		}

		@Override
		public Builder values(boolean allowMoving, boolean allowOtherCommand) {
			allowMoving(allowMoving);
			return allowOtherCommand(allowOtherCommand);
		}

		@Override
		public CancelRules.Builder reset() {
			allowMoving = false;
			allowOtherCommand = false;
			return this;
		}

		@Override
		public @NotNull CancelRules build() {
			return CancelRules.this;
		}
		
	}

}
