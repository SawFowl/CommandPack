package sawfowl.commandpack.configure.configs.commands;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class DelayData implements sawfowl.commandpack.api.data.command.Delay {

	public DelayData() {}
	public DelayData(long seconds) {
		this.seconds = seconds;
	}
	public DelayData(long seconds, CancelRulesData cancelRulesData) {
		this.seconds = seconds;
		this.cancelRulesData = cancelRulesData;
	}

	public Builder builder() {
		return new Builder();
	}

	@Setting("Seconds")
	private long seconds = 0;
	@Setting("CancelRulesData")
	private CancelRulesData cancelRulesData = new CancelRulesData();

	public long getSeconds() {
		return seconds;
	}

	public CancelRulesData getCancelRules() {
		return cancelRulesData;
	}

	@Override
	public String toString() {
		return "DelayData [Seconds=" + seconds + ", CancelRulesData=" + cancelRulesData + "]";
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Seconds"), seconds)
				.set(DataQuery.of("CancelRulesData"), cancelRulesData)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements sawfowl.commandpack.api.data.command.Delay.Builder {

		@Override
		public Builder setSeconds(long seconds) {
			DelayData.this.seconds = seconds;
			return this;
		}

		@Override
		public Builder setCancelRules(sawfowl.commandpack.api.data.command.CancelRules rules) {
			DelayData.this.cancelRulesData = new CancelRulesData(rules.isAllowMoving(), rules.isAllowOtherCommand());;
			return this;
		}

		@Override
		public Builder reset() {
			seconds = 0;
			cancelRulesData = new CancelRulesData(false, false);
			return this;
		}

		@Override
		public @NotNull DelayData build() {
			return DelayData.this;
		}
		
	}

}
