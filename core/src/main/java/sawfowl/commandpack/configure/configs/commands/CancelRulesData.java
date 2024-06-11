package sawfowl.commandpack.configure.configs.commands;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class CancelRulesData implements CancelRules {

	public CancelRulesData() {}
	public static CancelRulesData of(boolean allowMoving, boolean allowOtherCommand) {
		return new CancelRulesData().builder().allowMoving(allowMoving).allowOtherCommand(allowOtherCommand).build();
	}

	public Builder builder() {
		return new Builder();
	}

	@Setting("AllowMoving")
	@LocalisedComment(path = {"Comments", "CommandsConfig", "DelayData", "CancelRules", "AllowMoving"}, plugin = "commandpack")
	private boolean allowMoving = false;
	@Setting("AllowOtherCommand")
	@LocalisedComment(path = {"Comments", "CommandsConfig", "DelayData", "CancelRules", "AllowOtherCommand"}, plugin = "commandpack")
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
		return "CancelRulesData [AllowMoving=" + allowMoving + ", AllowOtherCommand=" + allowOtherCommand + "]";
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

	public class Builder implements sawfowl.commandpack.api.data.command.CancelRules.Builder {

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
		public CancelRulesData.Builder reset() {
			allowMoving = false;
			allowOtherCommand = false;
			return this;
		}

		@Override
		public @NotNull CancelRulesData build() {
			return CancelRulesData.this;
		}
		
	}

}
