package sawfowl.commandpack.configure.serializers;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.configure.configs.commands.CancelRulesData;

public class CancelRulesSerializer implements TypeSerializer<CancelRules> {

	@Override
	public CancelRules deserialize(Type type, ConfigurationNode node) throws SerializationException {
		return new CancelRulesData().builder().allowMoving(node.node("AllowMoving").getBoolean()).allowOtherCommand(node.node("AllowOtherCommand").getBoolean()).build();
	}

	@Override
	public void serialize(Type type, @Nullable CancelRules rules, ConfigurationNode node) throws SerializationException {
		node.node("AllowMoving").set(rules.isAllowMoving());
		node.node("AllowOtherCommand").set(rules.isAllowOtherCommand());
		if(node instanceof CommentedConfigurationNode commented) {
			commented.node("AllowMoving").comment("Cancelling the execution a command when the player moves.");
			commented.node("AllowOtherCommand").comment("Cancelling the execution a command when a player uses another command.");
		}
	}

}
