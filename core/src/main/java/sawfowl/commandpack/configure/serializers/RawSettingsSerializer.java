package sawfowl.commandpack.configure.serializers;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import sawfowl.commandpack.api.data.command.RawSettings;
import sawfowl.commandpack.api.data.command.UpdateTree;
import sawfowl.commandpack.configure.configs.commands.RawSettingsImpl;

public class RawSettingsSerializer implements TypeSerializer<RawSettings> {

	@Override
	public RawSettings deserialize(Type type, ConfigurationNode node) throws SerializationException {
		return new RawSettingsImpl().builder()
			.setAutoComplete(node.node("AutoComplete").getBoolean())
			.setGenerateRawTree(node.node("GenerateRawTree").getBoolean())
			.setUpdateTree(node.node("UpdateTree").virtual() ? null : node.node("UpdateTree").get(UpdateTree.class))
			.build();
	}

	@Override
	public void serialize(Type type, @Nullable RawSettings rawSettings, ConfigurationNode node) throws SerializationException {
		node.node("AutoComplete").set(rawSettings.isAutoComplete());
		node.node("GenerateRawTree").set(rawSettings.isGenerateRawTree());
		node.node("UpdateTree").set(UpdateTree.class, rawSettings.getUpdateTree());
		if(node instanceof CommentedConfigurationNode commented) {
			commented.node("AutoComplete").comment("No effect if argument tree construction is enabled.");
			commented.node("GenerateRawTree").comment("If true, the argument tree construction will be performed.\nIf this option is enabled, it is recommended to enable automatic timed update of the argument tree for those commands whose argument lists may change.\nChanging lists can be, for example, a list of players or a list of warps.");
		}
	}

}
