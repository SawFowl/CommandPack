package sawfowl.commandpack.configure.configs.commands;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.api.data.command.RawSettings;
import sawfowl.commandpack.api.data.command.UpdateTree;

@ConfigSerializable
public class RawSettingsImpl implements RawSettings {

	public RawSettingsImpl(){}

	public Builder builder() {
		return new Builder() {
			
			@Override
			public @NotNull RawSettings build() {
				return RawSettingsImpl.this;
			}
			
			@Override
			public Builder setUpdateTree(UpdateTree updateTree) {
				if(updateTree != null) RawSettingsImpl.this.updateTree = updateTree;
				return this;
			}
			
			@Override
			public Builder setGenerateRawTree(boolean value) {
				generateRawTree = value;
				return this;
			}
			
			@Override
			public Builder setAutoComplete(boolean value) {
				autocomplete = value;
				return this;
			}
		};
	}

	@Setting("UpdateTree")
	private UpdateTree updateTree = new UpdateRawTree();
	@Setting("Autocomplete")
	private boolean autocomplete = true;
	@Setting("GenerateRawTree")
	private boolean generateRawTree = true;

	@Override
	public UpdateTree getUpdateTree() {
		return updateTree;
	}

	@Override
	public boolean isAutoComplete() {
		return autocomplete;
	}

	@Override
	public boolean isGenerateRawTree() {
		return generateRawTree;
	}

	@Override
	public int contentVersion() {
		return 0;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("ContentVersion"), contentVersion())
				.set(DataQuery.of("UpdateTree"), updateTree)
				.set(DataQuery.of("Autocomplete"), autocomplete)
				.set(DataQuery.of("GenerateRawTree"), generateRawTree);
	}

}
