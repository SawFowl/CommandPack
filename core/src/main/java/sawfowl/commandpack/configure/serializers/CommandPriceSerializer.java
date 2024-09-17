package sawfowl.commandpack.configure.serializers;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.configure.configs.commands.CommandPrice;

public class CommandPriceSerializer implements TypeSerializer<Price> {

	@Override
	public Price deserialize(Type type, ConfigurationNode node) throws SerializationException {
		return new CommandPrice().builder().currency(node.node("Currency").getString()).money(node.node("Money").getDouble()).build();
	}

	@Override
	public void serialize(Type type, @Nullable Price price, ConfigurationNode node) throws SerializationException {
		node.node("Currency").set(price.getCurrency());
		node.node("Money").set(price.getMoney());
		if(node instanceof CommentedConfigurationNode commented) {
			commented.node("Currency").comment("The currency used. Both the currency symbol and its name are accepted.\nIf the specified currency will not be present on the server, the default currency will be used.");
			commented.node("Money").comment("The price a player will pay for completing a command.");
		}
	}

}
