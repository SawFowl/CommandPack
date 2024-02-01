package sawfowl.commandpack.configure.serializers;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import sawfowl.commandpack.api.data.command.Delay;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.RawSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;

public class CommandSettingSerializer implements TypeSerializer<Settings> {

	@Override
	public Settings deserialize(Type type, ConfigurationNode node) throws SerializationException {
		return new CommandSettings().builder()
			.setAliases(node.node("Aliases").getList(String.class, new ArrayList<String>()))
			.setCooldown(node.node("Cooldown").getLong(0))
			.setDelay(node.node("Delay").get(Delay.class))
			.setEnable(node.node("Enable").getBoolean(true))
			.setPrice(node.node("Price").get(Price.class))
			.setRawSettings(node.node("RawSettings").virtual() ? null : node.node("RawSettings").get(RawSettings.class))
			.build();
	}

	@Override
	public void serialize(Type type, @Nullable Settings settings, ConfigurationNode node) throws SerializationException {
		node.node("Aliases").setList(String.class, settings.getAliasesList());
		node.node("Cooldown").set(settings.getCooldown());
		node.node("Delay").set(settings.getDelay());
		node.node("Enable").set(settings.isEnable());
		node.node("Price").set(settings.getPrice());
		if(settings.getRawSettings() != null) node.node("RawSettings").set(settings.getRawSettings());
	}

}
