package sawfowl.commandpack.configure.serializers;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import sawfowl.commandpack.api.data.command.UpdateTree;
import sawfowl.commandpack.configure.configs.commands.UpdateRawTree;

public class UpdateTreeSerializer implements TypeSerializer<UpdateTree> {

	@Override
	public UpdateTree deserialize(Type type, ConfigurationNode node) throws SerializationException {
		return new UpdateRawTree().builder().setEnable(node.node("Enable").getBoolean()).setInterval(node.node("Interval").getLong()).build();
	}

	@Override
	public void serialize(Type type, @Nullable UpdateTree tree, ConfigurationNode node) throws SerializationException {
		node.node("Enable").set(tree.isEnable());
		node.node("Interval").set(tree.getInterval());
		if(node instanceof CommentedConfigurationNode commented) {
			commented.node("Enable").comment("If false, the list of command arguments will not be updated by the timer.\nThis is useful for cases when it is not necessary to update the lists.\nFor example, if the command argument contains the ID of enchantments or entities.");
			commented.node("Interval").comment("Time in seconds between updates to the argument tree.");
		}
	}

}
