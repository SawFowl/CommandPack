package sawfowl.commandpack.configure.serializers;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.api.data.command.Delay;

public class DelaySerializer implements TypeSerializer<Delay> {

	@Override
	public Delay deserialize(Type type, ConfigurationNode node) throws SerializationException {
		return Delay.builder().setSeconds(node.node("Seconds").getLong()).setCancelRules(node.node("CancelRules").get(CancelRules.class)).build();
	}

	@Override
	public void serialize(Type type, @Nullable Delay delay, ConfigurationNode node) throws SerializationException {
		node.node("Seconds").set(delay.getSeconds());
		node.node("CancelRules").set(delay.getCancelRules());
	}

}
