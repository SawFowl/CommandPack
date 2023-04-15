package sawfowl.commandpack.configure.configs.commands;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.api.data.commands.settings.DelaySettings;

@ConfigSerializable
public class Delay implements DelaySettings {

	public Delay() {}
	public Delay(long seconds) {
		this.seconds = seconds;
	}
	public Delay(long seconds, CancelRules cancelRules) {
		this.seconds = seconds;
		this.cancelRules = cancelRules;
	}

	public Builder builder() {
		return new Builder();
	}

	@Setting("Seconds")
	private long seconds = 0;
	@Setting("CancelRulesSettings")
	private CancelRules cancelRules = new CancelRules();

	public long getSeconds() {
		return seconds;
	}

	public CancelRules getCancelRules() {
		return cancelRules;
	}

	@Override
	public String toString() {
		return "Delay [Seconds=" + seconds + ", CancelRulesSettings=" + cancelRules + "]";
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Seconds"), seconds)
				.set(DataQuery.of("CancelRulesSettings"), cancelRules)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements sawfowl.commandpack.api.data.commands.settings.DelaySettings.Builder {

		@Override
		public Builder setSeconds(long seconds) {
			Delay.this.seconds = seconds;
			return this;
		}

		@Override
		public Builder setCancelRules(sawfowl.commandpack.api.data.commands.settings.CancelRulesSettings rules) {
			Delay.this.cancelRules = new CancelRules(rules.isAllowMoving(), rules.isAllowOtherCommand());;
			return this;
		}

		@Override
		public Builder reset() {
			seconds = 0;
			cancelRules = new CancelRules(false, false);
			return this;
		}

		@Override
		public @NotNull DelaySettings build() {
			return Delay.this;
		}
		
	}

}
