package sawfowl.commandpack.configure.serializers;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.configure.configs.commands.CommandPrice;

public class CommandPriceSerializer implements TypeSerializer<Price> {

	private final CommandPackInstance INSTANCE = CommandPackInstance.getInstance();

	@Override
	public Price deserialize(Type type, ConfigurationNode node) throws SerializationException {
		return new CommandPrice().builder().currency(node.node("Currency").getString()).money(node.node("Money").getDouble()).build();
	}

	@Override
	public void serialize(Type type, @Nullable Price price, ConfigurationNode node) throws SerializationException {
		node.node("Currency").set(price.getCurrency());
		node.node("Money").set(price.getMoney());
		if(node instanceof CommentedConfigurationNode commented) {
			commented.node("Currency").comment(getPrice().getCurrency());
			commented.node("Money").comment(getPrice().getMoney());
		}
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.Price getPrice() {
		return INSTANCE.getLocales().getSystemLocale().getComments().getCommandsConfig().getPrice();
	}

}
