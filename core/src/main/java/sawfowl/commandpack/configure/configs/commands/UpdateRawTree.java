package sawfowl.commandpack.configure.configs.commands;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.api.data.command.UpdateTree;

@ConfigSerializable
public class UpdateRawTree implements UpdateTree {

	public UpdateRawTree(){}

	public Builder builder() {
		return new Builder() {
			
			@Override
			public @NotNull UpdateTree build() {
				return UpdateRawTree.this;
			}
			
			@Override
			public Builder setInterval(long value) {
				interval = value;
				return this;
			}
			
			@Override
			public Builder setEnable(boolean value) {
				enable = value;
				return this;
			}
		};
	}

	@Setting("Enable")
	private boolean enable = true;
	@Setting("Interval")
	private long interval = 120;

	public boolean isEnable() {
		return enable;
	}

	public long getInterval() {
		return interval;
	}

	@Override
	public int contentVersion() {
		return 0;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
			.set(DataQuery.of("ContentVersion"), contentVersion())
			.set(DataQuery.of("Enable"), enable)
			.set(DataQuery.of("Interval"), interval);
	}

}
